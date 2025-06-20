package com.handicrafts.controller.signin_signup_forget.via_page;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.UserDTO;
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

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.UUID;

@Controller
public class RegisterController {

    @Autowired
    private IUserService userService;

    @Autowired
    private CodeVerifyService codeVerifyService;

    @Autowired
    private ILogService<UserDTO> logService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "web/signup";
    }

    @PostMapping("/register")
    public String processRegistration(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "retypePassword", required = false) String retypePassword,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        // Nếu có parameter đăng ký tài khoản được gửi về
        if (type != null && type.equals("sendRegister")) {
            String emailError = "";
            String passwordError = "";

            // Tạo biến boolean lấy kết quả kiểm tra email từ database
            boolean isExistEmail = userService.existsByEmail(email);
            boolean isValid = true;

            // Kiểm tra xem Email có bị bỏ trống hay không
            if (!codeVerifyService.isBlankEmail(email)) {
                // Nếu không bị bỏ trống, kiểm tra xem email có hợp lệ không
                if (codeVerifyService.isValidEmail(email)) {
                    // Nếu hợp lệ, kiểm tra xem email có tồn tại trong database không
                    if (isExistEmail) {
                        // Tồn tại thì trả về lỗi và set vào model
                        emailError = "Email này đã tồn tại!";
                        model.addAttribute("emailError", emailError);
                        isValid = false;
                    }
                    // Không tồn tại lỗi gì thì xuống điều kiện khác
                } else {
                    // Nếu không hợp lệ, trả về lỗi
                    emailError = "Email không hợp lệ!";
                    model.addAttribute("emailError", emailError);
                    isValid = false;
                }
            } else {
                // Nếu bị bỏ trống, trả về lỗi
                emailError = "Email không được để trống!";
                model.addAttribute("emailError", emailError);
                isValid = false;
            }

            // Kiểm tra xem trường Mật khẩu và Nhập lại mật khẩu có bị để trống
            if (!codeVerifyService.isBlankPassword(password, retypePassword)) {
                // Nếu không bị bỏ trống, kiểm tra các điều kiện khác
                if (!codeVerifyService.containsSpace(password) || !codeVerifyService.containsSpace(retypePassword)) {
                    if (codeVerifyService.isLengthEnough(password)) {
                        if (!codeVerifyService.isSamePassword(password, retypePassword)) {
                            passwordError = "Mật khẩu và Nhập lại mật khẩu không khớp";
                            model.addAttribute("passwordError", passwordError);
                            isValid = false;
                        }
                    } else {
                        passwordError = "Mật khẩu phải có 6 ký tự trở lên";
                        model.addAttribute("passwordError", passwordError);
                        isValid = false;
                    }
                } else {
                    passwordError = "Mật khẩu không được chứa khoảng trắng";
                    model.addAttribute("passwordError", passwordError);
                    isValid = false;
                }
            } else {
                // Nếu bị bỏ trống, trả về lỗi
                passwordError = "Mật khẩu hoặc Nhập lại mật khẩu không được để trống";
                model.addAttribute("passwordError", passwordError);
                isValid = false;
            }

            // Nếu thành công thì tạo UserDTO và gọi service để đăng ký
            if (isValid) {
                UserDTO user = new UserDTO();
                String verifiedCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // Tạo mã 8 ký tự

                user.setEmail(email);
                user.setPassword(password); // PasswordEncoder sẽ được áp dụng trong service
                user.setUsername(email); // Sử dụng email làm username
                user.setStatus(false); // Tài khoản chưa kích hoạt

                // Thêm vai trò mặc định cho người dùng mới (thường là ROLE_USER)
                user.setRoles(Collections.singletonList("ROLE_USER"));

                try {
                    // Thêm log để debug
                    System.out.println("Đang tạo người dùng mới với email: " + email);

                    UserDTO createdUser = userService.createUser(user);
                    System.out.println("Đã tạo người dùng thành công: " + createdUser.getEmail());

                    // Lưu mã xác thực vào database
                    try {
                        codeVerifyService.setNewCodeByEmail(email, verifiedCode);
                        System.out.println("Đã lưu mã xác thực cho email: " + email);
                    } catch (Exception e) {
                        e.printStackTrace();
                        model.addAttribute("error", "Lỗi khi lưu mã xác thực: " + e.getMessage());
                        return "web/signup";
                    }

                    // Ghi log
                    try {
                        logService.log(request, "register", LogState.SUCCESS, LogLevel.ALERT, null, createdUser);
                        System.out.println("Đã ghi log đăng ký thành công");
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Không cần dừng quy trình nếu ghi log thất bại
                    }

                    // Gửi verifiedCode về Email
                    try {
                        SendEmailUtil.sendVerificationCode(email, verifiedCode);
                        System.out.println("Đã gửi email xác thực đến: " + email);
                    } catch (Exception e) {
                        e.printStackTrace();
                        model.addAttribute("error", "Đã đăng ký thành công nhưng không thể gửi email xác thực. Vui lòng liên hệ hỗ trợ.");
                        return "web/signup";
                    }

                    // Redirect với thông báo thành công
                    redirectAttributes.addFlashAttribute("email", email);
                    System.out.println("Chuyển hướng đến trang xác thực mã với email: " + email);
                    return "redirect:/code-verify"; // Đảm bảo endpoint này đúng
                } catch (Exception e) {
                    e.printStackTrace(); // In stack trace để debug
                    model.addAttribute("error", "Đã xảy ra lỗi trong quá trình đăng ký: " + e.getMessage());
                    return "web/signup";
                }
            } else {
                return "web/signup";
            }
        }
        return  "web/signup";
    }

    @GetMapping("/code-verify")
    public String showCodeVerifyPage(@RequestParam(value = "email", required = false) String email, Model model) {
        if (email != null) {
            model.addAttribute("email", email);
        }
        return "/web/code-verify";
    }
}
