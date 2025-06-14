package com.handicrafts.controller.admin.product;

import com.ltw.bean.ProductBean;
import com.ltw.bean.ProductImageBean;
import com.ltw.constant.LogLevel;
import com.ltw.constant.LogState;
import com.ltw.dao.ImageDAO;
import com.ltw.dao.ProductDAO;
import com.ltw.service.LogService;
import com.ltw.util.NumberValidateUtil;
import com.ltw.util.ValidateParamUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@WebServlet(value = {"/admin/product-management/adding"})
public class ProductAddingController extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();
    private LogService<ProductBean> logService = new LogService<>();
    private ImageDAO imageDAO = new ImageDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/adding-product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // Sử dụng vòng lặp để set lỗi để trống theo index,
        // tuy nhiên cần phải giữ đúng thứ tự của input theo form và theo database (Vì sử dụng vòng lặp theo i để set lỗi)
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
        // Biến bắt lỗi
        boolean isValid = true;

        // Kiểm tra input rằng/null trong hàm checkEmptyParam
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

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

        // Kiểm tra tên sản phẩm (Không được trùng tên)
        if (productDAO.isExistProductName(name)) {
            // TODO: Thêm bắt lỗi trên JSP khi xử lý lỗi db
            String nameErr = "e";
            if (isValid) {
                isValid = false;
            }
            req.setAttribute("nameErr", nameErr);
        }

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

            int id = productDAO.createProduct(productBean);
            if (id <= 0) {
                logService.log(req, "admin-create-product", LogState.FAIL, LogLevel.ALERT, null, null);
                msg = "error";
            } else {
                ProductBean currentProduct = productDAO.findProductById(id);
                logService.log(req, "admin-create-product", LogState.SUCCESS, LogLevel.WARNING, null, currentProduct);
                msg = "success";
                for (String url : splitUrls(imgUrls)) {
                    ProductImageBean productImgBean = new ProductImageBean();
                    String uuid = UUID.randomUUID().toString().replace("-", "");
                    productImgBean.setName(uuid);
                    productImgBean.setProductId(id);
                    productImgBean.setLink(url);
                    imageDAO.insertProductImage(productImgBean);
                }
            }
        } else {
            req.setAttribute("errors", errors);
            logService.log(req, "admin-create-product", LogState.FAIL, LogLevel.ALERT, null, null);
            msg = "error";
        }

        req.setAttribute("msg", msg);
        req.getRequestDispatcher("/adding-product.jsp").forward(req, resp);
    }

    private String[] splitUrls(String imgUrls) {
        String replaceSpace = imgUrls.replaceAll("\\s+", "");
        return replaceSpace.split(",");
    }
}
