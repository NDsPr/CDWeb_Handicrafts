package com.handicrafts.controller.admin.product;

import com.handicrafts.dto.ProductDTO;
import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.repository.ImageRepository;
import com.handicrafts.repository.ProductRepository;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.service.impl.LogServiceImp;
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
@RequestMapping("/admin/product/edit")
@RequiredArgsConstructor
public class ProductEditingController {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final LogServiceImp<ProductDTO> logService;
    private final Environment environment;

    @GetMapping("/admin/product/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        ProductDTO product = productRepository.findProductById(id);
        model.addAttribute(environment.getProperty("model.attribute.images", "images"), mergeUrls(id));
        model.addAttribute(environment.getProperty("model.attribute.product", "product"), product);
        return environment.getProperty("product.editing.view", "admin/product/edit");
    }

    @PostMapping("/admin/product/edit")
    public String updateProduct(HttpServletRequest req, Model model) {
        // Lấy tham số từ request
        ProductFormData formData = extractFormData(req);

        // Validate dữ liệu
        ValidationResult validationResult = validateProductInputs(formData, model);

        // Lấy sản phẩm trước khi cập nhật
        ProductDTO prevProduct = productRepository.findProductById(formData.getId());
        String msg;

        if (validationResult.isValid()) {
            // Tạo đối tượng ProductDTO từ dữ liệu form
            ProductDTO product = createProductFromFormData(formData);

            // Cập nhật sản phẩm
            int affectedRows = productRepository.updateProduct(product);
            ProductDTO currentProduct = productRepository.findProductById(formData.getId());

            if (affectedRows <= 0) {
                logService.log(req, environment.getProperty("product.editing.log.action", "EDIT_PRODUCT"),
                        LogState.FAIL, LogLevel.ALERT, prevProduct, currentProduct);
                msg = environment.getProperty("message.error", "Cập nhật sản phẩm thất bại");
            } else {
                logService.log(req, environment.getProperty("product.editing.log.action", "EDIT_PRODUCT"),
                        LogState.SUCCESS, LogLevel.WARNING, prevProduct, currentProduct);
                msg = environment.getProperty("message.success", "Cập nhật sản phẩm thành công");

                // Cập nhật hình ảnh sản phẩm
                updateProductImages(formData.getId(), formData.getImgUrls());
            }
        } else {
            ProductDTO currentProduct = productRepository.findProductById(formData.getId());
            model.addAttribute("errors", validationResult.getErrors());
            logService.log(req, environment.getProperty("product.editing.log.action", "EDIT_PRODUCT"),
                    LogState.FAIL, LogLevel.ALERT, prevProduct, currentProduct);
            msg = environment.getProperty("message.error", "Cập nhật sản phẩm thất bại");
        }

        model.addAttribute(environment.getProperty("model.attribute.images", "images"), mergeUrls(formData.getId()));
        model.addAttribute(environment.getProperty("model.attribute.message", "message"), msg);
        model.addAttribute(environment.getProperty("model.attribute.product", "product"),
                productRepository.findProductById(formData.getId()));
        return environment.getProperty("product.editing.view", "admin/product/edit");
    }

    private ProductFormData extractFormData(HttpServletRequest req) {
        ProductFormData formData = new ProductFormData();
        formData.setId(Integer.parseInt(req.getParameter("id")));
        formData.setName(req.getParameter("name"));
        formData.setDescription(req.getParameter("description"));
        formData.setCategoryTypeId(req.getParameter("categoryTypeId"));
        formData.setOriginalPrice(req.getParameter("originalPrice"));
        formData.setDiscountPrice(req.getParameter("discountPrice"));
        formData.setDiscountPercent(req.getParameter("discountPercent"));
        formData.setQuantity(req.getParameter("quantity"));
        formData.setSize(req.getParameter("size"));
        formData.setOtherSpec(req.getParameter("otherSpec"));
        formData.setStatus(req.getParameter("status"));
        formData.setKeyword(req.getParameter("keyword"));
        formData.setImgUrls(req.getParameter("imgUrls"));
        return formData;
    }

    private ValidationResult validateProductInputs(ProductFormData formData, Model model) {
        ValidationResult result = new ValidationResult();

        String[] inputsForm = {
                formData.getName(),
                formData.getDescription(),
                formData.getCategoryTypeId(),
                formData.getOriginalPrice(),
                formData.getDiscountPrice(),
                formData.getDiscountPercent(),
                formData.getQuantity(),
                formData.getSize(),
                formData.getStatus(),
                formData.getImgUrls()
        };

        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);
        boolean isValid = errors.stream().noneMatch(e -> e != null);

        if (!NumberValidateUtil.isValidPrice(formData.getOriginalPrice())) {
            isValid = false;
            model.addAttribute("oPrErr", environment.getProperty("error.original.price", "Giá gốc không hợp lệ"));
        }

        if (!NumberValidateUtil.isValidPrice(formData.getDiscountPrice())) {
            isValid = false;
            model.addAttribute("dPrErr", environment.getProperty("error.discount.price", "Giá khuyến mãi không hợp lệ"));
        }

        if (!NumberValidateUtil.isValidPercent(formData.getDiscountPercent())) {
            isValid = false;
            model.addAttribute("dPeErr", environment.getProperty("error.discount.percent", "Phần trăm giảm giá không hợp lệ"));
        }

        if (!NumberValidateUtil.isValidQuantity(formData.getQuantity())) {
            isValid = false;
            model.addAttribute("qErr", environment.getProperty("error.quantity", "Số lượng không hợp lệ"));
        }

        result.setValid(isValid);
        result.setErrors(errors);
        return result;
    }

    private ProductDTO createProductFromFormData(ProductFormData formData) {
        ProductDTO product = new ProductDTO();
        product.setId(formData.getId());
        product.setName(formData.getName());
        product.setDescription(formData.getDescription());
        product.setCategoryTypeId(NumberValidateUtil.toInt(formData.getCategoryTypeId()));
        product.setOriginalPrice(NumberValidateUtil.toDouble(formData.getOriginalPrice()));
        product.setDiscountPrice(NumberValidateUtil.toDouble(formData.getDiscountPrice()));
        product.setDiscountPercent(NumberValidateUtil.toDouble(formData.getDiscountPercent()));
        product.setQuantity(NumberValidateUtil.toInt(formData.getQuantity()));
        product.setSize(formData.getSize());
        product.setOtherSpec(formData.getOtherSpec() != null ? formData.getOtherSpec() : "");
        product.setStatus(NumberValidateUtil.toInt(formData.getStatus()));
        product.setKeyword(formData.getKeyword() != null ? formData.getKeyword() : "");
        product.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        return product;
    }

    private void updateProductImages(int productId, String imgUrls) {
        List<ProductImageDTO> productImages = imageRepository.findImagesByProductId(productId);
        for (String url : splitUrls(imgUrls)) {
            ProductImageDTO img = new ProductImageDTO();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            img.setName(uuid);
            img.setProductId(productId);
            img.setLink(url);

            if (productImages.isEmpty()) {
                imageRepository.insertProductImage(img);
            } else {
                imageRepository.updateImage(img);
            }
        }
    }

    private String[] splitUrls(String imgUrls) {
        return imgUrls.replaceAll("\\s+", "").split(",");
    }

    private String mergeUrls(int productId) {
        List<ProductImageDTO> images = imageRepository.findImagesByProductId(productId);
        StringBuilder sb = new StringBuilder();
        for (ProductImageDTO image : images) {
            sb.append(image.getLink()).append(",");
        }
        return sb.toString();
    }

    // Inner classes for better organization
    private static class ProductFormData {
        private int id;
        private String name;
        private String description;
        private String categoryTypeId;
        private String originalPrice;
        private String discountPrice;
        private String discountPercent;
        private String quantity;
        private String size;
        private String otherSpec;
        private String status;
        private String keyword;
        private String imgUrls;

        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategoryTypeId() { return categoryTypeId; }
        public void setCategoryTypeId(String categoryTypeId) { this.categoryTypeId = categoryTypeId; }
        public String getOriginalPrice() { return originalPrice; }
        public void setOriginalPrice(String originalPrice) { this.originalPrice = originalPrice; }
        public String getDiscountPrice() { return discountPrice; }
        public void setDiscountPrice(String discountPrice) { this.discountPrice = discountPrice; }
        public String getDiscountPercent() { return discountPercent; }
        public void setDiscountPercent(String discountPercent) { this.discountPercent = discountPercent; }
        public String getQuantity() { return quantity; }
        public void setQuantity(String quantity) { this.quantity = quantity; }
        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }
        public String getOtherSpec() { return otherSpec; }
        public void setOtherSpec(String otherSpec) { this.otherSpec = otherSpec; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getKeyword() { return keyword; }
        public void setKeyword(String keyword) { this.keyword = keyword; }
        public String getImgUrls() { return imgUrls; }
        public void setImgUrls(String imgUrls) { this.imgUrls = imgUrls; }
    }

    private static class ValidationResult {
        private boolean valid;
        private List<String> errors;

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
    }
}
