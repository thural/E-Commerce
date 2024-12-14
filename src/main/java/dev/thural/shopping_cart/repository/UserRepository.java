package dev.thural.shopping_cart.repository;

import dev.thural.shopping_cart.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByUsernameIsLikeIgnoreCase(String userName, Pageable pageable);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
