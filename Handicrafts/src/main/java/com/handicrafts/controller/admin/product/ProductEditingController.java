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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/product-management")
@RequiredArgsConstructor
public class ProductEditingController {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final LogServiceImp<ProductDTO> logService;

    @GetMapping("/editing")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        ProductDTO product = productRepository.findProductById(id);
        model.addAttribute("imgUrls", mergeUrls(id));
        model.addAttribute("productBean", product);
        return "editing-product";
    }

    @PostMapping("/editing")
    public String updateProduct(HttpServletRequest req, Model model) {
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String categoryTypeId = req.getParameter("categoryTypeId");
        String originalPrice = req.getParameter("originalPrice");
        String discountPrice = req.getParameter("discountPrice");
        String discountPercent = req.getParameter("discountPercent");
        String quantity = req.getParameter("quantity");
        String size = req.getParameter("size");
        String otherSpec = req.getParameter("otherSpec");
        String status = req.getParameter("status");
        String keyword = req.getParameter("keyword");
        String imgUrls = req.getParameter("imgUrls");

        String[] inputsForm = {name, description, categoryTypeId, originalPrice, discountPrice, discountPercent, quantity, size, status, imgUrls};
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);
        boolean isValid = errors.stream().noneMatch(e -> e != null);

        if (!NumberValidateUtil.isValidPrice(originalPrice)) {
            isValid = false;
            model.addAttribute("oPrErr", "e");
        }

        if (!NumberValidateUtil.isValidPrice(discountPrice)) {
            isValid = false;
            model.addAttribute("dPrErr", "e");
        }

        if (!NumberValidateUtil.isValidPercent(discountPercent)) {
            isValid = false;
            model.addAttribute("dPeErr", "e");
        }

        if (!NumberValidateUtil.isValidQuantity(quantity)) {
            isValid = false;
            model.addAttribute("qErr", "e");
        }

        ProductDTO prevProduct = productRepository.findProductById(id);
        String msg;

        if (isValid) {
            ProductDTO product = new ProductDTO();
            product.setId(id);
            product.setName(name);
            product.setDescription(description);
            product.setCategoryTypeId(NumberValidateUtil.toInt(categoryTypeId));
            product.setOriginalPrice(NumberValidateUtil.toDouble(originalPrice));
            product.setDiscountPrice(NumberValidateUtil.toDouble(discountPrice));
            product.setDiscountPercent(NumberValidateUtil.toDouble(discountPercent));
            product.setQuantity(NumberValidateUtil.toInt(quantity));
            product.setSize(size);
            product.setOtherSpec(otherSpec != null ? otherSpec : "");
            product.setStatus(NumberValidateUtil.toInt(status));
            product.setKeyword(keyword != null ? keyword : "");
            product.setCreatedDate(new Timestamp(System.currentTimeMillis()));

            int affectedRows = productRepository.updateProduct(product);
            ProductDTO currentProduct = productRepository.findProductById(id);

            if (affectedRows <= 0) {
                logService.log(req, "admin-update-product", LogState.FAIL, LogLevel.ALERT, prevProduct, currentProduct);
                msg = "error";
            } else {
                logService.log(req, "admin-update-product", LogState.SUCCESS, LogLevel.WARNING, prevProduct, currentProduct);
                msg = "success";

                List<ProductImageDTO> productImages = imageRepository.findImagesByProductId(id);
                for (String url : splitUrls(imgUrls)) {
                    ProductImageDTO img = new ProductImageDTO();
                    String uuid = UUID.randomUUID().toString().replace("-", "");
                    img.setName(uuid);
                    img.setProductId(id);
                    img.setLink(url);

                    if (productImages.isEmpty()) {
                        imageRepository.insertProductImage(img);
                    }
                    imageRepository.updateImage(img);
                }
            }

        } else {
            ProductDTO currentProduct = productRepository.findProductById(id);
            model.addAttribute("errors", errors);
            logService.log(req, "admin-update-product", LogState.FAIL, LogLevel.ALERT, prevProduct, currentProduct);
            msg = "error";
        }

        model.addAttribute("imgUrls", mergeUrls(id));
        model.addAttribute("msg", msg);
        model.addAttribute("productBean", productRepository.findProductById(id));
        return "editing-product";
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
}
