package com.handicrafts.security.service;

import com.ltw.bean.UserBean;
import com.ltw.dao.UserDAO;
import com.ltw.util.EncryptPasswordUtil;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeVerifyService {
    private final UserDAO userDAO = new UserDAO();

    // Service thêm một user vào trong database
    public int register(UserBean user) {
        return userDAO.createInRegister(user);
    }

    // Service kiểm tra xem Email có để trống không
    public boolean isBlankEmail(String email) {
        return email == null || email.isEmpty();
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
        if (isBlankEmail(email)) {
            return false;
        }
        return userDAO.findIdByEmail(email) != -1;
    }

    // Service kiểm tra xem Mật khẩu và Nhập lại mật khẩu có bị trống không
    public boolean isBlankPassword(String password, String retypePassword) {
        return (password == null || password.isEmpty()) || (retypePassword == null || retypePassword.isEmpty());
    }

    public boolean isSamePassword(String password, String retypePassword) {
        if (isBlankPassword(password, retypePassword)) {
            return false;
        }
        return password.equals(retypePassword);
    }

    public String generateVerifiedCode() {
        int codeLength = 8;
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder verifiedCode = new StringBuilder();

        for (int i = 0; i < codeLength; i++) {
            // Lấy ngẫu nhiên 1 ký tự trong dãy characters
            char randomCharacter = characters.charAt(random.nextInt(characters.length()));
            verifiedCode.append(randomCharacter);
        }
        return verifiedCode.toString();
    }

    public void setNewCodeByEmail(String email, String verifiedCode) {
        userDAO.saveNewCodeByEmail(email, verifiedCode);
    }

    // Service kiểm tra xem verify input có để trống không
    public boolean isBlankVerification(String verifyInput) {
        return (verifyInput == null || verifyInput.isEmpty());
    }

    // Service kiểm tra xem verify input có đủ 8 ký tự không
    public boolean isCorrectLength(String verifyInput) {
        if (verifyInput == null || verifyInput.isEmpty()) {
            return false;
        }
        return verifyInput.length() == 8;
    }

    // Service kiểm tra verifiedCode
    public boolean isCorrectVerifiedCode(String email, String verifiedCode) {
        String emailQuery = userDAO.checkVerifiedCode(verifiedCode);
        return emailQuery.equals(email);
    }

    // Service active tài khoản
    public void activeAccount(String email) {
        userDAO.activeAccount(email);
    }

    // Service kiểm tra xem có khoảng trống trong password không
    public boolean containsSpace(String password) {
        return password.contains(" ");
    }

    // Service kiểm tra độ dài mật khẩu
    public boolean isLengthEnough(String password) {
        return password.length() >= 6;
    }

    // Service set một verify code rỗng cho user sau khi đã xác thực thành công
    public void setEmptyCode(String email) {
        userDAO.setEmptyCode(email);
    }

    // Service kiểm tra xem email và password có hợp lệ không
    public boolean isValidLogin(String email, String password) {
       String hashedPassword = userDAO.getHashedPasswordByEmail(email);
       if (hashedPassword == null) {
           return false;
       }
       return EncryptPasswordUtil.checkPassword(password, hashedPassword);
    }

    // Service kiểm tra xem tài khoản đã active chưa
    public boolean isActive(String email) {
        return userDAO.findActiveAccountByEmail(email) != -1;
    }

    // Service lấy lên thông tin user dựa vào email để đưa vào Session
    public UserBean findUserByEmail(String email) {
        return userDAO.findUserByEmail(email);
    }

    // Service lấy lên id từ email
    public int findIdByEmail(String email) {
        return userDAO.findIdByEmail(email);
    }
}
