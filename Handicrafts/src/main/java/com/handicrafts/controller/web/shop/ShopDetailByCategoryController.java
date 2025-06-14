package com.handicrafts.controller.web.shop;

import com.ltw.bean.*;
import com.ltw.dao.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(value = {"/shop-detail-by-category"})
public class ShopDetailByCategoryController extends HttpServlet {
    private final CustomizeDAO customizeDAO = new CustomizeDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final CategoryTypeDAO categoryTypeDAO = new CategoryTypeDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ImageDAO imageDAO = new ImageDAO();


    // Todo: Cần sửa lại logic gán link ảnh
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryId = req.getParameter("categoryId");
        CustomizeBean customizeInfo = customizeDAO.getCustomizeInfo();
        // Sử dụng cho navigation bên trái
        List<CategoryBean> categories = categoryDAO.findAllCategories();
        Map<Integer, List<CategoryTypeBean>> categoryTypeMap = new HashMap<>();
        // Sử dụng cho hiển thị sản phẩm dựa trên danh mục (category) đã chọn
        List<CategoryTypeBean> categoryTypeForProduct = categoryTypeDAO.findCategoryTypeByCategoryId(Integer.parseInt(categoryId));
        Map<Integer, List<ProductBean>> productMap = new HashMap<>();
        Map<Integer, ProductImageBean> imageMap = new HashMap<>();

        // Đưa phân loại sản phẩm tương ứng với categoryId vào map (navigation bên trái)
        for (CategoryBean category : categories) {
            List<CategoryTypeBean> categoryTypes = categoryTypeDAO.findCategoryTypeByCategoryId(category.getId());
            categoryTypeMap.put(category.getId(), categoryTypes);
        }

        for (CategoryTypeBean categoryType : categoryTypeForProduct) {
            List<ProductBean> products = productDAO.findFourProductByTypeId(categoryType.getId());
            for (ProductBean product : products) {
                imageMap.put(product.getId(), imageDAO.findOneByProductId(product.getId()));
            }
            productMap.put(categoryType.getId(), products);
        }

        req.setAttribute("customizeInfo", customizeInfo);
        req.setAttribute("categories", categories);
        req.setAttribute("categoryTypeMap", categoryTypeMap);
        req.setAttribute("categoryTypeForProduct", categoryTypeForProduct);
        req.setAttribute("productMap", productMap);
        req.setAttribute("imageMap", imageMap);
        req.getRequestDispatcher("/shop-detail.jsp").forward(req, resp);
    }
}
