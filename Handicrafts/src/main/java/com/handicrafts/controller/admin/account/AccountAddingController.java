package com.handicrafts.controller.admin.account;

import com.handicrafts.bean.UserBean;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dao.UserDAO;
import com.handicrafts.service.LogService;
import com.handicrafts.util.EncryptPasswordUtil;
import com.handicrafts.util.NumberValidateUtil;
import com.handicrafts.util.ValidateParamUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

@WebServlet("/admin/account-management/adding")
public class AccountAddingController extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private LogService<UserBean> logService = new LogService<>();
    private ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/adding-account.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String password = req.getParameter("password");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String roleId = req.getParameter("roleId");
        String email = req.getParameter("email");
        String addressLine = req.getParameter("addressLine");
        String addressWard = req.getParameter("addressWard");
        String addressDistrict = req.getParameter("addressDistrict");
        String addressProvince = req.getParameter("addressProvince");
        String status = req.getParameter("status");

        String msg = null;
        String[] inputsForm = {email, password, firstName, lastName, roleId, status};
        // Biến bắt lỗi
        boolean isValid = true;

        // Kiểm tra input rỗng/null trong hàm checkEmptyParam
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        // Nếu có lỗi (khác null) trả về isValid = false
        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        // Nếu không lỗi thì lưu vào database cùng với
        if (isValid) {
            // Đổi String về số
            int roleIdInt = NumberValidateUtil.toInt(roleId);
            int statusInt = NumberValidateUtil.toInt(status);

            // Set thuộc tính vào bean
            UserBean nowChange = new UserBean();
            nowChange.setEmail(email);
            nowChange.setPassword(EncryptPasswordUtil.encryptPassword(password));
            nowChange.setFirstName(firstName);
            nowChange.setLastName(lastName);
            nowChange.setRoleId(roleIdInt);
            nowChange.setStatus(statusInt);
            nowChange.setAddressLine(addressLine);
            nowChange.setAddressWard(addressWard);
            nowChange.setAddressDistrict(addressDistrict);
            nowChange.setAddressProvince(addressProvince);

            int id = userDAO.createAccount(nowChange);

            if (id <= 0) {
                logService.log(req, "admin-update-account", LogState.FAIL, LogLevel.ALERT, null, null);
                msg = "error";
            } else {
                UserBean currentUser = userDAO.findUserById(id);
                logService.log(req, "admin-update-account", LogState.SUCCESS, LogLevel.WARNING, null, currentUser);
                msg = "success";
            }
        } else {
            // Lỗi nhập liệu người dùng thì không ghi log
            req.setAttribute("errors", errors);
            msg = "error";
        }

        req.setAttribute("msg", msg);
        req.getRequestDispatcher("/adding-account.jsp").forward(req, resp);
    }
}
