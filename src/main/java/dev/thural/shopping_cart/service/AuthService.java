package dev.thural.shopping_cart.service;

import dev.thural.shopping_cart.entity.User;
import dev.thural.shopping_cart.model.RegistrationDto;

public interface AuthService {

    public User registerNewUser(RegistrationDto dto);

    public User updateUserProfile(User existingUser, User updatedUserDetails);

    void changePassword(User user, String newPassword);
}
