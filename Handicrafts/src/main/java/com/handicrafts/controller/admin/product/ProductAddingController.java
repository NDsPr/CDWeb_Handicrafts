package com.handicrafts.controller.admin.product;

import com.handicrafts.dto.ProductDTO;
import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.repository.ProductRepository;
import com.handicrafts.repository.ImageRepository;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.NumberValidateUtil;
import com.handicrafts.util.ValidateParamUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/product/adding")
@RequiredArgsConstructor
public class ProductAddingController {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final ILogService<ProductDTO> logService;
    private final Environment environment;

    @GetMapping("${product.adding.path}")
    public String showForm() {
        return environment.getProperty("product.adding.view", "admin/product/add");
    }

    @PostMapping("${product.adding.path}")
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

        boolean isValid = validateProductInputs(name, description, categoryTypeId,
                originalPrice, discountPrice, discountPercent, quantity,
                size, status, imgUrls, model);

        if (!isValid) {
            logService.log(req, environment.getProperty("product.log.action", "ADD_PRODUCT"),
                    LogState.FAIL, LogLevel.ALERT, null, null);
            model.addAttribute(environment.getProperty("model.attribute.message", "message"),
                    environment.getProperty("message.error", "Thêm sản phẩm thất bại"));
            return environment.getProperty("product.adding.view", "admin/product/add");
        }

        ProductDTO product = createProductFromInputs(name, description, categoryTypeId,
                originalPrice, discountPrice, discountPercent, quantity,
                size, status, otherSpec, keyword);

        int id = productRepository.createProduct(product);

        if (id <= 0) {
            logService.log(req, environment.getProperty("product.log.action", "ADD_PRODUCT"),
                    LogState.FAIL, LogLevel.ALERT, null, null);
            model.addAttribute(environment.getProperty("model.attribute.message", "message"),
                    environment.getProperty("message.error", "Thêm sản phẩm thất bại"));
        } else {
            ProductDTO currentProduct = productRepository.findProductById(id);
            logService.log(req, environment.getProperty("product.log.action", "ADD_PRODUCT"),
                    LogState.SUCCESS, LogLevel.WARNING, null, currentProduct);
            saveProductImages(id, imgUrls);
            model.addAttribute(environment.getProperty("model.attribute.message", "message"),
                    environment.getProperty("message.success", "Thêm sản phẩm thành công"));
        }

        return environment.getProperty("product.adding.view", "admin/product/add");
    }

    private boolean validateProductInputs(String name, String description, String categoryTypeId,
                                          String originalPrice, String discountPrice, String discountPercent,
                                          String quantity, String size, String status, String imgUrls,
                                          Model model) {
        boolean isValid = true;
        String[] inputs = {name, description, categoryTypeId, originalPrice, discountPrice,
                discountPercent, quantity, size, status, imgUrls};
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputs);

        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        if (!NumberValidateUtil.isNumeric(originalPrice) || !NumberValidateUtil.isValidPrice(originalPrice)) {
            isValid = false;
            model.addAttribute("oPrErr", environment.getProperty("error.original.price", "Giá gốc không hợp lệ"));
        }

        if (!NumberValidateUtil.isNumeric(discountPrice) || !NumberValidateUtil.isValidPrice(discountPrice)) {
            isValid = false;
            model.addAttribute("dPrErr", environment.getProperty("error.discount.price", "Giá khuyến mãi không hợp lệ"));
        }

        if (!NumberValidateUtil.isNumeric(discountPercent) || !NumberValidateUtil.isValidPercent(discountPercent)) {
            isValid = false;
            model.addAttribute("dPeErr", environment.getProperty("error.discount.percent", "Phần trăm giảm giá không hợp lệ"));
        }

        if (!NumberValidateUtil.isNumeric(quantity) || !NumberValidateUtil.isValidQuantity(quantity)) {
            isValid = false;
            model.addAttribute("qErr", environment.getProperty("error.quantity", "Số lượng không hợp lệ"));
        }

        if (productRepository.isExistProductName(name)) {
            isValid = false;
            model.addAttribute("nameErr", environment.getProperty("error.name.exists", "Tên sản phẩm đã tồn tại"));
        }

        if (!isValid) {
            model.addAttribute("errors", errors);
        }

        return isValid;
    }

    private ProductDTO createProductFromInputs(String name, String description, String categoryTypeId,
                                               String originalPrice, String discountPrice, String discountPercent,
                                               String quantity, String size, String status,
                                               String otherSpec, String keyword) {
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
        return product;
    }

    private void saveProductImages(int productId, String imgUrls) {
        for (String url : imgUrls.replaceAll("\\s+", "").split(",")) {
            ProductImageDTO image = new ProductImageDTO();
            image.setName(UUID.randomUUID().toString().replace("-", ""));
            image.setProductId(productId);
            image.setLink(url);
            imageRepository.insertProductImage(image);
        }
    }
}
