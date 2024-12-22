package dev.thural.shopping_cart.entity;

import dev.thural.shopping_cart.emums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.Collection;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails, Principal {

    @NotNull
    @NotBlank
    @Column(length = 32, unique = true)
    private String username;

    @NotNull
    @NotBlank
    @Column(length = 32, unique = true)
    private String email;

    @NotNull
    @NotBlank
    private String password;

    private String firstname;
    private String lastname;
    private OffsetDateTime dateOfBirth;
    private boolean accountLocked;
    private boolean enabled;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    @NotNull
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String fullName() {
        return getFirstname() + " " + getLastname();
    }

    @Override
    public String getName() {
        return username;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }

    @PrePersist
    void initAccount() {
        setEnabled(true);
        setAccountLocked(false);
    }

}