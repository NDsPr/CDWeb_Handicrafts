package com.handicrafts.controller.web;

import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.repository.CustomizeRepository;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.security.service.LogService;
import com.handicrafts.util.BlankInputUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;

@Controller
@RequestMapping("/user-info")
public class UserInfoController {

    private final CustomizeRepository customizeRepository;
    private final UserRepository userRepository;
    private final LogService<UserDTO> logService;
    private final ResourceBundle logBundle;

    @Autowired
    public UserInfoController(
            CustomizeRepository customizeRepository,
            UserRepository userRepository,
            LogService<UserDTO> logService) {
        this.customizeRepository = customizeRepository;
        this.userRepository = userRepository;
        this.logService = logService;
        this.logBundle = ResourceBundle.getBundle("log-content");
    }

    @GetMapping
    public String getUserInfo(Model model, HttpSession session) {
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
        UserDTO userDTO = (UserDTO) session.getAttribute("user");

        model.addAttribute("userInfo", userDTO);
        model.addAttribute("customizeInfo", customizeInfo);
        return "client-userinfo";
    }

    @PostMapping
    public String updateUserInfo(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "addressLine", required = false) String addressLine,
            @RequestParam(value = "addressWard", required = false) String addressWard,
            @RequestParam(value = "addressDistrict", required = false) String addressDistrict,
            @RequestParam(value = "addressProvince", required = false) String addressProvince,
            HttpServletRequest request,
            HttpSession session,
            Model model) {

        UserDTO sessionUser = (UserDTO) session.getAttribute("user");

        // String thông báo lên view
        String msg = "";
        String error = "";

        // Kiểm tra các trường hợp bỏ trống
        if (BlankInputUtil.isBlank(firstName)) {
            error = "error";
            model.addAttribute("fnErr", "e");
        }
        if (BlankInputUtil.isBlank(lastName)) {
            error = "error";
            model.addAttribute("lnErr", "e");
        }
        if (BlankInputUtil.isBlank(addressLine)) {
            error = "error";
            model.addAttribute("alErr", "e");
        }
        if (BlankInputUtil.isBlank(addressWard)) {
            error = "error";
            model.addAttribute("awErr", "e");
        }
        if (BlankInputUtil.isBlank(addressDistrict)) {
            error = "error";
            model.addAttribute("adErr", "e");
        }
        if (BlankInputUtil.isBlank(addressProvince)) {
            error = "error";
            model.addAttribute("apErr", "e");
        }

        // Nếu không lỗi thì set thành công và lưu vào database
//        if (!error.equals("error")) {
//            // Set thuộc tính vào DTO
//            UserDTO userDTO = new UserDTO();
//            userDTO.setId(sessionUser.getId());
//
//            UserDTO prevUser = userRepository.findById(sessionUser.getId());
//
//            // Cập nhật thông tin người dùng
//            boolean updateSuccess = userRepository.updateUser(userDTO);
//            UserDTO currentUser = userRepository.findById(sessionUser.getId());
//
//            if (updateSuccess) {
//                logService.log(request, "user-change-info", LogState.SUCCESS, LogLevel.WARNING, prevUser, currentUser);
//                msg = "success";
//                // Cập nhật lại giá trị trên Session
//                session.setAttribute("user", userRepository.findById(sessionUser.getId()));
//            } else {
//                logService.log(request, "user-change-info", LogState.FAIL, LogLevel.WARNING, prevUser, currentUser);
//                msg = "fail";
//            }
//        }

        // Cuối cùng lấy thông tin từ db để hiển thị cho người dùng
//        UserDTO userDTO = userRepository.findById(sessionUser.getId());
//        model.addAttribute("userInfo", userDTO);
        model.addAttribute("msg", msg);
        return "client-userinfo";
    }
}
