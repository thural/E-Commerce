package dev.thural.shopping_cart.repository;

import dev.thural.shopping_cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
