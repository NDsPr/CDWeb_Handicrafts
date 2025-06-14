package com.handicrafts.api.cart;

import com.ltw.bean.Cart;
import com.ltw.bean.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = {"/api/cart-deleting"})
public class CartDeletingAPI extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int productId = Integer.parseInt(req.getParameter("productId"));
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        int status = 0;
        List<Item> items = cart.getItems();
        if (!items.isEmpty()) {
            status = cart.deleteItem(productId);
        }

        int totalItems = cart.getTotalItem();
        // Kiểm tra trạng thái của hành động delete
        String serverResponse = switch (status) {
            case -1 -> "fail";
            case 1 -> "success";
            case 0 -> "empty";
            default -> "invalid";
        };

        String json = "{\"serverResponse\": \"" + serverResponse + "\", \"totalItems\": " + totalItems +  "}";
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(json);
    }
}
