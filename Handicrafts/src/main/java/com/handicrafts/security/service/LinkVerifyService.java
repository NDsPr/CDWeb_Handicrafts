package com.handicrafts.security.service;

import com.handicrafts.repository.impl.UserRepository;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.util.EncryptPasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LinkVerifyService {

    @Autowired
    private UserRepository UserRepository;

    /**
     * Lưu verifiedCode xuống database
     */
    public void saveNewCodeByEmail(String email, String verifiedCode) {
        UserRepository.saveNewCodeByEmail(email, verifiedCode);
    }

    /**
     * Tìm id theo email
     */
    public int findIdByEmail(String email) {
        return UserRepository.findIdByEmail(email);
    }

    /**
     * Kiểm tra xem Email có để trống không
     */
    public boolean isBlankInput(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * Kiểm tra tính hợp lệ của email
     */
    public boolean isValidEmail(String email) {
        // Regex để kiểm tra email
        // ?: Không ghi nhớ kết quả
        String emailRegex = "^[a-zA-Z0-9_-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Kiểm tra xem trong database đã tồn tại email được truyền vào hay chưa
     */
    public boolean isExistEmail(String email) {
        // Nếu để trống thì trả luôn về false
        if (isBlankInput(email)) {
            return false;
        }
        int id = UserRepository.findIdByEmail(email);
        return id != -1;
    }

    /**
     * Kiểm tra xem tài khoản đã được active (status = 1) hay chưa
     */
    public boolean isActiveAccount(String email) {
        return UserRepository.isActiveAccount(email);
    }

    /**
     * Kiểm tra verifiedCode
     */
    public boolean isCorrectVerifiedCode(String email, String verifiedCode) {
        String emailQuery = UserRepository.checkVerifiedCode(verifiedCode);
        // Nếu tìm thấy verifiedCode và id query được trùng với id được gửi từ Servlet => Trả vè true
        return email.equals(emailQuery);
    }

    /**
     * Kiểm tra xem input có chứa khoảng trống không
     */
    public boolean containsSpace(String input) {
        return input.contains(" ");
    }

    /**
     * Kiểm tra độ dài của mật khẩu
     */
    public boolean isLengthEnough(String password) {
        return password.length() >= 6;
    }

    /**
     * Lưu mật khẩu mới vào trong database
     */
    public int saveRenewPasswordByEmail(String email, String password) {
        // Hashing mật khẩu trước khi lưu
        String hashedPassword = EncryptPasswordUtil.encryptPassword(password);
        return UserRepository.saveRenewPasswordByEmail(email, hashedPassword);
    }

    /**
     * Lưu key vào user
     */
    public void saveKeyByEmail(String email, String key) {
        UserRepository.saveKeyByEmail(email, key);
    }

    /**
     * Kiểm tra key
     */
    public boolean isCorrectKey(String email, String key) {
        return UserRepository.isCorrectKey(email, key);
    }

    /**
     * Đặt key thành rỗng
     */
    public void setEmptyKey(String email) {
        UserRepository.setEmptyKey(email);
    }

    /**
     * Tìm user theo email
     */
    public UserDTO findUserByEmail(String email) {
        return UserRepository.findUserByEmail(email);
    }
}
