package com.handicrafts.controller.admin;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/info")
    public String getAdminInfo(Model model, HttpSession session) {
        // Lấy thông tin user từ session
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            // Nếu không có user trong session, chuyển hướng đến trang đăng nhập
            return "redirect:/login";
        }

        // Lấy thông tin chi tiết của user từ repository
        UserEntity adminInfo = userRepository.findUserById(user.getId());

        // Đưa thông tin admin vào model để hiển thị trên view
        model.addAttribute("admin", adminInfo);

        // Trả về tên view (Thymeleaf template hoặc JSP)
        return "admin-info";
    }

    // Các phương thức khác nếu cần, ví dụ như xử lý POST request để cập nhật thông tin
    /*
    @PostMapping("/info")
    public String updateAdminInfo(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
        // Xử lý cập nhật thông tin admin
        int result = userRepository.updateUser(userDTO);

        if (result > 0) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cập nhật thông tin thất bại");
        }

        return "redirect:/admin/info";
    }
    */
}
