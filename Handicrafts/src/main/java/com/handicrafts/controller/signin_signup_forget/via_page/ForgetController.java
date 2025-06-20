package com.handicrafts.controller.signin_signup_forget.via_page;

import com.handicrafts.security.service.CodeVerifyService;
import com.handicrafts.security.service.LinkVerifyService;
import com.handicrafts.util.SendEmailUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest; // Thay đổi từ javax sang jakarta
import jakarta.servlet.http.HttpServletResponse; // Thay đổi từ javax sang jakarta
import java.io.IOException;
import java.util.UUID;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/forget")
public class ForgetController {
    private final LinkVerifyService linkVerifyService = new LinkVerifyService();
    private final CodeVerifyService codeVerifyService = new CodeVerifyService();

    @GetMapping
    public String doGet(
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "email", required = false) String email,
            HttpServletRequest req,
            HttpServletResponse resp) throws IOException {

        if (action != null) {
            if (action.equals("resend")) {
                UUID uuid = UUID.randomUUID();
                String verifiedCode = uuid.toString();
                linkVerifyService.saveNewCodeByEmail(email, verifiedCode);
                String verifiedLink = "http://" + req.getServerName() + ":" + req.getLocalPort() + req.getContextPath() + "/link-verification?email=" + email + "&verifyCode=" + verifiedCode + "&action=verify";
                SendEmailUtil.sendVerificationCode(email, verifiedLink);
                return "redirect:/web/link-verify.html?email=" + email;
            }
        }

        return "web/forget"; // Thay đổi để trỏ đến template Thymeleaf thay vì HTML tĩnh
    }

    @PostMapping
    public String doPost(
            @RequestParam(value = "email", required = false) String email,
            Model model,
            HttpServletRequest req,
            HttpServletResponse resp) throws IOException {

        String emailError = null;

        // Validate các trường hợp nhập email
        if (!linkVerifyService.isBlankInput(email)) {
            if (linkVerifyService.isValidEmail(email)) {
                if (linkVerifyService.isExistEmail(email)) {
                    if (linkVerifyService.isActiveAccount(email)) {
                        UUID uuid = UUID.randomUUID();
                        UUID keyUUID = UUID.randomUUID();
                        String verifiedCode = uuid.toString();
                        String key = keyUUID.toString();
                        linkVerifyService.saveNewCodeByEmail(email, verifiedCode);
                        linkVerifyService.saveKeyByEmail(email, key);
                        String verifiedLink = "http://" + req.getServerName() + ":" + req.getLocalPort() + req.getContextPath() + "/link-verification?email=" + email + "&verifyCode=" + verifiedCode + "&key=" + key + "&action=verify";
                        SendEmailUtil.sendVerificationCode(email, verifiedLink);
                        return "redirect:/web/link-verify.html?email=" + email;
                    } else {
                        String verifyCode = codeVerifyService.generateVerifiedCode();
                        SendEmailUtil.sendVerificationCode(email, verifyCode);
                        codeVerifyService.setNewCodeByEmail(email, verifyCode);
                        return "redirect:/web/code-verify.html?email=" + email;
                    }
                } else {
                    emailError = "Email không tồn tại!";
                }
            } else {
                emailError = "Email không hợp lệ!";
            }
        } else {
            emailError = "Email không được để trống";
        }

        if (emailError != null) {
            model.addAttribute("emailError", emailError);
            return "web/forget"; // Thay đổi để trỏ đến template Thymeleaf thay vì HTML tĩnh
        }

        return "redirect:/signin"; // Thêm redirect mặc định
    }
}
