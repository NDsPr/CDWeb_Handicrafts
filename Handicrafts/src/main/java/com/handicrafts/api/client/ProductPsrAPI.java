package com.handicrafts.api.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltw.bean.ProductBean;
import com.ltw.bean.ProductImageBean;
import com.ltw.dao.ImageDAO;
import com.ltw.dao.ProductDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// TODO: Thêm ảnh vào JSON của sản phẩm
@WebServlet(value = {"/api/product-psr"})
public class ProductPsrAPI extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();
    private final ImageDAO imageDAO = new ImageDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryTypeId = req.getParameter("categoryTypeId");
        // Pagination
        String recentPage = req.getParameter("recentPage");
        // Sort & Range
        String sort = req.getParameter("sort");
        String range = req.getParameter("range");
        int totalPages = getTotalPagesByCategoryType(Integer.parseInt(categoryTypeId));
        double[] rangeLimit = getLimitRange(range);

        // Lấy ra danh sách sản phẩm sau khi đã filter (Loc) qua pagination, sort, range
        List<ProductBean> products = productDAO.findByTypeIdAndLimit(Integer.parseInt(categoryTypeId), rangeLimit, sort, getStartLimit(Integer.parseInt(recentPage)), 2);
        // Thêm ảnh tương ứng vào sản phẩm trong danh sách
        for (ProductBean product : products) {
            List<ProductImageBean> thumbnailImage = new ArrayList<>();
            thumbnailImage.add(imageDAO.findOneByProductId(product.getId()));
            product.setImages(thumbnailImage);
        }

        String jsonToClient = combineJson(products, Integer.parseInt(recentPage), totalPages);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(jsonToClient);
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
            // Đã xử lý trường hợp null
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

    // JSON Kết hợp Product và 2 thuộc tính totalPages, recentPage để phân trang
    private String combineJson(List<ProductBean> products, int recentPage, int totalPages) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(products);
        return "{\"products\": " + productJson + ", \"recentPage\": " + recentPage + ", \"totalPages\": " + totalPages + "}";
    }
}
