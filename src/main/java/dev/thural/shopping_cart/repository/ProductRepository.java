package dev.thural.shopping_cart.repository;

import dev.thural.shopping_cart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
