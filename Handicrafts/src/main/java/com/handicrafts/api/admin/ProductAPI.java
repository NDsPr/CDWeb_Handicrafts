package com.handicrafts.api.admin;

import com.ltw.bean.ProductBean;
import com.ltw.bean.UserBean;
import com.ltw.constant.LogLevel;
import com.ltw.constant.LogState;
import com.ltw.dao.ImageDAO;
import com.ltw.dao.ProductDAO;
import com.ltw.dto.DatatableDTO;
import com.ltw.service.LogService;
import com.ltw.util.SendEmailUtil;
import com.ltw.util.SessionUtil;
import com.ltw.util.TransferDataUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = {"/api/admin/product"})
public class ProductAPI extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();
    private ImageDAO imageDAO = new ImageDAO();
    private LogService<ProductBean> logService = new LogService<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy ra các Property mà DataTable gửi về
        // Thông tin về phân trang
        int draw = Integer.parseInt(req.getParameter("draw"));      // Số thứ tự của request hiện tại
        int start = Integer.parseInt(req.getParameter("start"));    // Vị trí bắt đầu của dữ liệu
        int length = Integer.parseInt(req.getParameter("length"));  // Số phần tử trên một trang

        // Thông tin về tìm kiếm
        String searchValue = req.getParameter("search[value]");

        // Thông tin về sắp xếp
        String orderBy = req.getParameter("order[0][column]") == null ? "0" : req.getParameter("order[0][column]");
        String orderDir = req.getParameter("order[0][dir]") == null ? "asc" : req.getParameter("order[0][dir]");
        String columnOrder = req.getParameter("columns[" + orderBy + "][data]");      // Tên của cột muốn sắp xếp

        List<ProductBean> products = productDAO.getProductsDatatable(start, length, columnOrder, orderDir, searchValue);
        for (ProductBean product : products) {
            product.setImages(imageDAO.findImagesByProductId(product.getId()));
        }
        int recordsTotal = productDAO.getRecordsTotal();
        // Tổng số record khi filter search
        int recordsFiltered = productDAO.getRecordsFiltered(searchValue);
        draw++;

        DatatableDTO<ProductBean> productDatatableDTO = new DatatableDTO<>(products, recordsTotal, recordsFiltered, draw);
        String jsonData = new TransferDataUtil<DatatableDTO<ProductBean>>().toJson(productDatatableDTO);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonData);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String status;
        String notify;

        // Lấy ra UserBean từ session để lấy
        int affectedRows = productDAO.disableProduct(id);

        // Product trước đó (Trong db)
        ProductBean prevProduct = productDAO.findProductById(id);
        if (affectedRows > 0) {
            logService.log(req, "admin-delete-product", LogState.SUCCESS, LogLevel.WARNING, prevProduct, null);
            status = "error";
            notify = "Có lỗi khi vô hiệu hóa sản phẩm!";
        } else {
            logService.log(req, "admin-delete-product", LogState.FAIL, LogLevel.ALERT, prevProduct, prevProduct);
            status = "success";
            notify = "Vô hiệu hóa sản phẩm thành công!";
            UserBean user = (UserBean) SessionUtil.getInstance().getValue(req, "user");
            SendEmailUtil.sendDeleteNotify(user.getId(), user.getEmail(), prevProduct.getId(), "Product");
        }
        String jsonData = "{\"status\": \"" + status + "\", \"notify\": \"" + notify + "\"}";

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonData);
    }
}
