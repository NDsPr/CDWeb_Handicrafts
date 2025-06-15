package com.handicrafts.controller.image;

import com.handicrafts.bean.ProductImageBean;
import com.handicrafts.dao.ImageDAO;
import com.handicrafts.util.BlankInputUtil;
import com.handicrafts.util.CloudStorageUtil;

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
import java.util.List;


// TODO: Sửa lại createdBy và modifiedBy sau khi đã xong phần đăng nhập
@WebServlet(value = {"/admin/all-image-adding"})
@MultipartConfig
public class AllImageAddingController extends HttpServlet {
    private final ImageDAO imageDAO = new ImageDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/all-image-adding.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        String productId = req.getParameter("productId");
        List<Part> filePaths = imagePaths((List<Part>) req.getParts());

        String success;
        String[] inputsForm = new String[]{name, productId};
        ArrayList<String> errors = new ArrayList<>();

        // Biến bắt lỗi
        boolean isValid = true;

        // Kiểm tra lỗi cho các trường input
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

        // Kiểm tra lỗi cho input file ảnh
        if (filePaths == null) {
            errors.add("e");
            if (isValid) {
                isValid = false;
            }
        } else {
            errors.add(null);
        }
        req.setAttribute("errors", errors);

        // Nếu không lỗi gì trong việc validate thì tiếp tục
        if (isValid) {
            for (Part part : filePaths) {
                String nameInStorage = part.getSubmittedFileName();
                ProductImageBean image = CloudStorageUtil.uploadOneImageToCloudStorage(part);
                image.setName(name);
                image.setNameInStorage(nameInStorage);
                image.setProductId(Integer.parseInt(productId));
                image.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                image.setCreatedBy("");
                image.setModifiedDate(new Timestamp(System.currentTimeMillis()));
                image.setModifiedBy("");

                // Thêm ảnh vào database
                imageDAO.insertProductImage(image);
                success = "s";
                req.setAttribute("success", success);
            }
        }
        req.getRequestDispatcher("/all-image-adding.jsp").forward(req, resp);
    }

    // Lấy ra các Path không phải là trường nhập và là ảnh
    private List<Part> imagePaths(List<Part> filePaths) {
        List<Part> images = new ArrayList<>();
        for (Part part : filePaths) {
            if (!isFormField(part)) {
                if (part.getContentType().startsWith("image")) {
                    images.add(part);
                }
            }
        }
        return images;
    }
    private boolean isFormField(Part part) {
        String contentType = part.getHeader("content-type");
        return contentType == null;
    }
}
