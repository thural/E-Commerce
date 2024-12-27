package dev.thural.shopping_cart.model;

import dev.thural.shopping_cart.emums.Role;

import java.time.OffsetDateTime;

public class UserDto {

    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private OffsetDateTime dateOfBirth;
    private boolean accountLocked;
    private boolean enabled;
    private Role role;

}
