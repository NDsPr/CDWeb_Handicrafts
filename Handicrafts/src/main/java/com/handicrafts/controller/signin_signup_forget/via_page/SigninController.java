package com.handicrafts.controller.signin_signup_forget.via_page;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.security.service.CodeVerifyService;
import com.handicrafts.service.IUserService;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.SendEmailUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;
import java.util.UUID;

@Controller
public class SigninController {

    @Autowired
    private IUserService userService;

    @Autowired
    private CodeVerifyService codeVerifyService;

    @Autowired
    private ILogService<UserDTO> logService;

    private ResourceBundle notifyBundle = ResourceBundle.getBundle("notify-message");

    @GetMapping("/signin")
    public String showSigninPage(
            @RequestParam(value = "message", required = false) String message,
            Model model) {

        // Nhận message khi bắt lỗi Authorization từ Filter
        if (message != null) {
            model.addAttribute("message", notifyBundle.getString(message));
        }
        return "signin";
    }

    @PostMapping("/signin")
    public String processSignin(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpSession session) {

        String emailError = null;

        // Kiểm tra các trường hợp email
        // Nếu email không để trống thì tiếp tục
        if (!codeVerifyService.isBlankEmail(email)) {
            // Nếu email không sai cú pháp thì tiếp tục
            if (codeVerifyService.isValidEmail(email)) {
                // Nếu email đã tồn tại trong database thì tiếp tục
                if (userService.existsByEmail(email)) {
                    // Nếu email và password không trùng khớp với database, trả về lỗi
                    if (!codeVerifyService.isValidLogin(email, password)) {
                        emailError = "Email hoặc mật khẩu không đúng!";
                        model.addAttribute("emailError", emailError);
                    }
                } else {
                    // Nếu email chưa tồn tại, thông báo email không tồn tại
                    emailError = "Email không tồn tại!";
                    model.addAttribute("emailError", emailError);
                }
            } else {
                // Nếu email sai cú pháp, trả về lỗi cú pháp
                emailError = "Email không hợp lệ!";
                model.addAttribute("emailError", emailError);
            }
        } else {
            // Nếu email để trống, trả về lỗi để trống
            emailError = "Email không được để trống!";
            model.addAttribute("emailError", emailError);
        }

        // Nếu có lỗi (emailError != null) thì trả về trang signin và thông báo lỗi
        if (emailError != null) {
            return "signin";
        } else {
            // Nếu không có lỗi gì, kiểm tra xem tài khoản đã active chưa
            UserDTO user = userService.findByEmail(email);

            if (user != null) {
                // Kiểm tra trạng thái active của tài khoản
                if (codeVerifyService.isActive(email)) {
                    // Ghi lại log nếu tài khoản đã active
                    logService.log((jakarta.servlet.http.HttpServletRequest) request, "login-active", LogState.SUCCESS, LogLevel.INFO, user, user);

                    // Thêm tài khoản vào Session
                    session.setAttribute("user", user);

                    // Authentication
                    // Kiểm tra role khi đăng nhập để redirect (1 là client, 2 là admin, 3 là mod)
                    if (user.getId() == 1) {
                        return "redirect:/home";
                    } else if (user.getId() == 2 || user.getId() == 3) {
                        return "redirect:/admin/home";
                    }
                } else {
                    // Nếu chưa active thì tạo ra verifiedCode mới, gửi về email người dùng và redirect sang trang verified
                    String verifiedCode = UUID.randomUUID().toString();
                    codeVerifyService.setNewCodeByEmail(email, verifiedCode);
                    SendEmailUtil.sendVerificationCode(email, verifiedCode);

                    // Ghi lại log nếu tài khoản cần verify
                    logService.log((jakarta.servlet.http.HttpServletRequest) request, "login-verify", LogState.SUCCESS, LogLevel.INFO, user, user);

                    redirectAttributes.addAttribute("email", email);
                    return "redirect:/code-verify";
                }
            }

            // Nếu không tìm thấy user (trường hợp này không nên xảy ra vì đã kiểm tra ở trên)
            model.addAttribute("emailError", "Đã xảy ra lỗi trong quá trình đăng nhập");
            return "signin";
        }
    }
}
