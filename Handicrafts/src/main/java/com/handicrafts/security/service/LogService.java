package com.handicrafts.security.service;

import com.ltw.bean.LogBean;
import com.ltw.bean.UserBean;
import com.ltw.dao.impl.LogDAO;
import com.ltw.dto.LogAddressDTO;
import com.ltw.util.SessionUtil;
import com.ltw.util.TransferDataUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class LogService<T> {
    private LogDAO logDAO = new LogDAO();
    ResourceBundle logContentBundle = ResourceBundle.getBundle("log-content");

    public void log(HttpServletRequest request, String function, String state, int level, Object previousInfo, Object currentInfo) {
        UserBean userModify = (UserBean) SessionUtil.getInstance().getValue(request, "user");
        String ip = request.getRemoteAddr();
        String national = "";
        String content = makeContentName(function, state);
        // Chưa đăng nhập thì id = -1
        int id = (userModify == null) ? -1 : userModify.getId();
        LogAddressDTO address = new LogAddressDTO(function, id, content);
        createLog(ip, national, level, address, previousInfo, currentInfo);
    }

    // Ghi log thông qua model
    // previous và current có thể truyền một model hoặc List của model đó, việc ghi vào JSON đã xử lý riêng trong TransferDataUtil
    private void createLog(String ip, String national, int level, LogAddressDTO logObj, Object previousObj, Object currentObj) {
        // Validate các Object được truyền vào
        String address = (logObj == null ? null : new TransferDataUtil<LogAddressDTO>().toJson(logObj));
        String previousValue = (previousObj == null ? null : new TransferDataUtil<T>().toJson(previousObj));
        String currentValue = (currentObj == null ? null : new TransferDataUtil<T>().toJson(currentObj));

        // Ghi log
        LogBean log = new LogBean();
        log.setIp(ip);
        log.setNational(national);
        log.setLevel(level);
        // Chuyển đổi các Object sang JSON
        log.setAddress(address);
        log.setPreviousValue(previousValue);
        log.setCurrentValue(currentValue);
        log.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        logDAO.create(log);
    }

    private String makeContentName(String function, String state) {
        String name = function + "-" + state;
        return logContentBundle.getString(name);
    }
}
