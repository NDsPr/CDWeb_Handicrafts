package com.handicrafts.security.filter;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.util.SessionUtil;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/user-info", "/order-history", "/order-detail-history", "/user-change-password", "/api/admin/*"})
public class Authentication implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Kiểm tra xem người dùng đã đăng nhập chưa
        HttpSession session = request.getSession(false); // Lấy session hiện tại nếu có, không tạo mới
        UserDTO user = (UserDTO) SessionUtil.getInstance().getValue(request, "user");

        if (session == null || user == null) {
            // Người dùng chưa đăng nhập, chuyển hướng về trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/signin?message=not_permission");
        } else {
            // Người dùng đã đăng nhập, cho phép truy cập tiếp
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
