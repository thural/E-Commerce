package dev.thural.shopping_cart.config;

import dev.thural.shopping_cart.entity.User;
import dev.thural.shopping_cart.service.CartService;
import dev.thural.shopping_cart.service.CommonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
            User signedUser = commonService.getSignedUser();
            session.setAttribute("user", signedUser);
        }

        cartService.getCart(session);

        return true;
    }
}
