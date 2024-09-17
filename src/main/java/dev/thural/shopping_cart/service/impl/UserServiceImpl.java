package dev.thural.shopping_cart.service.impl;

import dev.thural.shopping_cart.entity.User;
import dev.thural.shopping_cart.model.RegistrationDto;
import dev.thural.shopping_cart.repository.UserRepository;
import dev.thural.shopping_cart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public boolean isDuplicateEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public User createUser(RegistrationDto dto) {
        var user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setPassword(encoder.encode(dto.getPassword()));
        return repository.save(user);
    }

}
