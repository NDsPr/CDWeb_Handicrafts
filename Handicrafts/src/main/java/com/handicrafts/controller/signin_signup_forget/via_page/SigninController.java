package com.handicrafts.controller.signin_signup_forget.via_page;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.security.service.CodeVerifyService;
import com.handicrafts.service.IUserService;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.SendEmailUtil;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class SigninController {

    private final IUserService userService;
    private final CodeVerifyService codeVerifyService;
    private final ILogService<UserDTO> logService;
    private final Environment environment;

    public SigninController(
            IUserService userService,
            CodeVerifyService codeVerifyService,
            ILogService<UserDTO> logService,
            Environment environment) {
        this.userService = userService;
        this.codeVerifyService = codeVerifyService;
        this.logService = logService;
        this.environment = environment;
    }
    @GetMapping("/signin")
    public String showSigninPage(
            @RequestParam(value = "message", required = false) String message,
            Model model) {

        // Nhận message khi bắt lỗi Authorization từ Filter
        if (message != null) {
            model.addAttribute("message", environment.getProperty("notify." + message, message));
        }

        // Return the correct path to the template
        return "web/signin";
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

        // Lấy các thông báo lỗi từ Environment hoặc sử dụng giá trị mặc định
        String invalidLoginMessage = environment.getProperty(
                "signin.error.invalid-login",
                "Email hoặc mật khẩu không đúng!");

        String emailNotExistMessage = environment.getProperty(
                "signin.error.email-not-exist",
                "Email không tồn tại!");

        String invalidEmailMessage = environment.getProperty(
                "signin.error.invalid-email",
                "Email không hợp lệ!");

        String blankEmailMessage = environment.getProperty(
                "signin.error.blank-email",
                "Email không được để trống!");

        String generalErrorMessage = environment.getProperty(
                "signin.error.general",
                "Đã xảy ra lỗi trong quá trình đăng nhập");

        // Kiểm tra các trường hợp email
        // Nếu email không để trống thì tiếp tục
        if (!codeVerifyService.isBlankEmail(email)) {
            // Nếu email không sai cú pháp thì tiếp tục
            if (codeVerifyService.isValidEmail(email)) {
                // Nếu email đã tồn tại trong database thì tiếp tục
                if (userService.existsByEmail(email)) {
                    // Nếu email và password không trùng khớp với database, trả về lỗi
                    if (!codeVerifyService.isValidLogin(email, password)) {
                        emailError = invalidLoginMessage;
                        model.addAttribute("emailError", emailError);
                    }
                } else {
                    // Nếu email chưa tồn tại, thông báo email không tồn tại
                    emailError = emailNotExistMessage;
                    model.addAttribute("emailError", emailError);
                }
            } else {
                // Nếu email sai cú pháp, trả về lỗi cú pháp
                emailError = invalidEmailMessage;
                model.addAttribute("emailError", emailError);
            }
        } else {
            // Nếu email để trống, trả về lỗi để trống
            emailError = blankEmailMessage;
            model.addAttribute("emailError", emailError);
        }

        // Lấy tên view từ Environment
        String signinView = environment.getProperty("views.signin", "signin");
        String homeRedirect = environment.getProperty("redirect.home", "redirect:/home");
        String adminHomeRedirect = environment.getProperty("redirect.admin.home", "redirect:/admin/home");
        String verifyRedirect = environment.getProperty("redirect.verify", "redirect:/code-verify");

        // Nếu có lỗi (emailError != null) thì trả về trang signin và thông báo lỗi
        if (emailError != null) {
            return signinView;
        } else {
            // Nếu không có lỗi gì, kiểm tra xem tài khoản đã active chưa
            UserDTO user = userService.findByEmail(email);

            if (user != null) {
                // Kiểm tra trạng thái active của tài khoản
                if (codeVerifyService.isActive(email)) {
                    // Ghi lại log nếu tài khoản đã active
                    logService.log(request, "login-active", LogState.SUCCESS, LogLevel.INFO, user, user);

                    // Thêm tài khoản vào Session
                    session.setAttribute("user", user);

                    // Authentication
                    // Kiểm tra role khi đăng nhập để redirect (1 là client, 2 là admin, 3 là mod)
                    int clientRoleId = Integer.parseInt(environment.getProperty("role.client.id", "1"));
                    int adminRoleId = Integer.parseInt(environment.getProperty("role.admin.id", "2"));
                    int modRoleId = Integer.parseInt(environment.getProperty("role.mod.id", "3"));

                    if (user.getId() == clientRoleId) {
                        return homeRedirect;
                    } else if (user.getId() == adminRoleId || user.getId() == modRoleId) {
                        return adminHomeRedirect;
                    }
                } else {
                    // Nếu chưa active thì tạo ra verifiedCode mới, gửi về email người dùng và redirect sang trang verified
                    String verifiedCode = UUID.randomUUID().toString();
                    codeVerifyService.setNewCodeByEmail(email, verifiedCode);
                    SendEmailUtil.sendVerificationCode(email, verifiedCode);

                    // Ghi lại log nếu tài khoản cần verify
                    logService.log(request, "login-verify", LogState.SUCCESS, LogLevel.INFO, user, user);

                    redirectAttributes.addAttribute("email", email);
                    return verifyRedirect;
                }
            }

            // Nếu không tìm thấy user (trường hợp này không nên xảy ra vì đã kiểm tra ở trên)
            model.addAttribute("emailError", generalErrorMessage);
            return signinView;
        }
    }
}
