package com.handicrafts.security.filter;

import com.ltw.bean.UserBean;
import com.ltw.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/user-info", "/order-history", "/order-detail-history", "/user-change-password", "/api/admin/*"})
public class Authentication implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Kiểm tra xem người dùng đã đăng nhập chưa
        HttpSession session = SessionUtil.getInstance().getSession(request);
        UserBean user = (UserBean) SessionUtil.getInstance().getValue(request, "user");
        if (session == null || user == null) {
            // Người dùng chưa đăng nhập, chuyển hướng về trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/signin?message=not_permission");
        } else {
            // Người dùng đã đăng nhập, cho phép truy cập tiếp
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
