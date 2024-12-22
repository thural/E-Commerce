package dev.thural.shopping_cart.repository;

import dev.thural.shopping_cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
