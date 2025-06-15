package com.handicrafts.controller.image;

import com.handicrafts.bean.ProductImageBean;
import com.handicrafts.dao.ImageDAO;
import com.handicrafts.util.CloudStorageUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = {"/admin/all-image-delete"})
public class AllImageDeleteController extends HttpServlet {
    private final ImageDAO imageDAO = new ImageDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(req.getParameter("id"));
        // Xóa ảnh trong Cloud Storage
        String nameInStorage = imageDAO.findNameInStorageById(id);
        CloudStorageUtil.delete(nameInStorage);

        // Xóa ảnh trong database
        int affectedRows = imageDAO.deleteImage(id);
        if (affectedRows > 0) {
            req.setAttribute("success", "Xóa ảnh thành công!");
        } else if (affectedRows == -1) {
            req.setAttribute("error", "Lỗi hệ thống!");
        } else if (affectedRows == 0){
            req.setAttribute("error", "Ảnh không tồn tại!");
        }
        // Lấy danh sách sản phẩm lên để cập nhật hiển thị trên view
        List<ProductImageBean> allImages = imageDAO.findAllImages();
        req.setAttribute("allImages", allImages);
        req.getRequestDispatcher("/all-image-management.jsp").forward(req, resp);
    }
}
