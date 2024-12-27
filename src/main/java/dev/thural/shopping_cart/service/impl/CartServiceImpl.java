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
        CartDto cartDto = (CartDto) session.getAttribute("cart");
        if (cartDto != null) {
            return cartRepository.findById(cartDto.getId())
                    .orElseGet(this::createNewCart);
        }

        User user = commonService.getSignedUser();
        return user.getCart() != null ? user.getCart() : createNewCart();
    }

    private Cart createNewCart() {
        return cartRepository.save(Cart.builder()
                .user(commonService.getSignedUser())
                .build());
    }

    @Transactional
    @Override
    public CartDto getCartDto(HttpSession session) {
        return cartMapper.toDto(getCart(session));
    }

    public Cart addItemToCart(Cart cart, Product product) {
        cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + 1),
                        () -> cart.getCartItems().add(createCartItem(cart, product))
                );
        return cartRepository.save(cart);
    }

    private CartItem createCartItem(Cart cart, Product product) {
        return CartItem.builder()
                .product(product)
                .quantity(1)
                .cart(cart)
                .build();
    }

    public CartDto addItemToCartById(HttpSession session, Long productId) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
        return cartMapper.toDto(addItemToCart(getCart(session), product));
    }

    @Override
    public CartDto handleCartAction(HttpSession session, CartRequest request) {
        return request.getAction().equals(CartAction.INCREMENT) ?
                addItemToCartById(session, request.getItemId()) :
                removeItemFromCartById(session, request.getItemId());
    }

    public Cart removeItemFromCart(Cart cart, CartItem cartItem) {
        cart.getCartItems().stream()
                .filter(item -> item.equals(cartItem))
                .filter(item -> item.getQuantity() > 0)
                .forEach(item -> item.setQuantity(item.getQuantity() - 1));
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