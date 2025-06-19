package com.handicrafts.security.filter;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.util.SessionUtil;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/cart-management", "/cart-adding", "/cart-updating", "/cart-delete",
        "/checkout", "/api/cart-adding", "/api/cart-updating"})
public class CartAndPaymentSecurity implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();

        // Filter các url bắt đầu bằng "/cart" hoặc có chứa "cart"
        if (uri.startsWith("/cart") || uri.contains("cart")) {
            UserDTO user = (UserDTO) SessionUtil.getInstance().getValue(request, "user");
            String xRequestedWith = request.getHeader("X-Requested-With");

            if ("XMLHttpRequest".equals(xRequestedWith)) {
                if (user == null) {
                    // Nếu là AJAX request thì phải chuyển hướng bằng client-side => Send redirect link bằng JSON lên client
                    // Nếu chưa tồn tại Session, điều hướng sang trang login
                    // Bắt buộc phải chuyển hướng bằng client-side => Send redirect link bằng JSON lên client
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json");
                    String redirectJson = "{\"redirectLink\": \"http://localhost:8080" + request.getContextPath() + "/signin?message=must_login\"}";
                    response.getWriter().write(redirectJson);
                    return;
                }
            } else {
                if (user == null) {
                    // Nếu không là AJAX request thì sendRedirct thông qua servers
                    response.sendRedirect(request.getContextPath() + "/signin?message=must_login");
                    return;
                }
            }
        }

        if (uri.startsWith("/checkout") || uri.contains("checkout")) {
            UserDTO user = (UserDTO) SessionUtil.getInstance().getValue(request, "user");
            if (user == null) {
                // Nếu chưa tồn tại Session, điều hướng sang trang login
                response.sendRedirect(request.getContextPath() + "/signin?message=must_login");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
