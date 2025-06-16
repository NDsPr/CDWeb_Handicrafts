package com.handicrafts.security.service;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.util.EncryptPasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LinkVerifyService {

    @Autowired
    private UserRepository userRepository;

    // Save verified code to DB
    public void saveNewCodeByEmail(String email, String verifiedCode) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        userOpt.ifPresent(user -> {
            user.setVerifiedCode(verifiedCode);
            userRepository.save(user);
        });
    }
//
//    public int findIdByEmail(String email) {
//        return userRepository.findIdByEmail(email).orElse(-1);
//    }

    public boolean isBlankInput(String input) {
        return input == null || input.trim().isEmpty();
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isExistEmail(String email) {
        if (isBlankInput(email)) return false;
        return userRepository.findByEmail(email).isPresent();
    }

//    public boolean isActiveAccount(String email) {
//        return userRepository.isActiveAccount(email);
//    }

    public boolean isCorrectVerifiedCode(String email, String verifiedCode) {
        String emailFromDb = String.valueOf(userRepository.findByVerifiedCode(verifiedCode));
        return email.equals(emailFromDb);
    }

    public boolean containsSpace(String input) {
        return input.contains(" ");
    }

    public boolean isLengthEnough(String password) {
        return password.length() >= 6;
    }

    public int saveRenewPasswordByEmail(String email, String password) {
        String hashedPassword = EncryptPasswordUtil.encryptPassword(password);
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            user.setPassword(hashedPassword);
            userRepository.save(user);
            return 1;
        }
        return 0;
    }

    public void saveKeyByEmail(String email, String key) {
        userRepository.saveKeyByEmail(email, key);
    }

//    public boolean isCorrectKey(String email, String key) {
//        return userRepository.isCorrectKey(email, key);
//    }

    public void setEmptyKey(String email) {
        userRepository.setEmptyKey(email);
    }

    public UserDTO findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(user.getId());
                    dto.setEmail(user.getEmail());
//                    dto.setUsername(user.g());
//                    dto.setFullname(user.getFullname());
                    return dto;
                }).orElse(null);
    }
}
