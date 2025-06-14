package com.handicrafts.controller.web;

import com.ltw.bean.CustomizeBean;
import com.ltw.bean.ProductBean;
import com.ltw.bean.ProductImageBean;
import com.ltw.bean.ReviewBean;
import com.ltw.dao.CustomizeDAO;
import com.ltw.dao.ImageDAO;
import com.ltw.dao.ProductDAO;
import com.ltw.dao.ReviewDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = {"/product-detail"})
public class ProductDetailController extends HttpServlet {
    private final CustomizeDAO customizeDAO = new CustomizeDAO();
    private final ProductDAO productDetailDAO = new ProductDAO();
    private final ImageDAO imageDAO = new ImageDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomizeBean customizeInfo = customizeDAO.getCustomizeInfo();
        int productId = Integer.parseInt(req.getParameter("id"));

        ProductBean productDetailBean = productDetailDAO.findProductById(productId);
        List<ProductImageBean> productImages = imageDAO.findImagesByProductId(productId);
        productDetailBean.setImages(productImages);

        List<ProductBean> productSuggest = productDetailDAO.findSixProductsForSuggest(productId, productDetailBean.getCategoryTypeId(), 0);
        for (ProductBean product : productSuggest) {
            List<ProductImageBean> thumbnail = imageDAO.getThumbnailByProductId(product.getId());
            product.setImages(thumbnail);
        }

        List<ReviewBean> reviews = reviewDAO.findReviewPaginationByProductId(productId, 0, 0);

        req.setAttribute("customizeInfo", customizeInfo);
        req.setAttribute("productDetail", productDetailBean);
        req.setAttribute("productSuggest", productSuggest);
        req.setAttribute("reviews", reviews);

        req.getRequestDispatcher("/product-detail.jsp").forward(req, resp); 
    }
}
