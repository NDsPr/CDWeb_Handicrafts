package com.handicrafts.controller.web;

import com.handicrafts.dto.BlogDTO;
import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.repository.BlogRepository;
import com.handicrafts.repository.CustomizeRepository;
import org.springframework.DTOs.factory.annotation.Autowired;
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
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
        BlogDTO blogDetail = blogRepository.findBlogById(blogId);

        model.addAttribute("customizeInfo", customizeInfo);
        model.addAttribute("blogDetail", blogDetail);

        return "blog-detail"; // Trả về tên view (blog-detail.jsp hoặc blog-detail.html)
    }
}
