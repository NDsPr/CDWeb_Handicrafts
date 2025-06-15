package com.handicrafts.security.filter;

import com.handicrafts.bean.Cart;
import com.handicrafts.bean.UserBean;
import com.handicrafts.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// TODO: Nên làm tất cả Servlet đều forward để kiểm tra trường hợp endWiths(".jsp")
// TODO: Filter cho cả việc thêm vào giỏ hàng và mua sản phẩm
public class Authorization implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        UserBean user = (UserBean) SessionUtil.getInstance().getValue(request, "user");

        if (user != null) {
            // Tạo cart ngay sau khi thêm tài khoản vào Session
            if (SessionUtil.getInstance().getValue(request, "cart") == null) {
                Cart cart = new Cart();
                SessionUtil.getInstance().putValue(request, "cart", cart);
            }
        }

        // Filter các url bắt đầu bằng "/admin" hoặc có chứa "admin"
        if (uri.startsWith("/admin") || uri.contains("admin")) {
            // Nếu có tồn tại Session thì tiếp tục
            if (user != null) {
                // Nếu roleId là 2 (Admin) hoặc 3 (Mod) thì cho qua
                if (user.getRoleId() == 2 || user.getRoleId() == 3) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    // Nếu không phải thì trả về trang home và nhắc nhở không có quyền truy cập
                    response.sendRedirect(request.getContextPath() + "/home?message=not_permission");
                }
            } else {
                // Nếu chưa tồn tại Session, điều hướng sang trang login
                response.sendRedirect(request.getContextPath() + "/signin?message=must_login");
            }
        } else {
            // Nếu không chứa gì liên quan đến admin thì cho qua
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
