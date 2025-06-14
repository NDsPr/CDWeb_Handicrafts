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

@WebServlet(value = {"/shop"})
public class ShopController extends HttpServlet {
    private final CustomizeDAO customizeDAO = new CustomizeDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final CategoryTypeDAO categoryTypeDAO = new CategoryTypeDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ImageDAO imageDAO = new ImageDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy ra customize web
        CustomizeBean customizeInfo = customizeDAO.getCustomizeInfo();
        // Sử dụng cho navigation bên trái
        List<CategoryBean> categories = categoryDAO.findAllCategories();
        Map<Integer, List<CategoryTypeBean>> categoryTypeMap = new HashMap<>();
        // Hiển thị nội dung ở giữa
        Map<Integer, List<ProductBean>> productMap = new HashMap<>();
        Map<Integer, ProductImageBean> imageMap = new HashMap<>();

        // Đưa sản phẩm tương ứng với categoryId vào map
        for (CategoryBean category : categories) {
            int categoryId = category.getId();
            List<ProductBean> products = productDAO.findThreeProductByCategoryId(categoryId);
            // Đưa ảnh tương ứng với productId vào map của ảnh
            for (ProductBean product : products) {
                imageMap.put(product.getId(), imageDAO.findOneByProductId(product.getId()));
            }
            productMap.put(categoryId, products);
        }

        // // Đưa phân loại sản phẩm tương ứng với categoryId vào map (navigation bên trái)
        for (CategoryBean category : categories) {
            int categoryId = category.getId();
            List<CategoryTypeBean> categoryTypes = categoryTypeDAO.findCategoryTypeByCategoryId(categoryId);
            categoryTypeMap.put(categoryId, categoryTypes);
        }

        req.setAttribute("customizeInfo", customizeInfo);
        req.setAttribute("categories", categories);
        req.setAttribute("categoryTypeMap", categoryTypeMap);
        req.setAttribute("productMap", productMap);
        req.setAttribute("imageMap", imageMap);
        req.getRequestDispatcher("/shop.jsp").forward(req, resp);
    }
}
