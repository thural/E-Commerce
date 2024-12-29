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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

    @Override
    @Transactional
    public CartDto getCartDto(HttpSession session) {
        return cartMapper.toDto(getCart(session));
    }

    @Transactional
    public CartDto incrementItemQuantity(HttpSession session, Long productId) {
        Cart cart = getCart(session);

        CartItem existingItem = findCartItemByProduct(cart, productId);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            cart.addItem(createCartItem(cart, productId));
        }

        return cartMapper.toDto(cart);
    }

    @Transactional
    public CartDto decrementItemQuantity(HttpSession session, Long productId) {
        Cart cart = getCart(session);

        CartItem cartItem = findCartItemByProduct(cart, productId);

        int newQuantity = cartItem.getQuantity() - 1;
        if (newQuantity <= 0) {
            removeCartItem(cart, cartItem);
        } else {
            cartItem.setQuantity(newQuantity);
        }

        return cartMapper.toDto(cart);
    }

    @Transactional
    public CartDto removeCartItem(HttpSession session, Long itemId) {
        Cart cart = getCart(session);
        CartItem cartItem = cartItemService.getCartItemById(itemId);
        removeCartItem(cart, cartItem);
        return cartMapper.toDto(cart);
    }

    private void removeCartItem(Cart cart, CartItem cartItem) {
        cart.removeItem(cartItem);
        cartItemService.deleteCartItem(cartItem);
    }

    private CartItem findCartItemByProduct(Cart cart, Long productId) {
        Product product = productService.getProductById(productId)
                .orElseThrow(EntityNotFoundException::new);
        return cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);
    }

    private CartItem createCartItem(Cart cart, Long productId) {
        Product product = productService.getProductById(productId)
                .orElseThrow(EntityNotFoundException::new);
        return CartItem.builder()
                .product(product)
                .quantity(1)
                .cart(cart)
                .build();
    }

    @Override
    @Transactional
    public CartDto handleCartAction(HttpSession session, CartRequest request) {
        return request.getAction().equals(CartAction.INCREMENT) ?
                incrementItemQuantity(session, request.getProductId()) :
                decrementItemQuantity(session, request.getProductId());
    }
}