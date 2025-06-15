package com.handicrafts.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
public class SignOutController {

    @GetMapping("/signout")
    public RedirectView signOut(HttpSession session) {
        // Xóa thuộc tính "user" khỏi session
        session.removeAttribute("user");

        // Redirect về trang home
        return new RedirectView("/home");
    }
}
