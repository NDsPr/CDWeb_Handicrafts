package com.handicrafts.controller.admin.account;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.NumberValidateUtil;
import com.handicrafts.util.ValidateParamUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
@RequestMapping("/admin/account-management/editing")
public class AccountEditingController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ILogService<UserDTO> logService;

    private final ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    @GetMapping

    public String showEditAccountForm(@RequestParam("id") Integer id, Model model) {
        UserEntity user = userRepository.findUserById(id);
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            model.addAttribute("user", userDTO);
        }
        return "editing-account";
    }


    @PostMapping
    public String updateAccount(@ModelAttribute("user") UserDTO userDTO,
                                @RequestParam("addressLine") String addressLine,
                                @RequestParam("addressWard") String addressWard,
                                @RequestParam("addressDistrict") String addressDistrict,
                                @RequestParam("addressProvince") String addressProvince,
                                HttpServletRequest request,
                                Model model) {
        boolean isValid = true;

        String[] inputsForm = {
                userDTO.getFullName(),
                userDTO.getUsername(),
                addressLine, addressWard, addressDistrict, addressProvince
        };

        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);
        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        String msg;
        UserEntity prevUser = userRepository.findUserById(userDTO.getId());

        if (isValid && prevUser != null) {
            try {
                // Cập nhật phần tên (nếu fullName tồn tại)
                String firstName = "";
                String lastName = "";
                if (userDTO.getFullName() != null) {
                    String[] parts = userDTO.getFullName().trim().split(" ", 2);
                    firstName = parts[0];
                    lastName = (parts.length == 2) ? parts[1] : "";
                }

                // Cập nhật thông tin cơ bản
                userRepository.updateAccount(
                        firstName,
                        lastName,
                        addressLine,
                        addressWard,
                        addressDistrict,
                        addressProvince,
                        userDTO.getId()
                );

                // Cập nhật role và status (giả sử roles là list và bạn lấy phần tử đầu tiên làm roleId)
                Integer roleId = NumberValidateUtil.parseIntSafe(userDTO.getRoles().get(0), 0);
                userRepository.updateAccountForAdmin(
                        roleId,
                        Boolean.TRUE.equals(userDTO.getStatus()) ? 1 : 0,
                        userDTO.getId()
                );

                logService.log(request, "admin-update-account", LogState.SUCCESS, LogLevel.INFO, null, userDTO);
                msg = "success";

            } catch (Exception e) {
                logService.log(request, "admin-update-account", LogState.FAIL, LogLevel.ALERT, null, null);
                msg = "error";
            }

        } else {
            model.addAttribute("errors", errors);
            msg = "error";
        }

        // Load lại user để hiển thị
        UserEntity displayUser = userRepository.findUserById(userDTO.getId());
        if (displayUser != null) {
            UserDTO displayUserDTO = new UserDTO();
            BeanUtils.copyProperties(displayUser, displayUserDTO);
            model.addAttribute("user", displayUserDTO);
        }

        model.addAttribute("msg", msg);
        return "editing-account";
    }

}