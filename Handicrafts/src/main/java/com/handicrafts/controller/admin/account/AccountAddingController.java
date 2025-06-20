package com.handicrafts.controller.admin.account;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.EncryptPasswordUtil;
import com.handicrafts.util.ValidateParamUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/account-management")
@RequiredArgsConstructor
public class AccountAddingController {

    private final UserRepository userRepository;
    private final ILogService<UserDTO> logService;
    private final Environment environment;

    @GetMapping("/adding")
    public String showAddForm() {
        return "adding-account"; // Tên view (JSP/Thymeleaf)
    }

    @PostMapping("/adding")
    @Transactional
    public String handleAddAccount(@ModelAttribute("user") UserDTO userDTO,
                                   HttpServletRequest request,
                                   Model model) {
        boolean isValid = true;

        // Kiểm tra các trường bắt buộc
        String[] requiredInputs = {
                userDTO.getEmail(), userDTO.getPassword(),
                userDTO.getFullName(), userDTO.getUsername(),
                String.valueOf(userDTO.getRoles()), String.valueOf(userDTO.getStatus())
        };

        List<String> errors = ValidateParamUtil.checkEmptyParam(requiredInputs);
        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        String msg;
        if (isValid) {
            try {
                // Chuyển từ DTO sang Entity
                UserEntity entity = new UserEntity();
                BeanUtils.copyProperties(userDTO, entity);

                // Mã hóa mật khẩu
                entity.setPassword(EncryptPasswordUtil.encryptPassword(userDTO.getPassword()));

                // Chuyển kiểu Boolean -> Integer cho status (1 = active, 0 = inactive)
                entity.setStatus(Boolean.TRUE.equals(userDTO.getStatus()) ? 1 : 0);

                // Gán tên đầy đủ nếu chưa có (nếu dùng fullName riêng)
                if (userDTO.getFullName() != null) {
                    String[] parts = userDTO.getFullName().trim().split(" ", 2);
                    if (parts.length == 2) {
                        entity.setFirstName(parts[0]);
                        entity.setLastName(parts[1]);
                    } else {
                        entity.setFirstName(userDTO.getFullName());
                        entity.setLastName(""); // hoặc null tùy bạn
                    }
                }

                // Lưu vào CSDL
                userRepository.save(entity);

                // Ghi log thành công
                String successLogKey = environment.getProperty("log-content.admin-create-account-success");
                logService.log(request, successLogKey, LogState.SUCCESS, LogLevel.INFO, null, userDTO);
                msg = environment.getProperty("ui-message.account-add-success", "success");
            } catch (Exception e) {
                // Ghi log lỗi
                String failLogKey = environment.getProperty("log-content.admin-create-account-fail");
                logService.log(request, failLogKey, LogState.FAIL, LogLevel.ALERT, null, null);
                msg = environment.getProperty("ui-message.account-add-error", "error");
            }
        } else {
            model.addAttribute("errors", errors);
            msg = environment.getProperty("ui-message.validation-error", "error");
        }

        model.addAttribute("msg", msg);
        return "adding-account";
    }
}
