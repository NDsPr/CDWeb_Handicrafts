package com.handicrafts.controller.web;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.security.service.LinkVerifyService;
import com.handicrafts.security.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class ChangePasswordController {

    @Autowired
    private LinkVerifyService linkVerifyService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LogService<UserDTO> logService;

    private ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    /**
     * Chuyển đổi từ UserEntity sang UserDTO
     */
    private UserDTO convertToUserDTO(UserEntity entity) {
        if (entity == null) return null;

        UserDTO dto = new UserDTO();
        // Thiết lập các thuộc tính từ entity sang dto
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setUsername(entity.getUsername());
        // Thêm các thuộc tính khác tùy theo cấu trúc của UserDTO

        return dto;
    }

    @GetMapping("/user-change-password")
    public String showChangePasswordPage() {
        return "change-password";
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

        // Kiểm tra mật khẩu không bị trống
        if (!linkVerifyService.isBlankInput(newPassword) && !linkVerifyService.isBlankInput(retypePassword)) {
            // Kiểm tra mật khẩu không chứa khoảng trắng
            if (!linkVerifyService.containsSpace(newPassword) && !linkVerifyService.containsSpace(retypePassword)) {
                // Kiểm tra độ dài mật khẩu
                if (linkVerifyService.isLengthEnough(newPassword)) {
                    // Kiểm tra mật khẩu và nhập lại mật khẩu có khớp nhau không
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

        // Lấy thông tin người dùng trước khi thay đổi
        Optional<UserEntity> prevUserOptional = userRepo.findByEmail(email);
        UserDTO prevUser = prevUserOptional.map(this::convertToUserDTO).orElse(null);

        if (isValid) {
            // Nếu dữ liệu hợp lệ, thực hiện đổi mật khẩu
            int affectedRows = linkVerifyService.saveRenewPasswordByEmail(email, newPassword);

            // Lấy thông tin người dùng sau khi thay đổi
            Optional<UserEntity> currentUserOptional = userRepo.findByEmail(email);
            UserDTO currentUser = currentUserOptional.map(this::convertToUserDTO).orElse(null);

            if (affectedRows <= 0) {
                // Nếu không thành công
                logService.log(request, "user-change-password", LogState.FAIL, LogLevel.ALERT, prevUser, currentUser);
                model.addAttribute("error", "e");
                return "forget";
            } else {
                // Nếu thành công
                logService.log(request, "user-change-password", LogState.SUCCESS, LogLevel.WARNING, prevUser, currentUser);
                return "thankyou";
            }
        } else {
            // Nếu dữ liệu không hợp lệ
            Optional<UserEntity> currentUserOptional = userRepo.findByEmail(email);
            UserDTO currentUser = currentUserOptional.map(this::convertToUserDTO).orElse(null);

            logService.log(request, "user-change-password", LogState.FAIL, LogLevel.ALERT, prevUser, currentUser);

            // Giữ email để người dùng không phải nhập lại
            model.addAttribute("email", email);

            return "change-password";
        }
    }
}
