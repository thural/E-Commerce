package dev.thural.shopping_cart.service.impl;

import dev.thural.shopping_cart.entity.User;
import dev.thural.shopping_cart.mapper.UserMapper;
import dev.thural.shopping_cart.model.UserDto;
import dev.thural.shopping_cart.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final UserMapper userMapper;

    @Override
    public User getSignedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public UserDto getSignedUserDto() {
        return userMapper.toDto(getSignedUser());
    }
}
