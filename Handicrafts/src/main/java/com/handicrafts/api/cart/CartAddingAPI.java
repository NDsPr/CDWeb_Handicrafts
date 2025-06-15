package com.handicrafts.api.cart;

import com.handicrafts.bean.Cart;
import com.handicrafts.bean.Item;
import com.handicrafts.bean.ProductBean;
import com.handicrafts.bean.ProductImageBean;
import com.handicrafts.dao.ImageDAO;
import com.handicrafts.dao.ProductDAO;
import com.handicrafts.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// Cần tạo API bắt đầu bằng "/api/cart-"
@WebServlet(value = {"/api/cart-adding"})
public class CartAddingAPI extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();
    private final ImageDAO imageDAO = new ImageDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("productId");
        ProductBean product = productDAO.findProductById(Integer.parseInt(productId));
        List<ProductImageBean> thumbnailProduct = imageDAO.getThumbnailByProductId(Integer.parseInt(productId));
        product.setImages(thumbnailProduct);
        Cart cart = (Cart) SessionUtil.getInstance().getValue(req, "cart");

        Item item = new Item();
        item.setProduct(product);

        cart.addItem(item);
        SessionUtil.getInstance().putValue(req, "cart", cart);
        int totalItems = cart.getTotalItem();

        String responseJson = "{\"totalItems\": " + totalItems + ", \"success\": \"true\"}";
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(responseJson);
    }
}
