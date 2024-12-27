package dev.thural.shopping_cart.service;

import dev.thural.shopping_cart.entity.User;
import dev.thural.shopping_cart.model.UserDto;

public interface CommonService {
    User getSignedUser();

    UserDto getSignedUserDto();
}
