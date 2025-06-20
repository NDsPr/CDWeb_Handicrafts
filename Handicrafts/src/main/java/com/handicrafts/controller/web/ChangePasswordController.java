package com.handicrafts.controller.web;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.security.service.LinkVerifyService;
import com.handicrafts.service.ILogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ChangePasswordController {

    @Autowired
    private LinkVerifyService linkVerifyService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ILogService<UserEntity> logService;

    @Autowired
    private Environment env;  // Thêm Environment để truy cập các thuộc tính

    @GetMapping("/user-change-password")
    public String showChangePasswordForm() {
        return "web/change-password";
    }

    @PostMapping("/user-change-password")
    public String processChangePassword(
            @RequestParam("email") String email,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("retypePassword") String retypePassword,
            Model model,
            HttpServletRequest request) {

        String newPasswordInputErr = null;
        String retypePasswordInputErr = null;
        String newPasswordSpaceErr = null;
        String retypePasswordSpaceErr = null;

        boolean isValid = true;

        if (!linkVerifyService.isBlankInput(newPassword) && !linkVerifyService.isBlankInput(retypePassword)) {
            if (!linkVerifyService.containsSpace(newPassword) && !linkVerifyService.containsSpace(retypePassword)) {
                if (linkVerifyService.isLengthEnough(newPassword)) {
                    if (!newPassword.equals(retypePassword)) {
                        newPasswordInputErr = "Mật khẩu và Nhập lại mật khẩu không đúng!";
                        retypePasswordInputErr = "Mật khẩu và Nhập lại mật khẩu không đúng!";
                        model.addAttribute("newpw-error", newPasswordInputErr);
                        model.addAttribute("retypePasswordInputErr", retypePasswordInputErr);
                        isValid = false;
                    }
                } else {
                    newPasswordInputErr = "Mật khẩu chứa ít nhất 6 ký tự!";
                    model.addAttribute("newpw-error", newPasswordInputErr);
                    isValid = false;
                }
            } else {
                if (linkVerifyService.containsSpace(newPassword)) {
                    newPasswordSpaceErr = "Mật khẩu không được chứa ô trống!";
                    model.addAttribute("newpw-error", newPasswordSpaceErr);
                }
                if (linkVerifyService.containsSpace(retypePassword)) {
                    retypePasswordSpaceErr = "Mật khẩu không được chứa ô trống!";
                    model.addAttribute("retype-error", retypePasswordSpaceErr);
                }
                isValid = false;
            }
        } else {
            if (linkVerifyService.isBlankInput(newPassword)) {
                newPasswordInputErr = "Mật khẩu không được để trống!";
                model.addAttribute("newpw-error", newPasswordInputErr);
            }
            if (linkVerifyService.isBlankInput(retypePassword)) {
                retypePasswordInputErr = "Mật khẩu không được để trống!";
                model.addAttribute("retype-error", retypePasswordInputErr);
            }
            isValid = false;
        }

        UserEntity prevUser = userRepository.findByEmail(email);

        if (isValid) {
            int affectedRows = linkVerifyService.saveRenewPasswordByEmail(email, newPassword);
            UserEntity currentUser = userRepository.findByEmail(email);

            if (affectedRows <= 0) {
                // Sử dụng Environment để lấy thông báo log
                String logMessage = env.getProperty("log.user-change-password.fail");
                logService.log(request, "user-change-password", LogState.FAIL.toString(), LogLevel.ALERT, prevUser, currentUser);
                model.addAttribute("error", "e");
                return "web/forget";
            } else {
                // Sử dụng Environment để lấy thông báo log
                String logMessage = env.getProperty("log.user-change-password.success");
                logService.log(request, "user-change-password", LogState.SUCCESS.toString(), LogLevel.WARNING, prevUser, currentUser);
                return "web/thankyou";
            }
        } else {
            UserEntity currentUser = userRepository.findByEmail(email);
            // Sử dụng Environment để lấy thông báo log
            String logMessage = env.getProperty("log.user-change-password.fail");
            logService.log(request, "user-change-password", LogState.FAIL.toString(), LogLevel.ALERT, prevUser, currentUser);
            return "web/change-password";
        }
    }
}
