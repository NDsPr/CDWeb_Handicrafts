package com.handicrafts.controller.admin.blog;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.BlogDTO;
import com.handicrafts.repository.BlogRepository;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.ValidateParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/blog-management")
public class BlogAddingController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private Environment environment;

    @Autowired
    private ILogService<BlogDTO> logService;

    // Hiển thị form thêm blog
    @GetMapping("/adding")
    public String showAddBlogForm(Model model, @RequestParam(value = "success", required = false) String success) {
        if (success != null) {
            String successMessage = environment.getProperty("ui-message.blog-add-success", "Blog has been added successfully");
            model.addAttribute("msg", successMessage);
        }
        model.addAttribute("blog", new BlogDTO());
        return "adding-blog"; // view hiển thị form
    }

    // Xử lý form khi submit
    @PostMapping("/adding")
    public String handleAddBlog(@ModelAttribute("blog") BlogDTO blogDTO,
                                Model model,
                                HttpServletRequest request) {
        boolean isValid = true;

        // Kiểm tra các trường bắt buộc
        String[] requiredInputs = {
                blogDTO.getTitle(),
                blogDTO.getDescription(),
                blogDTO.getContent(),
                String.valueOf(blogDTO.getCategoryId()),
                blogDTO.getAuthor(),
                String.valueOf(blogDTO.getStatus())
        };

        List<String> errors = ValidateParamUtil.checkEmptyParam(requiredInputs);
        for (String err : errors) {
            if (err != null) {
                isValid = false;
                break;
            }
        }

        model.addAttribute("errors", errors);

        if (isValid) {
            try {
                // Thiết lập thông tin mặc định nếu chưa có
                if (blogDTO.getCreatedDate() == null) {
                    blogDTO.setCreatedDate(new Timestamp(new Date().getTime()));
                }

                if (blogDTO.getCreatedBy() == null || blogDTO.getCreatedBy().isEmpty()) {
                    blogDTO.setCreatedBy("admin"); // hoặc lấy từ session đăng nhập
                }

                blogRepository.createBlog(blogDTO);

                // Ghi log thành công
                String successLogKey = environment.getProperty("log-content.admin-add-blog-success", "admin-add-blog");
                logService.log(request, successLogKey, LogState.SUCCESS, LogLevel.INFO, null, blogDTO);

                return "redirect:/admin/blog-management/adding?success=success";
            } catch (Exception e) {
                // Ghi log thất bại
                String failLogKey = environment.getProperty("log-content.admin-add-blog-fail", "admin-add-blog");
                logService.log(request, failLogKey, LogState.FAIL, LogLevel.ALERT, null, blogDTO);

                String errorMessage = environment.getProperty("ui-message.blog-add-error", "Error adding blog");
                model.addAttribute("msg", errorMessage);
            }
        } else {
            String validationErrorMessage = environment.getProperty("ui-message.blog-validation-error", "Please fill all required fields");
            model.addAttribute("msg", validationErrorMessage);
        }

        return "adding-blog"; // Nếu không hợp lệ hoặc có lỗi, quay lại form
    }
}
