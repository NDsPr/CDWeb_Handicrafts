package com.handicrafts.security.service;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.util.EncryptPasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CodeVerifyService {

    @Autowired
    private UserRepository userRepository;

    public int register(UserDTO user) {
        return userRepository.createInRegister(user);
    }

    public boolean isBlankEmail(String email) {
        return email == null || email.isEmpty();
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isExistEmail(String email) {
        if (isBlankEmail(email)) {
            return false;
        }
        return userRepository.findIdByEmail(email) != -1;
    }

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
            char randomCharacter = characters.charAt(random.nextInt(characters.length()));
            verifiedCode.append(randomCharacter);
        }
        return verifiedCode.toString();
    }

    public void setNewCodeByEmail(String email, String verifiedCode) {
        userRepository.saveNewCodeByEmail(email, verifiedCode);
    }

    public boolean isBlankVerification(String verifyInput) {
        return (verifyInput == null || verifyInput.isEmpty());
    }

    public boolean isCorrectLength(String verifyInput) {
        if (verifyInput == null || verifyInput.isEmpty()) {
            return false;
        }
        return verifyInput.length() == 8;
    }

    public boolean isCorrectVerifiedCode(String email, String verifiedCode) {
        String emailQuery = userRepository.checkVerifiedCode(verifiedCode);
        return emailQuery.equals(email);
    }

    public void activeAccount(String email) {
        userRepository.activeAccount(email);
    }

    public boolean containsSpace(String password) {
        return password.contains(" ");
    }

    public boolean isLengthEnough(String password) {
        return password.length() >= 6;
    }

    public void setEmptyCode(String email) {
        userRepository.setEmptyCode(email);
    }

    public boolean isValidLogin(String email, String password) {
        String hashedPassword = userRepository.getHashedPasswordByEmail(email);
        if (hashedPassword == null) {
            return false;
        }
        return EncryptPasswordUtil.checkPassword(password, hashedPassword);
    }

    public boolean isActive(String email) {
        return userRepository.findActiveAccountByEmail(email) != -1;
    }

    public UserDTO findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public int findIdByEmail(String email) {
        return userRepository.findIdByEmail(email);
    }
}