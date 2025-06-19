package com.handicrafts.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.oauth2.CustomOAuth2User;
import com.handicrafts.service.IUserService;
import com.handicrafts.util.EncryptPasswordUtil;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/check-mail")
    public ResponseEntity<String> checkEmail(@RequestParam(name = "email", required = false) String email) {
        UserDTO user = userService.findByEmail(email);
        if (user != null && Boolean.TRUE.equals(user.getIsEnable())) {
            return ResponseEntity.ok(user.getEmail());
        }
        return ResponseEntity.ok("");
    }

    @PostMapping("/dang-ky")
    public ModelAndView postRegister(@ModelAttribute("User") UserDTO user) {
        ModelAndView mav = new ModelAndView("web/confirmCode.html");
        try {
            // Tạo mã xác nhận ngẫu nhiên
            String verifiedCode = UUID.randomUUID().toString();
            user.setConfirmToken(verifiedCode); // Giả sử UserDTO có trường verifiedCode
            user.setStatus(true); // Trạng thái hoạt động

            // Sử dụng phương thức createUser có sẵn trong IUserService
            UserDTO savedUser = userService.createUser(user);

            if (savedUser != null) {
                // Thêm userId vào model để hiển thị trang xác nhận
                mav.addObject("userId", savedUser.getId());
                mav.addObject("email", savedUser.getEmail());

                // Lưu ý: Trong UserServiceImp không có phương thức gửi email
                // Bạn có thể cần tạo một service riêng để gửi email
                // emailService.sendVerificationEmail(savedUser.getEmail(), verifiedCode);
            } else {
                mav = new ModelAndView("web/register.html");
                mav.addObject("message", "Đăng ký không thành công");
            }
        } catch (Exception e) {
            mav = new ModelAndView("web/register.html");
            mav.addObject("message", "Đăng ký không thành công: " + e.getMessage());
        }
        return mav;
    }

    @GetMapping("/quen-mat-khau")
    public ModelAndView getForgetPassword() {
        return new ModelAndView("web/forget.html");
    }

    @PostMapping("/gui-mail-quen-mat-khau")
    public ModelAndView sendMailForgetPassword(@RequestParam(name = "mailForgot") String email) {
        ModelAndView mav = new ModelAndView("web/forget.html");

        try {
            // Sử dụng phương thức resetPassword có sẵn trong IUserService
            userService.resetPassword(email);

            // Nếu thành công, hiển thị thông báo
            mav.addObject("message", "Vui lòng kiểm tra email để đặt lại mật khẩu");
            mav.addObject("success", true);
        } catch (Exception e) {
            // Nếu có lỗi (email không tồn tại), hiển thị thông báo lỗi
            mav.addObject("message", "Tài khoản không tồn tại hoặc có lỗi xảy ra");
            mav.addObject("success", false);
        }

        return mav;
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserDTO> getUser(Authentication authentication) {
        if (authentication != null) {
            UserDTO user;
            if (authentication.getPrincipal() instanceof CustomOAuth2User) {
                CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                user = userService.findByEmail(oauthUser.getAttribute("email"));
            } else {
                user = userService.findByEmail(authentication.getName());
            }

            if (user != null) {
                user.setPassword(null); // Không trả về mật khẩu
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserDTO());
    }

    @GetMapping("/thong-tin-tai-khoan")
    public ModelAndView information() {
        return new ModelAndView("web/client-userinfo.html");
    }

    @PostMapping("/cap-nhat-thong-tin")
    public ModelAndView changeInformation(@ModelAttribute(name = "user") UserDTO user,
                                          Authentication authentication) {
        ModelAndView mav = new ModelAndView("web/client-userinfo.html");

        userService.changeInformation(user);
        mav.addObject("message", "Cập nhật thông tin thành công");

        if (authentication != null) {
            return mav;
        }
        return new ModelAndView("web/signin.html");
    }

    @GetMapping("/kiem-tra-mat-khau")
    public boolean checkPass(@RequestParam(name = "oldPassword") String oldPass,
                             Authentication authentication) {
        String userEmail = "";
        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            userEmail = oAuth2User.getAttribute("email");
        } else {
            userEmail = authentication.getName();
        }

        return userService.checkPass(userEmail, oldPass);
    }

    @PostMapping("/doi-mat-khau")
    public ModelAndView changePassword(@RequestParam(name = "password") String newPass,
                                       Authentication authentication) {
        ModelAndView mav = new ModelAndView("web/client-userinfo.html");

        String userEmail = "";
        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            userEmail = oAuth2User.getAttribute("email");
        } else {
            userEmail = authentication.getName();
        }

        userService.changePassword(newPass, userEmail);
        mav.addObject("message", "Cập nhật mật khẩu thành công.");

        if (authentication != null) {
            return mav;
        }
        return new ModelAndView("web/signin.html");
    }
}
