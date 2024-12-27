package dev.thural.shopping_cart.service.impl;

import dev.thural.shopping_cart.emums.CartAction;
import dev.thural.shopping_cart.entity.Cart;
import dev.thural.shopping_cart.entity.CartItem;
import dev.thural.shopping_cart.entity.Product;
import dev.thural.shopping_cart.entity.User;
import dev.thural.shopping_cart.mapper.CartMapper;
import dev.thural.shopping_cart.model.CartDto;
import dev.thural.shopping_cart.model.request.CartRequest;
import dev.thural.shopping_cart.repository.CartRepository;
import dev.thural.shopping_cart.service.CartItemService;
import dev.thural.shopping_cart.service.CartService;
import dev.thural.shopping_cart.service.CommonService;
import dev.thural.shopping_cart.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CommonService commonService;
    private final ProductService productService;
    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final CartMapper cartMapper;

    @Transactional
    public Cart getCart(HttpSession session) {
        User user = commonService.getSignedUser();
        CartDto cartDto = (CartDto) session.getAttribute("cart");

        if (cartDto != null) {
            return cartRepository.findById(cartDto.getId())
                    .orElseThrow(EntityNotFoundException::new);
        }

        Cart cart = user.getCart();
        if (cart != null) return cart;

        return cartRepository.save(Cart.builder()
                .user(user)
                .build()
        );
    }

    @Transactional
    @Override
    public CartDto getCartDto(HttpSession session) {
        return cartMapper.toDto(getCart(session));
    }

    public Cart addItemToCart(Cart cart, Product product) {
        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }
        return cartRepository.save(cart);
    }

    public CartDto addItemToCartById(HttpSession session, Long productId) {
        Product product = productService.getProductById(productId)
                .orElseThrow(EntityNotFoundException::new);
        Cart cart = getCart(session);
        Cart updatedCart = addItemToCart(cart, product);
        return cartMapper.toDto(updatedCart);
    }

    @Override
    public CartDto handleCartAction(HttpSession session, CartRequest request) {
        if (request.getAction().equals(CartAction.INCREMENT))
            return addItemToCartById(session, request.getItemId());
        else return removeItemFromCartById(session, request.getItemId());
    }

    public Cart removeItemFromCart(Cart cart, CartItem cartItem) {
        cart.getCartItems().remove(cartItem);
        return cartRepository.save(cart);
    }

    @Override
    public CartDto removeItemFromCartById(HttpSession session, Long productId) {
        Cart cart = getCart(session);
        CartItem cartItem = cartItemService.getCartItemById(productId);
        Cart updatedCart = removeItemFromCart(cart, cartItem);
        return cartMapper.toDto(updatedCart);
    }
}