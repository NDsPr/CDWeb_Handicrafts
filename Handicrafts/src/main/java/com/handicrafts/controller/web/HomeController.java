package com.handicrafts.controller.web;

import com.handicrafts.dto.BlogDTO;
import com.handicrafts.dto.CategoryDTO;
import com.handicrafts.dto.Content1DTO;
import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.repository.BlogRepository;
import com.handicrafts.repository.CategoryRepository;
import com.handicrafts.repository.CustomizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CustomizeRepository customizeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BlogRepository blogRepository;

    @GetMapping("/home")
    public String home(Model model) {
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();

        String[] prIcon1List = splitByComma(customizeInfo.getPrIcon1());
        String[] prContentTitle1List = splitByTilde(customizeInfo.getPrContentTitle1());
        String[] prContentDes1List = splitByTilde(customizeInfo.getPrContentDes1());
        List<Content1DTO> listContent1 = putContentIntoList(prIcon1List, prContentTitle1List, prContentDes1List);

        String[] prContent2List = splitByTilde(customizeInfo.getPrContent2());

        List<CategoryDTO> listCategories = categoryRepository.findAllCategories();
        List<BlogDTO> listThreeBlogs = blogRepository.findThreeBlogs();

        model.addAttribute("customizeInfo", customizeInfo);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("listBlogs", listThreeBlogs);
        model.addAttribute("listContent1", listContent1);
        model.addAttribute("prIcon1List", prIcon1List);
        model.addAttribute("prContent2List", prContent2List);

        return "web/client-home";
    }

    private String[] splitByTilde(String input) {
        if (input == null) return new String[0];
        return input.trim().split("~\\s*");
    }

    private String[] splitByComma(String input) {
        if (input == null) return new String[0];
        return input.trim().split(",\\s*");
    }

    private List<Content1DTO> putContentIntoList(String[] iconArray, String[] contentTitleArray, String[] contentDesArray) {
        List<Content1DTO> content1List = new ArrayList<>();
        int len = Math.min(iconArray.length, Math.min(contentTitleArray.length, contentDesArray.length));
        for (int i = 0; i < len; i++) {
            Content1DTO dto = new Content1DTO();
            dto.setPrIcon1(iconArray[i]);
            dto.setPrContentTitle1(contentTitleArray[i]);
            dto.setPrContentDes1(contentDesArray[i]);
            content1List.add(dto);
        }
        return content1List;
    }
}