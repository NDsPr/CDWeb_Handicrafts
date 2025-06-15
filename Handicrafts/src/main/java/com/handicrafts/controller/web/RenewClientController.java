package com.handicrafts.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RenewClientController {

    @GetMapping("/renew-password")
    public String renewPassword() {
        // Trả về tên của template/view
        return "renew-password-client";
    }
}
