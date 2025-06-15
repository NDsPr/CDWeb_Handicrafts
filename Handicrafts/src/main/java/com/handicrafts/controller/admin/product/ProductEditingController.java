package com.handicrafts.controller.admin.product;

import com.handicrafts.bean.ProductBean;
import com.handicrafts.bean.ProductImageBean;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dao.ImageDAO;
import com.handicrafts.dao.ProductDAO;
import com.handicrafts.service.LogService;
import com.handicrafts.util.NumberValidateUtil;
import com.handicrafts.util.ValidateParamUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@WebServlet(value = {"/admin/product-management/editing"})
public class ProductEditingController extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();
    private LogService<ProductBean> logService = new LogService<>();
    private ImageDAO imageDAO = new ImageDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        ProductBean productBean = productDAO.findProductById(id);
        String imgUrlsMerge = mergeUrls(id);
        req.setAttribute("imgUrls", imgUrlsMerge);
        req.setAttribute("productBean", productBean);
        req.getRequestDispatcher("/editing-product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // Sử dụng vòng lặp để set lỗi để trống theo index,
        // tuy nhiên cần phải giữ đúng thứ tự của input theo form và theo database (Vì sử dụng vòng lặp theo i để set lỗi)
        String idStr = req.getParameter("id");
        // Lấy id kiểu int ra để lưu vào database
        int id = Integer.parseInt(idStr);
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

        // Các biến lưu giữ lỗi về giá
        String oPrErr = "e", dPrErr = "e", dPeErr = "e", qErr = "e";

        // Biến thông báo
        String msg = "";

        // Đặt các thuộc tính đúng thứ tự
        String[] inputsForm = new String[]{name, description, categoryTypeId, originalPrice, discountPrice, discountPercent, quantity, size, status, imgUrls};
        // Mảng lưu trữ lỗi
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        // Biến bắt lỗi
        boolean isValid = true;

        // Nếu có lỗi (khác null) trả về isValid = false
        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }


        // Kiểm tra các lỗi nhập liệu khác
        // Lỗi nhập liệu cho giá và phần trăm (Là phần số)
        if (!NumberValidateUtil.isNumeric(originalPrice) || !NumberValidateUtil.isValidPrice(originalPrice)) {
            if (isValid) {
                isValid = false;
            }
            req.setAttribute("oPrErr", oPrErr);
        }

        if (!NumberValidateUtil.isNumeric(discountPrice) || !NumberValidateUtil.isValidPrice(discountPrice)) {
            if (isValid) {
                isValid = false;
            }
            req.setAttribute("oPrErr", dPrErr);
        }

        if (!NumberValidateUtil.isNumeric(discountPercent) || !NumberValidateUtil.isValidPercent(discountPercent)) {
            if (isValid) {
                isValid = false;
            }
            req.setAttribute("dPeErr", dPeErr);
        }
        if (!NumberValidateUtil.isNumeric(quantity) || !NumberValidateUtil.isValidQuantity(quantity)) {
            if (isValid) {
                isValid = false;
            }
            req.setAttribute("qErr", qErr);
        }

        // Product trước đó (Trong db)
        ProductBean prevProduct = productDAO.findProductById(id);

        // Nếu không lỗi thì lưu vào database
        if (isValid) {
            // Đổi String về số
            int categoryTypeIdInt = NumberValidateUtil.toInt(categoryTypeId);
            int quantityInt = NumberValidateUtil.toInt(quantity);
            int statusInt = NumberValidateUtil.toInt(status);
            double originalPriceDouble = NumberValidateUtil.toDouble(originalPrice);
            double discountPriceDouble = NumberValidateUtil.toDouble(discountPrice);
            double discountPercentDouble = NumberValidateUtil.toDouble(discountPercent);

            // Set thuộc tính vào bean
            ProductBean productBean = new ProductBean();
            productBean.setId(id);
            productBean.setName(name);
            productBean.setDescription(description);
            productBean.setCategoryTypeId(categoryTypeIdInt);
            productBean.setOriginalPrice(originalPriceDouble);
            productBean.setDiscountPrice(discountPriceDouble);
            productBean.setDiscountPercent(discountPercentDouble);
            productBean.setQuantity(quantityInt);
            productBean.setSize(size);
            if (otherSpec != null) {
                productBean.setOtherSpec(otherSpec);
            } else {
                productBean.setOtherSpec("");
            }
            productBean.setStatus(statusInt);
            if (otherSpec != null) {
                productBean.setKeyword(keyword);
            } else {
                productBean.setKeyword("");
            }
            productBean.setCreatedDate(new Timestamp(System.currentTimeMillis()));

            int affectedRows = productDAO.updateProduct(productBean);

            ProductBean currentProduct = productDAO.findProductById(id);
            if (affectedRows < 0) {
                logService.log(req, "admin-update-product", LogState.FAIL, LogLevel.ALERT, prevProduct, currentProduct);
                msg = "error";
            } else if (affectedRows > 0) {
                logService.log(req, "admin-update-product", LogState.SUCCESS, LogLevel.WARNING, prevProduct, currentProduct);
                msg = "success";
                List<ProductImageBean> productImageBeans = imageDAO.findImagesByProductId(id);
                for (String url : splitUrls(imgUrls)) {
                    ProductImageBean productImgBean = new ProductImageBean();
                    // TODO: Cần xem lại logic
                    String uuid = UUID.randomUUID().toString().replace("-", "");
                    productImgBean.setName(uuid);
                    productImgBean.setProductId(id);
                    productImgBean.setLink(url);
                    if (productImageBeans.isEmpty()) {
                        imageDAO.insertProductImage(productImgBean);
                    }
                    imageDAO.updateImage(productImgBean);
                }
            }
        } else {
            ProductBean currentProduct = productDAO.findProductById(id);
            req.setAttribute("errors", errors);
            logService.log(req, "admin-update-product", LogState.FAIL, LogLevel.ALERT, prevProduct, currentProduct);
            msg = "error";
        }

        // Truyền vào 1 productId
        String imgUrlsMerge = mergeUrls(id);
        req.setAttribute("imgUrls", imgUrlsMerge);

        ProductBean displayProduct = productDAO.findProductById(id);
        req.setAttribute("msg", msg);
        req.setAttribute("productBean", displayProduct);
        req.getRequestDispatcher("/editing-product.jsp").forward(req, resp);
    }

    private String[] splitUrls(String imgUrls) {
        String replaceSpace = imgUrls.replaceAll("\\s+", "");
        return replaceSpace.split(",");
    }

    private String mergeUrls(int productId) {
        List<ProductImageBean> productImageBeans = imageDAO.findImagesByProductId(productId);
        StringBuilder sb = new StringBuilder();
        for (ProductImageBean image : productImageBeans) {
            sb.append(image.getLink()).append(",");
        }
        return sb.toString();
    }
}
