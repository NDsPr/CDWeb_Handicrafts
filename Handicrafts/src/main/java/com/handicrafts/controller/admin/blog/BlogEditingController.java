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
import java.util.List;

@Controller
@RequestMapping("/admin/blog-management")
public class BlogEditingController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ILogService<BlogDTO> logService;

    @Autowired
    private Environment environment;

    // Hiển thị form chỉnh sửa blog
    @GetMapping("/editing")
    public String showEditBlogForm(@RequestParam("id") int id, Model model) {
        BlogDTO blog = blogRepository.findBlogById(id);
        if (blog == null) {
            String errorMessage = environment.getProperty("ui-message.blog-not-found", "Blog not found");
            model.addAttribute("msg", errorMessage);
            return "redirect:/admin/blog-management";
        }
        model.addAttribute("blog", blog);
        return "editing-blog"; // Tên view Thymeleaf hoặc JSP
    }

    // Xử lý submit form chỉnh sửa blog
    @PostMapping("/editing")
    public String handleEditBlog(
            @ModelAttribute("blog") BlogDTO blogDTO,
            @RequestParam("id") int id,
            HttpServletRequest request,
            Model model
    ) {
        boolean isValid = true;
        String msg;

        // Kiểm tra các trường không được bỏ trống
        String[] inputsForm = {
                blogDTO.getTitle(), blogDTO.getAuthor(), blogDTO.getDescription(),
                blogDTO.getContent(), String.valueOf(blogDTO.getCategoryId()),
                String.valueOf(blogDTO.getStatus()), blogDTO.getProfilePic(),
                String.valueOf(blogDTO.getCreatedDate()), blogDTO.getCreatedBy(),
                String.valueOf(blogDTO.getModifiedDate()), blogDTO.getModifiedBy()
        };

        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        model.addAttribute("errors", errors);

        BlogDTO prevBlog = blogRepository.findBlogById(id);

        if (isValid) {
            try {
                blogDTO.setId(id); // Đảm bảo có id
                int affectedRow = blogRepository.updateBlog(blogDTO);
                BlogDTO currentBlog = blogRepository.findBlogById(id);

                if (affectedRow <= 0) {
                    // Không có hàng nào được cập nhật
                    String failLogKey = environment.getProperty("log-content.admin-update-blog-fail", "admin-update-blog");
                    logService.log(request, failLogKey, LogState.FAIL, LogLevel.ALERT, prevBlog, currentBlog);
                    msg = environment.getProperty("ui-message.blog-update-error", "Error updating blog");
                } else {
                    // Cập nhật thành công
                    String successLogKey = environment.getProperty("log-content.admin-update-blog-success", "admin-update-blog");
                    logService.log(request, successLogKey, LogState.SUCCESS, LogLevel.WARNING, prevBlog, currentBlog);
                    msg = environment.getProperty("ui-message.blog-update-success", "Blog updated successfully");
                }
            } catch (Exception e) {
                // Xử lý ngoại lệ
                String failLogKey = environment.getProperty("log-content.admin-update-blog-fail", "admin-update-blog");
                logService.log(request, failLogKey, LogState.FAIL, LogLevel.ALERT, prevBlog, null);
                msg = environment.getProperty("ui-message.blog-update-error", "Error updating blog");
            }
        } else {
            // Form không hợp lệ
            String validationFailLogKey = environment.getProperty("log-content.admin-update-blog-validation-fail", "admin-update-blog-validation");
            logService.log(request, validationFailLogKey, LogState.FAIL, LogLevel.ALERT, prevBlog, null);
            msg = environment.getProperty("ui-message.blog-validation-error", "Please fill all required fields");
        }

        BlogDTO displayBlog = blogRepository.findBlogById(id);
        model.addAttribute("msg", msg);
        model.addAttribute("blog", displayBlog);

        return "editing-blog";
    }
}
