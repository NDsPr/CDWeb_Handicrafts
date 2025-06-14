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

@WebServlet(value = {"/shop-detail-by-type"})
public class ShopDetailByTypeController extends HttpServlet {
    private final CustomizeDAO customizeDAO = new CustomizeDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final CategoryTypeDAO categoryTypeDAO = new CategoryTypeDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ImageDAO imageDAO = new ImageDAO();


    // Sửa số lượng item/pagination: Sửa offset của List<ProductBean>, sửa số lượng bắt đầu của getStartLimit()
    // Và sửa số lương bắt đầu của getTotalPages
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryTypeId = req.getParameter("categoryTypeId");
        // Pagination
        String recentPage = req.getParameter("recentPage");
        // Sort & Range
        String sort = req.getParameter("sort");
        String range = req.getParameter("range");
        int totalPages = getTotalPagesByCategoryType(Integer.parseInt(categoryTypeId));
        CustomizeBean customizeInfo = customizeDAO.getCustomizeInfo();
        // Sử dụng cho navigation bên trái
        List<CategoryBean> categories = categoryDAO.findAllCategories();
        Map<Integer, List<CategoryTypeBean>> categoryTypeMap = new HashMap<>();
        // Sử dụng cho phần nội dung (Tên type và danh sách sản phẩm)
        CategoryTypeBean categoryType = categoryTypeDAO.findTypeById(Integer.parseInt(categoryTypeId));
        // Phân tích range và lấy ra mảng giá trị
        double[] rangeLimit = getLimitRange(range);
        List<ProductBean> products = productDAO.findByTypeIdAndLimit(Integer.parseInt(categoryTypeId), rangeLimit, sort, getStartLimit(Integer.parseInt(recentPage)), 2);

        // Lấy ra List ảnh dựa vào id của product
        Map<Integer, ProductImageBean> imageMap = new HashMap<>();
        for (ProductBean product : products) {
            ProductImageBean thumbnailImage = imageDAO.findOneByProductId(product.getId());
            imageMap.put(product.getId(), thumbnailImage);
        }

        // Đưa phân loại sản phẩm tương ứng với categoryId vào map (navigation bên trái)
        for (CategoryBean category : categories) {
            List<CategoryTypeBean> categoryTypes = categoryTypeDAO.findCategoryTypeByCategoryId(category.getId());
            categoryTypeMap.put(category.getId(), categoryTypes);
        }

        req.setAttribute("customizeInfo", customizeInfo);
        req.setAttribute("serverPage", Integer.valueOf(recentPage));
        req.setAttribute("serverTotalPages", totalPages);
        req.setAttribute("sort", sort);
        req.setAttribute("range", range);
        req.setAttribute("categoryType", categoryType);
        req.setAttribute("products", products);
        req.setAttribute("categories", categories);
        req.setAttribute("categoryTypeMap", categoryTypeMap);
        req.setAttribute("imageMap", imageMap);
        req.getRequestDispatcher("/shop-detail-type.jsp").forward(req, resp);
    }

    private double[] getLimitRange(String range) {
        if (!range.equals("none")) {
            double[] rangeLimit = new double[2];
            switch (range) {
                case "0-to-499":
                    rangeLimit[0] = 0;
                    rangeLimit[1] = 499000.0;
                    break;
                case "500-to-2999":
                    rangeLimit[0] = 500000.0;
                    rangeLimit[1] = 2999000.0;
                    break;
                case "3000-to-9999":
                    rangeLimit[0] = 3000000.0;
                    rangeLimit[1] = 9999000.0;
                    break;
                case "up-to-10000":
                    rangeLimit[0] = 10000000.0;
                    rangeLimit[1] = 10000000000.0;
                    break;
            }
            return rangeLimit;
        } else {
            return null;
        }
    }

    // Pagination
    private int getTotalPagesByCategoryType(int categoryTypeId) {
        int totalItems = productDAO.getTotalItemsByCategoryType(categoryTypeId);
        return (int) Math.ceil((double) totalItems / 2);
    }

    // Pagination
    private int getStartLimit(int page) {
        return 2 * (page - 1);
    }
}
