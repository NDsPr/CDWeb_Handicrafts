package com.handicrafts.controller.admin.blog;

import com.handicrafts.dto.BlogDTO;
import com.handicrafts.repository.BlogRepository;
import com.handicrafts.util.ValidateParamUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/blog-management")
@RequiredArgsConstructor
public class BlogAddingController {

    private final BlogRepository blogRepository;

    // Hiển thị form thêm blog
    @GetMapping("/adding")
    public String showAddBlogForm(Model model) {
        model.addAttribute("blog", new BlogDTO());
        return "adding-blog"; // view hiển thị form
    }

    // Xử lý form khi submit
    @PostMapping("/adding")
    public String handleAddBlog(@ModelAttribute("blog") BlogDTO blogDTO, Model model) {
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
            // Thiết lập thông tin mặc định nếu chưa có
            if (blogDTO.getCreatedDate() == null) {
                blogDTO.setCreatedDate(new Timestamp(new Date().getTime()));
            }

            if (blogDTO.getCreatedBy() == null || blogDTO.getCreatedBy().isEmpty()) {
                blogDTO.setCreatedBy("admin"); // hoặc lấy từ session đăng nhập
            }

            blogRepository.createBlog(blogDTO);
            return "redirect:/admin/blog-management/adding?success=success";
        }

        return "adding-blog"; // Nếu không hợp lệ, quay lại form
    }
}
