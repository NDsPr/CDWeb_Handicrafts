package com.handicrafts.controller.image;

import com.ltw.bean.ProductImageBean;
import com.ltw.dao.ImageDAO;
import com.ltw.util.BlankInputUtil;
import com.ltw.util.CloudStorageUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

@WebServlet(value = {"/admin/all-image-editing"})
@MultipartConfig
public class AllImageEditingController extends HttpServlet {
    private final ImageDAO imageDAO = new ImageDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        ProductImageBean image = imageDAO.findImageById(id);
        req.setAttribute("image", image);
        req.getRequestDispatcher("/all-image-editing.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String productId = req.getParameter("productId");
        String link = req.getParameter("link");
        Part part = req.getPart("imageFile");
        ProductImageBean imageBean;

        String success;
        String[] inputsForm = new String[]{name, productId};
        ArrayList<String> errors = new ArrayList<>();

        // Biến bắt lỗi
        boolean isValid = true;

        for (String string : inputsForm) {
            if (BlankInputUtil.isBlank(string)) {
                errors.add("e");
                if (isValid) {
                    isValid = false;
                }
            } else {
                errors.add(null);
            }
        }
        req.setAttribute("errors", errors);

        // Nếu không lỗi gì trong việc validate thì tiếp tục
        if (isValid) {
            // Part <= 0 nghĩa là không upload ảnh, sẽ lưu lại ảnh cũ vào db
            if (part.getSize() <= 0) {
                imageBean = new ProductImageBean();
                imageBean.setId(id);
                imageBean.setName(name);
                imageBean.setProductId(Integer.parseInt(productId));
                imageBean.setLink(link);
                // Cập nhật thời gian và người chỉnh sửa
                imageBean.setModifiedDate(new Timestamp(System.currentTimeMillis()));
                imageBean.setModifiedBy("");
                // Câp nhật ảnh vào database
                imageDAO.updateImageNotPart(imageBean);
            } else {
                // Không null nghĩa là có upload ảnh mới
                // Upload ảnh lên Cloud Storage và lấy link instance ProductImageBean gồm tên ảnh và link ảnh
                imageBean = CloudStorageUtil.uploadOneImageToCloudStorage(part);
                // Xóa ảnh cũ trong storage
                String nameInStorage = imageDAO.findNameInStorageById(id);
                CloudStorageUtil.delete(nameInStorage);
                // Đã thêm tên ảnh luôn được lưu trên Storage trong util (Để khi xóa sẽ lấy tên này ra)
                imageBean.setId(id);
                imageBean.setName(name);
                imageBean.setProductId(Integer.parseInt(productId));
                // Cập nhật thời gian và người chỉnh sửa
                imageBean.setModifiedDate(new Timestamp(System.currentTimeMillis()));
                imageBean.setModifiedBy("");
                // Câp nhật ảnh vào database
                imageDAO.updateImage(imageBean);
            }
            success = "s";
            req.setAttribute("success", success);
        }
        ProductImageBean image = imageDAO.findImageById(id);
        req.setAttribute("image", image);
        req.getRequestDispatcher("/all-image-editing.jsp").forward(req, resp);
    }
}
