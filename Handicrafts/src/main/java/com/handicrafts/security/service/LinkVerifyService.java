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
import java.util.Collections;
import java.sql.Timestamp;
import java.util.Date;

@Service
public class LinkVerifyService {

    @Autowired
    private UserRepository userRepository;

    // Save verified code to DB
    public void saveNewCodeByEmail(String email, String verifiedCode) {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            user.setVerifiedCode(verifiedCode);
            userRepository.save(user);
        }
    }

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
        return userRepository.findByEmail(email) != null;
    }

    public boolean isCorrectVerifiedCode(String email, String verifiedCode) {
        UserEntity user = userRepository.findByVerifiedCode(verifiedCode);
        return user != null && email.equals(user.getEmail());
    }

    public boolean containsSpace(String input) {
        return input.contains(" ");
    }

    public boolean isLengthEnough(String password) {
        return password.length() >= 6;
    }

    public int saveRenewPasswordByEmail(String email, String password) {
        String hashedPassword = EncryptPasswordUtil.encryptPassword(password);
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(hashedPassword);
            user.setModifiedDate(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            return 1;
        }
        return 0;
    }

    public void saveKeyByEmail(String email, String key) {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            user.setChangePwHash(key);
            user.setExpiredTime(new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // 24 giờ
            userRepository.save(user);
        }
    }

    public void setEmptyKey(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            user.setChangePwHash(null);
            user.setExpiredTime(null);
            userRepository.save(user);
        }
    }

    public UserDTO findUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());

            // Tạo fullName từ firstName và lastName
            String fullName = "";
            if (user.getFirstName() != null) {
                fullName += user.getFirstName();
            }
            if (user.getLastName() != null) {
                if (!fullName.isEmpty()) {
                    fullName += " ";
                }
                fullName += user.getLastName();
            }
            dto.setFullName(fullName);

            // Chuyển đổi status từ Integer sang Boolean
            dto.setStatus(user.getStatus() == 1);

            // Chuyển đổi thời gian
            if (user.getCreatedDate() != null) {
                dto.setCreatedAt(new Date(user.getCreatedDate().getTime()));
            }
            if (user.getModifiedDate() != null) {
                dto.setUpdatedAt(new Date(user.getModifiedDate().getTime()));
            }

            // Thiết lập provider
            dto.setProvider(user.getViaOAuth());

            // Nếu có role entity, thêm vào DTO
            if (user.getRole() != null) {
                dto.setRoles(Collections.singletonList(user.getRole().getName()));
            }

            return dto;
        }
        return null;
    }
    /**
     * Kiểm tra xem key đổi mật khẩu có chính xác với email không
     * @param email Email của người dùng
     * @param key Mã hash dùng để xác thực việc đổi mật khẩu
     * @return true nếu key hợp lệ và chưa hết hạn, false nếu ngược lại
     */
    public boolean isCorrectKey(String email, String key) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null || user.getChangePwHash() == null) {
            return false;
        }

        // Kiểm tra key có khớp không
        boolean isKeyMatch = user.getChangePwHash().equals(key);

        // Kiểm tra key có hết hạn chưa
        boolean isKeyExpired = false;
        if (user.getExpiredTime() != null) {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            isKeyExpired = currentTime.after(user.getExpiredTime());
        }

        // Key hợp lệ khi khớp và chưa hết hạn
        return isKeyMatch && !isKeyExpired;
    }
    /**
     * Kiểm tra xem tài khoản có đang active hay không
     * @param email Email của người dùng
     * @return true nếu tài khoản đã active (status = 1), false nếu ngược lại
     */
    public boolean isActiveAccount(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }

        // Kiểm tra status của tài khoản, status = 1 là đã active
        return user.getStatus() == 1;
    }

}
