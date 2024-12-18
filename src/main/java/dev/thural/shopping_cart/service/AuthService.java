package dev.thural.shopping_cart.service;

import dev.thural.shopping_cart.entity.User;
import dev.thural.shopping_cart.model.RegistrationDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    User registerNewUser(RegistrationDto dto);

    void logout(HttpServletRequest request, HttpServletResponse response);

    User updateUserProfile(User existingUser, User updatedUserDetails);

    void changePassword(User user, String newPassword);
}
