package com.handicrafts.controller.web;

import com.ltw.bean.CustomizeBean;
import com.ltw.bean.UserBean;
import com.ltw.constant.LogLevel;
import com.ltw.constant.LogState;
import com.ltw.dao.CustomizeDAO;
import com.ltw.dao.UserDAO;
import com.ltw.service.LogService;
import com.ltw.util.BlankInputUtil;
import com.ltw.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

@WebServlet(value = {"/user-info"})
public class UserInfoController extends HttpServlet {
    private final CustomizeDAO customizeDAO = new CustomizeDAO();
    private final UserDAO userDAO = new UserDAO();
    private LogService<UserBean> logService = new LogService<>();
    private ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomizeBean customizeInfo = customizeDAO.getCustomizeInfo();
        UserBean userBean = (UserBean) SessionUtil.getInstance().getValue(req, "user");

        req.setAttribute("userInfo", userBean);
        req.setAttribute("customizeInfo", customizeInfo);
        req.getRequestDispatcher("client-userinfo.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        UserBean session = (UserBean) SessionUtil.getInstance().getValue(req, "user");
        // Nhận vào các dữ liệu từ JSP
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String addressLine = req.getParameter("addressLine");
        String addressWard = req.getParameter("addressWard");
        String addressDistrict = req.getParameter("addressDistrict");
        String addressProvince = req.getParameter("addressProvince");

        // String thông báo lên JSP
        String msg = "";
        String error = "";
        String fnError;
        String lnError;
        String alError;
        String awError;
        String adError;
        String apError;

        // Tạo ra bean lưu trữ thông tin để đưa xuống DAO
        UserBean userBean;

        // Kiểm tra các trường hợp bỏ trống
        // TODO: Nếu không trống, kiểm tra các trường hợp có số
        if (BlankInputUtil.isBlank(firstName)) {
            error = "error";
            fnError = "e";
            req.setAttribute("fnErr", fnError);
        }
        // TODO: Nếu không trống, kiểm tra các trường hợp có số
        if (BlankInputUtil.isBlank(lastName)) {
            error = "error";
            lnError = "e";
            req.setAttribute("lnErr", lnError);
        }
        if (BlankInputUtil.isBlank(addressLine)) {
            error = "error";
            alError = "e";
            req.setAttribute("alErr", alError);
        }
        if (BlankInputUtil.isBlank(addressWard)) {
            error = "error";
            awError = "e";
            req.setAttribute("awErr", awError);
        }
        if (BlankInputUtil.isBlank(addressDistrict)) {
            error = "error";
            adError = "e";
            req.setAttribute("adErr", adError);
        }
        if (BlankInputUtil.isBlank(addressProvince)) {
            error = "error";
            apError = "e";
            req.setAttribute("apErr", apError);
        }

        // Nếu không lỗi thì set thành công và lưu vào database
        if (!error.equals("error")) {
            // Set thuộc tính vào bean
            userBean = new UserBean();
            userBean.setFirstName(firstName);
            userBean.setLastName(lastName);
            userBean.setAddressLine(addressLine);
            userBean.setAddressWard(addressWard);
            userBean.setAddressDistrict(addressDistrict);
            userBean.setAddressProvince(addressProvince);
            userBean.setId(session.getId());

            UserBean prevUser = userDAO.findUserById(session.getId());
            // Đưa bean xuống Database
            int affectedRows = userDAO.updateAccount(userBean);
            UserBean currentUser = userDAO.findUserById(session.getId());
            if (affectedRows > 0) {
                logService.log(req, "user-change-info", LogState.SUCCESS, LogLevel.WARNING, prevUser, currentUser);
                msg = "success";
                // Cập nhật lại giá trị trên Session
                SessionUtil.getInstance().putValue(req, "user", userDAO.findUserById(session.getId()));
            } else {
                logService.log(req, "user-change-info", LogState.FAIL, LogLevel.WARNING, prevUser, currentUser);
                msg = "fail";
            }
        }

        // Cuối cùng lấy thông tin từ db để hiển thị cho người dùng
        userBean = userDAO.findUserById(session.getId());
        req.setAttribute("userInfo", userBean);
        req.setAttribute("msg", msg);
        req.getRequestDispatcher("/client-userinfo.jsp").forward(req, resp);
    }
}
