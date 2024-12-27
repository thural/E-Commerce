package dev.thural.shopping_cart.config;

import dev.thural.shopping_cart.model.CartDto;
import dev.thural.shopping_cart.model.UserDto;
import dev.thural.shopping_cart.service.CartService;
import dev.thural.shopping_cart.service.CommonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {

    private final CommonService commonService;
    private final CartService cartService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        HttpSession session = request.getSession();

        if (session.getAttribute("user") == null) {
            UserDto signedUserDto = commonService.getSignedUserDto();
            session.setAttribute("user", signedUserDto);
        }

        CartDto cartDto = cartService.getCartDto(session);
        session.setAttribute("cart", cartDto);
        log.info("cartDto on the interceptor: {}", cartDto);
        return true;
    }
}
