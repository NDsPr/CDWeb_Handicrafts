package com.handicrafts.controller.admin.product;

import com.handicrafts.dto.ProductDTO;
import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.repository.ProductRepository;
import com.handicrafts.repository.ImageRepository;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.security.service.LogService;
import com.handicrafts.util.NumberValidateUtil;
import com.handicrafts.util.ValidateParamUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/product-management")
@RequiredArgsConstructor
public class ProductAddingController {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final LogService<ProductDTO> logService;

    @GetMapping("/adding")
    public String showForm() {
        return "adding-product";
    }

    @PostMapping("/adding")
    public String addProduct(HttpServletRequest req,
                             @RequestParam("name") String name,
                             @RequestParam("description") String description,
                             @RequestParam("categoryTypeId") String categoryTypeId,
                             @RequestParam("originalPrice") String originalPrice,
                             @RequestParam("discountPrice") String discountPrice,
                             @RequestParam("discountPercent") String discountPercent,
                             @RequestParam("quantity") String quantity,
                             @RequestParam("size") String size,
                             @RequestParam(value = "otherSpec", required = false) String otherSpec,
                             @RequestParam("status") String status,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam("imgUrls") String imgUrls,
                             Model model) {

        boolean isValid = true;
        String[] inputs = {name, description, categoryTypeId, originalPrice, discountPrice, discountPercent, quantity, size, status, imgUrls};
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputs);

        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        if (!NumberValidateUtil.isNumeric(originalPrice) || !NumberValidateUtil.isValidPrice(originalPrice)) {
            isValid = false;
            model.addAttribute("oPrErr", "e");
        }

        if (!NumberValidateUtil.isNumeric(discountPrice) || !NumberValidateUtil.isValidPrice(discountPrice)) {
            isValid = false;
            model.addAttribute("dPrErr", "e");
        }

        if (!NumberValidateUtil.isNumeric(discountPercent) || !NumberValidateUtil.isValidPercent(discountPercent)) {
            isValid = false;
            model.addAttribute("dPeErr", "e");
        }

        if (!NumberValidateUtil.isNumeric(quantity) || !NumberValidateUtil.isValidQuantity(quantity)) {
            isValid = false;
            model.addAttribute("qErr", "e");
        }

        if (productRepository.isExistProductName(name)) {
            isValid = false;
            model.addAttribute("nameErr", "e");
        }

        if (!isValid) {
            model.addAttribute("errors", errors);
            logService.log(req, "admin-create-product", LogState.FAIL, LogLevel.ALERT, null, null);
            model.addAttribute("msg", "error");
            return "adding-product";
        }

        ProductDTO product = new ProductDTO();
        product.setName(name);
        product.setDescription(description);
        product.setCategoryTypeId(NumberValidateUtil.toInt(categoryTypeId));
        product.setOriginalPrice(NumberValidateUtil.toDouble(originalPrice));
        product.setDiscountPrice(NumberValidateUtil.toDouble(discountPrice));
        product.setDiscountPercent(NumberValidateUtil.toDouble(discountPercent));
        product.setQuantity(NumberValidateUtil.toInt(quantity));
        product.setSize(size);
        product.setStatus(NumberValidateUtil.toInt(status));
        product.setOtherSpec(otherSpec != null ? otherSpec : "");
        product.setKeyword(keyword != null ? keyword : "");
        product.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        int id = productRepository.createProduct(product);

        if (id <= 0) {
            logService.log(req, "admin-create-product", LogState.FAIL, LogLevel.ALERT, null, null);
            model.addAttribute("msg", "error");
        } else {
            ProductDTO currentProduct = productRepository.findProductById(id);
            logService.log(req, "admin-create-product", LogState.SUCCESS, LogLevel.WARNING, null, currentProduct);
            for (String url : imgUrls.replaceAll("\\s+", "").split(",")) {
                ProductImageDTO image = new ProductImageDTO();
                image.setName(UUID.randomUUID().toString().replace("-", ""));
                image.setProductId(id);
                image.setLink(url);
                imageRepository.insertProductImage(image);
            }
            model.addAttribute("msg", "success");
        }

        return "adding-product";
    }
}
