package com.handicrafts.controller.web;


import com.handicrafts.api.output.HandicraftOutput;
import com.handicrafts.dto.HandicraftDTO;
import com.handicrafts.service.IHandicraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class HandicraftController {

    @Autowired
    private IHandicraftService handicraftService;

    @GetMapping("/danh-sach-san-pham")
    public HandicraftOutput findAll(
            @RequestParam(name = "category", required = false, defaultValue = "null") String category,
            @RequestParam(name = "category_type", required = false, defaultValue = "null") String categoryType,
            @RequestParam(name = "material", required = false, defaultValue = "null") String material,
            @RequestParam(name = "title", required = false, defaultValue = "null") String title,
            @RequestParam(name = "popular", required = false, defaultValue = "false") boolean isPopular,
            @RequestParam(name = "new_arrival", required = false, defaultValue = "false") boolean isNewArrival,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String sortOrder,
            @RequestParam(name = "from", required = false) Integer from,
            @RequestParam(name = "to", required = false) Integer to,
            @RequestParam(name = "discount", required = false, defaultValue = "0") Double discount
    ) {
        final int SIZE = 9;
        HandicraftOutput output = new HandicraftOutput();

        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(0, SIZE, sort); // Không phân trang thực, chỉ lấy 1 trang 9 SP

        if (!title.equalsIgnoreCase("null")) {
            output.setResult(handicraftService.findAllByTitleContaining(title, pageable));
            output.setTotalPage((int) Math.ceil((double) handicraftService.countAllByTitleContaining(title) / SIZE));

        } else if (!category.equalsIgnoreCase("null")) {
            output.setResult(handicraftService.findByCategoryCode(category, pageable));
            output.setTotalPage((int) Math.ceil((double) handicraftService.countByCategory(category) / SIZE));

        } else if (!categoryType.equalsIgnoreCase("null")) {
            output.setResult(handicraftService.findByCategoryTypeCode(categoryType, pageable));
            output.setTotalPage((int) Math.ceil((double) handicraftService.countByCategoryType(categoryType) / SIZE));

        } else if (!material.equalsIgnoreCase("null")) {
            output.setResult(handicraftService.findByMaterial(material, pageable));
            output.setTotalPage((int) Math.ceil((double) handicraftService.countByMaterial(material) / SIZE));

        } else if (from != null && to != null) {
            output.setResult(handicraftService.findByPriceBetween(from, to, pageable));
            output.setTotalPage((int) Math.ceil((double) handicraftService.countByPriceBetween(from, to) / SIZE));

        } else if (from != null) {
            output.setResult(handicraftService.findByPriceGreaterThan(from, pageable));
            output.setTotalPage((int) Math.ceil((double) handicraftService.countByPriceGreaterThan(from) / SIZE));

        } else if (isPopular) {
            output.setResult(handicraftService.findAllPopular(true, pageable));
            output.setTotalPage((int) Math.ceil((double) handicraftService.countPopular(true) / SIZE));

        } else if (isNewArrival) {
            output.setResult(handicraftService.findAllNewArrival(true, pageable));
            output.setTotalPage((int) Math.ceil((double) handicraftService.countNewArrival(true) / SIZE));

        } else if (discount != 0) {
            output.setResult(handicraftService.findByDiscountGreaterThan(discount, pageable));
            output.setTotalPage((int) Math.ceil((double) handicraftService.countByDiscountGreaterThan(discount) / SIZE));

        } else {
            output.setResult(handicraftService.findAll(pageable));
            output.setTotalPage((int) Math.ceil((double) handicraftService.countAllActive() / SIZE));
        }

        return output;
    }

    @GetMapping("/san-pham")
    public ModelAndView viewShopPage() {
        return new ModelAndView("web/shop.html");
    }

    @GetMapping("/san-pham-noi-bat")
    public List<HandicraftDTO> findPopularItems() {
        return handicraftService.findPopularItems(true);
    }

    @GetMapping("/san-pham-moi")
    public List<HandicraftDTO> findNewArrivals() {
        return handicraftService.findNewArrivals(true);
    }

    @GetMapping("/autocomplete")
    public List<String> autocompleteTitle(@RequestParam("title") String title) {
        return handicraftService.autocompleteTitle(title);
    }

    @GetMapping("/chi-tiet")
    public ModelAndView viewDetail(@RequestParam("id") Integer id) {
        ModelAndView mav = new ModelAndView("web/detail.html");
        HandicraftDTO item = handicraftService.findById(id);
        mav.addObject("list", handicraftService.findSimilarItems(item.getCategory().getCategoryID(), 50));
        return mav;
    }

    @GetMapping("/getDetail")
    public HandicraftDTO getDetail(@RequestParam("id") int id) {
        return handicraftService.findById(id);
    }
}

