package dev.thural.shopping_cart.service.impl;

import dev.thural.shopping_cart.entity.User;
import dev.thural.shopping_cart.model.RegistrationDto;
import dev.thural.shopping_cart.repository.UserRepository;
import dev.thural.shopping_cart.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static dev.thural.shopping_cart.emums.Role.USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerNewUser(RegistrationDto dto) {
        if (!isUsernameAvailable(dto.getUsername())) {
            log.error("Username already exists: {}", dto.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }
        if (!isEmailAvailable(dto.getEmail())) {
            log.error("Email already exists: {}", dto.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }

        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setEmail(dto.getEmail());
        newUser.setFirstname(dto.getFirstname());
        newUser.setLastname(dto.getLastname());
        newUser.setRole(USER);
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        User savedUser = userRepository.save(newUser);
        log.info("User registered successfully: {}", savedUser.getUsername());
        return savedUser;
    }

    @Override
    @Transactional
    public User updateUserProfile(User existingUser, User updatedUserDetails) {
        existingUser.setFirstname(updatedUserDetails.getFirstname());
        existingUser.setLastname(updatedUserDetails.getLastname());

        if (!existingUser.getEmail().equals(updatedUserDetails.getEmail())) {
            if (userRepository.findUserByEmail(updatedUserDetails.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already in use");
            }
            existingUser.setEmail(updatedUserDetails.getEmail());
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void changePassword(User user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        log.info("Password changed for user: {}", user.getUsername());
    }

    public boolean validatePassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public boolean isUsernameAvailable(String username) {
        return userRepository.findUserByUsername(username).isEmpty();
    }

    public boolean isEmailAvailable(String email) {
        return userRepository.findUserByEmail(email).isEmpty();
    }
}
