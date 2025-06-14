package com.handicrafts.controller.admin.account;

import com.ltw.bean.UserBean;
import com.ltw.constant.LogLevel;
import com.ltw.constant.LogState;
import com.ltw.dao.UserDAO;
import com.ltw.service.LogService;
import com.ltw.util.NumberValidateUtil;
import com.ltw.util.ValidateParamUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

@WebServlet("/admin/account-management/editing")
public class AccountEditingController extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private LogService<UserBean> logService = new LogService<>();
    private ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    // Todo: Cần chỉnh sửa lại logic cho thêm người dùng
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        UserBean userBean = userDAO.findUserById(id);
        request.setAttribute("displayUser", userBean);
        request.getRequestDispatcher("/editing-account.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // Sử dụng vòng lặp để set lỗi để trống theo index,
        // tuy nhiên cần phải giữ đúng thứ tự của input theo form và theo database (Vì sử dụng vòng lặp theo i để set lỗi)
        String idStr = req.getParameter("id");
        // Lấy id kiểu int ra để lưu vào database
        int id = Integer.parseInt(idStr);

        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String roleId = req.getParameter("roleId");
        String status = req.getParameter("status");
        String addressLine = req.getParameter("addressLine");
        String addressWard = req.getParameter("addressWard");
        String addressDistrict = req.getParameter("addressDistrict");
        String addressProvince = req.getParameter("addressProvince");

        // Biến thông báo trạng thái
        String msg = "";

        String[] inputsForm = {firstName, lastName, roleId, status, addressLine, addressWard, addressDistrict, addressProvince};
        // Biến bắt lỗi
        boolean isValid = true;

        // Kiểm tra input rằng/null trong hàm checkEmptyParam
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        // Nếu có lỗi (khác null) trả về isValid = false
        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        UserBean prevUser = userDAO.findUserById(id);
        if (isValid) {
            // Đọc lỗi với database
            int roleIdInt = NumberValidateUtil.toInt(roleId);
            int statusInt = NumberValidateUtil.toInt(status);

            // Set thuộc tính vào bean
            UserBean nowChange = new UserBean();
            nowChange.setId(id);
            nowChange.setFirstName(firstName);
            nowChange.setLastName(lastName);
            nowChange.setRoleId(roleIdInt);
            nowChange.setStatus(statusInt);
            nowChange.setAddressLine(addressLine);
            nowChange.setAddressWard(addressWard);
            nowChange.setAddressDistrict(addressDistrict);
            nowChange.setAddressProvince(addressProvince);

            int affectedRow = userDAO.updateAccount(nowChange);
            // Sau khi update, cập nhật nội dung hiện tại trong db
            UserBean currentUser = userDAO.findUserById(id);

            if (affectedRow < 0) {
                logService.log(req, "admin-update-account", LogState.FAIL, LogLevel.ALERT, prevUser, currentUser);
                msg = "error";
            } else if (affectedRow > 0) {
                logService.log(req, "admin-update-account", LogState.SUCCESS, LogLevel.WARNING, prevUser, currentUser);
                msg = "success";
            }
        } else {
            req.setAttribute("errors", errors);
            UserBean currentUser = userDAO.findUserById(id);
            logService.log(req, "admin-update-account", LogState.FAIL, LogLevel.ALERT, prevUser, currentUser);
            msg = "error";
        }

        UserBean displayUser = userDAO.findUserById(id);
        req.setAttribute("msg", msg);
        req.setAttribute("displayUser", displayUser);
        req.getRequestDispatcher("/editing-account.jsp").forward(req, resp);
    }
}