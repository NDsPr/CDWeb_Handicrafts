package com.handicrafts.security.service;

import com.handicrafts.dto.LogDTO;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.dto.LogAddressDTO;
import com.handicrafts.util.SessionUtil;
import com.handicrafts.util.TransferDataUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ResourceBundle;

@Service
public class LogService<T> {

    /**
     *
     */
    @Autowired
    private ILogRepository logRepository;

    private ResourceBundle logContentBundle = ResourceBundle.getBundle("log-content");

    public void log(HttpServletRequest request, String function, String state, int level, T previousInfo, T currentInfo) {
        UserDTO userModify = (UserDTO) SessionUtil.getInstance().getValue(request, "user");
        String ip = request.getRemoteAddr();
        String national = "";
        String content = makeContentName(function, state);
        int id = (userModify == null) ? -1 : userModify.getId(); // Đúng là getUserID()
        LogAddressDTO address = new LogAddressDTO(function, id, content);
        createLog(ip, national, level, address, previousInfo, currentInfo);
    }

    private void createLog(String ip, String national, int level, LogAddressDTO logObj, T previousObj, T currentObj) {
        String address = (logObj == null ? null : new TransferDataUtil<LogAddressDTO>().toJson(logObj));
        String previousValue = (previousObj == null ? null : new TransferDataUtil<T>().toJson(previousObj));
        String currentValue = (currentObj == null ? null : new TransferDataUtil<T>().toJson(currentObj));

        LogDTO log = new LogDTO();
        log.setIp(ip);
        log.setNational(national);
        log.setLevel(level);
        log.setAddress(address);
        log.setPreviousValue(previousValue);
        log.setCurrentValue(currentValue);
        log.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        logRepository.create(log);
    }

    private String makeContentName(String function, String state) {
        String name = function + "-" + state;
        return logContentBundle.getString(name);
    }
}
