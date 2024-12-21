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
public class LoginDto extends BaseResponse {

    @Email(message = "invalid email format")
    @NotEmpty(message = "email field is empty")
    @NotNull(message = "email is required")
    @Size(min = 1, max = 256)
    private String email;

    @NotEmpty(message = "password field is empty")
    @NotNull(message = "password is required")
    @Size(min = 8, max = 32, message = "password length should be in range 8 and 32 characters")
    private String password;

}
