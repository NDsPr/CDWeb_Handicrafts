package com.handicrafts.controller.web;

import com.handicrafts.repository.CustomizeRepository;
import com.handicrafts.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlogDetailController {

    private final CustomizeRepository customizeRepository;
    private final BlogRepository blogRepository;

    @Autowired
    public BlogDetailController(CustomizeRepository customizeRepository, BlogRepository blogRepository) {
        this.customizeRepository = customizeRepository;
        this.blogRepository = blogRepository;
    }

    @GetMapping("/blog-detail")
    public String showBlogDetail(@RequestParam("id") int blogId, Model model) {
        // Lấy thông tin customize từ repository
        model.addAttribute("customizeInfo", customizeRepository.getCustomizeInfo());

        // Lấy chi tiết blog từ repository
        var blogDetail = blogRepository.findBlogById(blogId);

        // Kiểm tra xem blogDetail có null không
        if (blogDetail == null) {
            // Log để debug
            System.out.println("Không tìm thấy blog với ID: " + blogId);

            // Chuyển hướng về trang blog với thông báo lỗi
            return "redirect:/blog?error=blog-not-found";
        }

        // Log để xác nhận dữ liệu
        System.out.println("Blog Detail: " + blogDetail.getTitle() + ", ID: " + blogDetail.getId());

        model.addAttribute("blogDetail", blogDetail);
        return "web/blog-detail";
    }
}