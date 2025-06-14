package com.handicrafts.controller.web;


import com.handicrafts.dto.BlogDTO;
import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.repository.BlogRepository;
import com.handicrafts.repository.CustomizeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BlogController {

    private final CustomizeRepository customizeRepository;
    private final BlogRepository blogRepository;

    public BlogController(CustomizeRepository customizeRepository, BlogRepository blogRepository) {
        this.customizeRepository = customizeRepository;
        this.blogRepository = blogRepository;
    }

    @GetMapping("/blog")
    public String showBlogPage(Model model) {
        List<BlogDTO> listBlogs = blogRepository.findAllBlogs();
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();

        model.addAttribute("customizeInfo", customizeInfo);
        model.addAttribute("listBlogs", listBlogs);

        return "blog";
    }
}
