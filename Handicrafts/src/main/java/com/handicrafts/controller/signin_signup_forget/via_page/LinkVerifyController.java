package com.handicrafts.controller.signin_signup_forget.via_page;


import com.handicrafts.security.service.LinkVerifyService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// TODO: Làm thêm phần hết hạn cho verify code
@WebServlet(value = {"/link-verification"})
public class LinkVerifyController extends HttpServlet {
    private final LinkVerifyService linkVerifyService = new LinkVerifyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if (action != null) {
            if (action.equals("verify")) {
                String email = req.getParameter("email");
                String verifyCode = req.getParameter("verifyCode");
                String key = req.getParameter("key");
                if (linkVerifyService.isCorrectVerifiedCode(email, verifyCode) && linkVerifyService.isCorrectKey(email, key)) {
                    resp.sendRedirect("change-password.html?email=" + email + "&key=" + key);
                }
                else {
                    resp.sendRedirect("web/error-verify.html");
                }
            }
        }
    }
}