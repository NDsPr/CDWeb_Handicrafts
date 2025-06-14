package com.handicrafts.controller.image;

import com.ltw.bean.ProductImageBean;
import com.ltw.dao.ImageDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = {"/admin/all-image-management"})
public class AllImageManagementController extends HttpServlet {
    private final ImageDAO imageDAO = new ImageDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ProductImageBean> allImages = imageDAO.findAllImages();
        req.setAttribute("allImages", allImages);
        req.getRequestDispatcher("/all-image-management.jsp").forward(req, resp);
    }
}
