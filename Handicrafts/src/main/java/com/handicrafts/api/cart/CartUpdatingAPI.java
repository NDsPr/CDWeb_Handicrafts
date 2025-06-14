package com.handicrafts.api.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltw.bean.Cart;
import com.ltw.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = {"/api/cart-updating"})
public class CartUpdatingAPI extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int productId = Integer.parseInt(req.getParameter("productId"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        // Lấy Item trong Cart ra để cập nhật (Đã có hàm updateItem trong Cart)
        Cart cart = (Cart) SessionUtil.getInstance().getValue(req, "cart");
        cart.updateItem(productId, quantity);

        ObjectMapper objectMapper = new ObjectMapper();
        String stringJson = objectMapper.writeValueAsString(cart);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(stringJson);
    }
}
