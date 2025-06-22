package com.handicrafts.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

// Sử dụng jakarta thay vì javax
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class SignOutController {

    @GetMapping("/signout")
    public RedirectView signOut(HttpServletRequest request) {
        // Lấy session từ request
        HttpSession session = request.getSession();

        // Xóa thuộc tính "user" khỏi session
        session.removeAttribute("user");

        // Redirect về trang home
        return new RedirectView("/");
    }
}
