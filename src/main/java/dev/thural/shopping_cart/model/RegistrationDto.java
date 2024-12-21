package dev.thural.shopping_cart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationDto extends BaseResponse {

    @NotEmpty(message = "username field is empty")
    @NotNull(message = "username is required")
    private String username;

    @NotEmpty(message = "firstname field is empty")
    @NotNull(message = "firstname is required")
    private String firstname;

    @NotEmpty(message = "lastname field is empty")
    @NotNull(message = "lastname is required")
    private String lastname;

    @Email(message = "invalid email format")
    @NotEmpty(message = "email field is empty")
    @NotNull(message = "email is required")
    private String email;

    @NotEmpty(message = "password field is empty")
    @NotNull(message = "password is required")
    @Size(min = 8, message = "password should be 8 characters long minimum")
    private String password;

    @NotEmpty(message = "password field is empty")
    @NotNull(message = "password is required")
    @Size(min = 8, message = "password should be 8 characters long minimum")
    private String confirmPassword;

}
