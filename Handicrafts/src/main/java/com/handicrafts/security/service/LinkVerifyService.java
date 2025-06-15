package com.handicrafts.security.service;

import com.ltw.bean.UserBean;
import com.ltw.dao.UserDAO;
import com.ltw.util.EncryptPasswordUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkVerifyService {
    private final UserDAO userDAO = new UserDAO();

    // Service lưu verifiedCode xuống database
    public void saveNewCodeByEmail(String email, String verifiedCode) {
        userDAO.saveNewCodeByEmail(email, verifiedCode);
    }

    // Service tìm id theo email
    public int findIdByEmail(String email) {
        return userDAO.findIdByEmail(email);
    }

    // Service kiểm tra xem Email có để trống không
    public boolean isBlankInput(String input) {
        return input == null || input.trim().isEmpty();
    }

    // Service Kiểm tra tính hợp lệ của email
    public boolean isValidEmail(String email) {
        // Regex để kiểm tra email
        // ?: Không ghi nhớ kết quả
        String emailRegex = "^[a-zA-Z0-9_-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Service kiểm tra xem trong database đã tồn tại email được truyền vào hay chưa
    public boolean isExistEmail(String email) {
        // Nếu để trống thì trả luôn về false
        if (isBlankInput(email)) {
            return false;
        }
        int id = userDAO.findIdByEmail(email);
        return id != -1;
    }

    // Service kiểm tra xem tài khoản đã được active (status = 1) hay chưa
    public boolean isActiveAccount(String email) {
        return userDAO.isActiveAccount(email);
    }


    // Service kiểm tra verifiedCode
    public boolean isCorrectVerifiedCode(String email, String verifiedCode) {
        String emailQuery = userDAO.checkVerifiedCode(verifiedCode);
        // Nếu tìm thấy verifiedCode và id query được trùng với id được gửi từ Servlet => Trả vè true
        return email.equals(emailQuery);
    }

    // Service kiểm tra xem input có chứa khoảng trống không
    public boolean containsSpace(String input) {
        return input.contains(" ");
    }

    // Service kiểm tra độ dài của mật khẩu
    public boolean isLengthEnough(String password) {
        return password.length() >= 6;
    }

    // Service lưu mật khẩu mới vào trong database
    public int saveRenewPasswordByEmail(String email, String password) {
        // Hashing mật khẩu trước khi lưu
        String hashedPassword = EncryptPasswordUtil.encryptPassword(password);
        return userDAO.saveRenewPasswordByEmail(email, hashedPassword);
    }

    // Lưu key vào user
    public void saveKeyByEmail(String email, String key) {
        userDAO.saveKeyByEmail(email, key);
    }

    // Kiểm tra key
    public boolean isCorrectKey(String email, String key) {
        return userDAO.isCorrectKey(email, key);
    }

    public void setEmptyKey(String email) {
        userDAO.setEmptyKey(email);
    }

    public UserBean findUserByEmail(String email) {
        return userDAO.findUserByEmail(email);
    }
}
