package dev.thural.shopping_cart.service;

import dev.thural.shopping_cart.entity.User;
import dev.thural.shopping_cart.model.RegistrationDto;

public interface UserService {

    boolean isDuplicateEmail(String email);

    User createUser(RegistrationDto dto);
}
