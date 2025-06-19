package com.handicrafts.controller.web;

import com.handicrafts.dto.BlogDTO;
import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.repository.BlogRepository;
import com.handicrafts.repository.CustomizeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {

    private final CustomizeRepository customizeRepository;
    private final BlogRepository blogRepository;

    public BlogController(CustomizeRepository customizeRepository, BlogRepository blogRepository) {
        this.customizeRepository = customizeRepository;
        this.blogRepository = blogRepository;
    }

    @GetMapping("/blog")
    public String showBlogPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size,
            @RequestParam(name = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(name = "direction", defaultValue = "DESC") String direction,
            @RequestParam(name = "search", required = false) String searchTerm,
            Model model) {

        // Lấy thông tin tùy chỉnh chung
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
        model.addAttribute("customizeInfo", customizeInfo);

        // Xử lý phân trang và sắp xếp
        Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Lấy danh sách blog với phân trang
        int start = page * size;
        List<BlogDTO> blogs;
        int totalRecords;
        int totalPages;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            blogs = blogRepository.getBlogsDatatable(start, size, sort, direction, searchTerm);
            totalRecords = blogRepository.getRecordsFiltered(searchTerm);
        } else {
            blogs = blogRepository.getBlogsDatatable(start, size, sort, direction, null);
            totalRecords = blogRepository.getRecordsTotal();
        }

        totalPages = (int) Math.ceil((double) totalRecords / size);

        model.addAttribute("listBlogs", blogs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalRecords);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("sortField", sort);
        model.addAttribute("sortDirection", direction);

        // Thêm các thuộc tính phân trang
        model.addAttribute("reverseSortDirection", direction.equals("ASC") ? "DESC" : "ASC");

        return "blog";
    }

    @GetMapping("/blog/{id}")
    public String showBlogDetail(@PathVariable("id") int id, Model model) {
        // Lấy thông tin tùy chỉnh chung
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
        model.addAttribute("customizeInfo", customizeInfo);

        // Lấy chi tiết blog theo ID
        BlogDTO blog = blogRepository.findBlogById(id);

        if (blog == null) {
            return "redirect:/blog?error=Blog+not+found";
        }

        model.addAttribute("blog", blog);

        // Lấy các bài viết liên quan (ví dụ: cùng category)
        List<BlogDTO> relatedBlogs = blogRepository.getBlogsDatatable(0, 3, "createdDate", "DESC", null);
        model.addAttribute("relatedBlogs", relatedBlogs);

        return "blog-detail";
    }

    @GetMapping("/blog/category/{categoryId}")
    public String showBlogsByCategory(
            @PathVariable("categoryId") int categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size,
            Model model) {

        // Lấy thông tin tùy chỉnh chung
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
        model.addAttribute("customizeInfo", customizeInfo);

        // Giả sử bạn có phương thức để lấy blog theo categoryId
        // Cần bổ sung phương thức này vào BlogRepository
        // List<BlogDTO> blogsByCategory = blogRepository.findBlogsByCategoryId(categoryId, page, size);

        // Thay thế bằng phương thức hiện có
        List<BlogDTO> blogsByCategory = blogRepository.getBlogsDatatable(page * size, size, "createdDate", "DESC", null);
        model.addAttribute("listBlogs", blogsByCategory);
        model.addAttribute("categoryId", categoryId);

        // Thêm các thuộc tính phân trang
        // Cần bổ sung phương thức để đếm tổng số blog theo category
        int totalRecords = blogRepository.getRecordsTotal();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalRecords);

        return "blog-category";
    }
}
