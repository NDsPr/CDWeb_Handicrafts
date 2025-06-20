package com.handicrafts.controller.signin_signup_forget.via_page;

import com.handicrafts.security.service.CodeVerifyService;
import com.handicrafts.util.SendEmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/code-verification")
public class CodeVerifyController {

    private static final Logger logger = LoggerFactory.getLogger(CodeVerifyController.class);

    @Autowired
    private CodeVerifyService codeVerifyService;

    @Autowired
    private SendEmailUtil sendEmailUtil;

    @GetMapping
    public String showVerificationPage(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "confirm", required = false) String confirm,
            Model model,
            RedirectAttributes redirectAttributes) {

        logger.info("Xử lý GET /code-verification với type={}, email={}", type, email);

        if (email == null) {
            logger.warn("Email không được cung cấp, chuyển hướng đến trang đăng nhập");
            return "redirect:/login";
        }

        model.addAttribute("email", email);

        if (type != null && type.equals("resendCode")) {
            try {
                // Tạo verifiedCode mới
                String verifiedCode = codeVerifyService.generateVerifiedCode();
                logger.info("Đã tạo mã xác thực mới: {} cho email: {}", verifiedCode, email);

                // Gửi vào email cho người dùng
                SendEmailUtil.sendVerificationCode(email, verifiedCode);
                logger.info("Đã gửi mã xác thực mới đến email: {}", email);

                // Set vào database
                codeVerifyService.setNewCodeByEmail(email, verifiedCode);
                logger.info("Đã lưu mã xác thực mới vào database cho email: {}", email);

                // Thông báo cho người dùng đẫ gửi code mới thông qua 1 String
                model.addAttribute("confirm", "confirm");

                return "web/code-verify";
            } catch (Exception e) {
                logger.error("Lỗi khi gửi lại mã xác thực: {}", e.getMessage(), e);
                model.addAttribute("error", "Không thể gửi lại mã xác thực. Vui lòng thử lại sau.");
                return "web/code-verify";
            }
        }

        return "web/code-verify";
    }

    @PostMapping
    public String verifyCode(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "verifyInput", required = false) String verifyInput,
            Model model) {

        logger.info("Xử lý POST /code-verification với type={}, email={}", type, email);

        if (email == null) {
            logger.warn("Email không được cung cấp, chuyển hướng đến trang đăng nhập");
            return "redirect:/login";
        }

        model.addAttribute("email", email);

        if (type != null && type.equals("verified")) {
            String codeError = null;

            // Kiểm tra các trường hợp nhập VerifieCode
            if (!codeVerifyService.isBlankVerification(verifyInput)) {
                // Nếu không trống, kiểm tra xem có đủ 8 ký tự hay không
                if (codeVerifyService.isCorrectLength(verifyInput)) {
                    // Nếu đủ, kiểm tra xem có khớp verify code không
                    if (codeVerifyService.isCorrectVerifiedCode(email, verifyInput)) {
                        // Nếu khớp, kích hoạt tài khoản và chuyển hướng
                        try {
                            codeVerifyService.activeAccount(email);
                            codeVerifyService.setEmptyCode(email);
                            logger.info("Xác thực thành công cho email: {}", email);
                            return "web/verify-success"; // Thay đổi ở đây
                        } catch (Exception e) {
                            logger.error("Lỗi khi kích hoạt tài khoản: {}", e.getMessage(), e);
                            codeError = "Lỗi khi kích hoạt tài khoản: " + e.getMessage();
                        }
                    } else {
                        codeError = "Mã xác thực không tồn tại!";
                        logger.warn("Mã xác thực không tồn tại cho email: {}", email);
                    }
                } else {
                    codeError = "Mã xác thực không hợp lệ, phải có 8 ký tự!";
                    logger.warn("Mã xác thực không đủ 8 ký tự cho email: {}", email);
                }
            } else {
                codeError = "Mã xác thực không được để trống!";
                logger.warn("Mã xác thực bị bỏ trống cho email: {}", email);
            }

            if (codeError != null) {
                model.addAttribute("codeError", codeError);
            }
        }

        return "web/code-verify";
    }
}