package com.handicrafts.controller.admin.blog;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.BlogDTO;
import com.handicrafts.repository.BlogRepository;
import com.handicrafts.util.ValidateParamUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@RequestMapping("/admin/blog-management")
@RequiredArgsConstructor
public class BlogEditingController {

    private final BlogRepository blogRepository;
    private final LogService<BlogDTO> logService;
    private final ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    // Hiển thị form chỉnh sửa blog
    @GetMapping("/editing")
    public String showEditBlogForm(@RequestParam("id") int id, Model model) {
        BlogDTO blog = blogRepository.findBlogById(id);
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
            blogDTO.setId(id); // Đảm bảo có id
            int affectedRow = blogRepository.updateBlog(blogDTO);
            BlogDTO currentBlog = blogRepository.findBlogById(id);

            if (affectedRow < 0) {
                logService.log(request, "admin-update-blog", LogState.FAIL, LogLevel.ALERT, prevBlog, currentBlog);
                msg = "error";
            } else if (affectedRow > 0) {
                logService.log(request, "admin-update-blog", LogState.SUCCESS, LogLevel.WARNING, prevBlog, currentBlog);
                msg = "success";
            } else {
                msg = "error";
            }

        } else {
            BlogDTO currentBlog = blogRepository.findBlogById(id);
            logService.log(request, "admin-update-blog", LogState.FAIL, LogLevel.ALERT, prevBlog, currentBlog);
            msg = "error";
        }

        BlogDTO displayBlog = blogRepository.findBlogById(id);
        model.addAttribute("msg", msg);
        model.addAttribute("blog", displayBlog);

        return "editing-blog";
    }
}
