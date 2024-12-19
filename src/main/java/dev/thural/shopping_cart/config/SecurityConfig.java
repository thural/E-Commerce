package dev.thural.shopping_cart.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/about").permitAll()
                        .requestMatchers("/products/create").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(
                                "/static/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/icons/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/register", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/products", true)
                        .failureUrl("/login?error=true")
                        .failureHandler((request, response, exception) -> {
                            log.error("Login failed: {}", exception.getMessage());
                            response.sendRedirect("/login?error=true");
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
