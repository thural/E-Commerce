package dev.thural.shopping_cart.mapper;

import dev.thural.shopping_cart.entity.User;
import dev.thural.shopping_cart.model.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
}
