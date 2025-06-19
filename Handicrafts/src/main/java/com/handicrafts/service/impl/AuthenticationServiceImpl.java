package com.handicrafts.service.impl;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.oauth2.CustomOAuth2User;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.service.AuthenticationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    // Bỏ @Autowired vì Spring tự động inject khi chỉ có một constructor
    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserEntity processOAuthLogin(CustomOAuth2User userInfo) {
        UserEntity existingUser = userRepository.findByEmail(userInfo.getEmail());

        if (existingUser == null) {
            // Tạo user mới từ thông tin OAuth
            UserEntity newUser = new UserEntity();
            newUser.setEmail(userInfo.getEmail());
            newUser.setStatus(1); // Mặc định active

            return userRepository.save(newUser);
        }

        // Chuyển đổi từ UserDTO sang UserEntity trước khi trả về
        return convertToEntity(existingUser);
    }

    @Transactional
    @Override
    public String checkOAuthAccount(String email) {
        UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            return "error";
        }

        if (user.getAddressProvince() != null && !user.getAddressProvince().isEmpty()) {
            return "oAuth";
        } else {
            return "notOAuth";
        }
    }

    // Thêm phương thức chuyển đổi từ DTO sang Entity
    private UserEntity convertToEntity(UserEntity userDTO) {
        UserEntity entity = new UserEntity();
        entity.setId(userDTO.getId());
        entity.setEmail(userDTO.getEmail());

        // Sao chép các thuộc tính khác nếu có
        if (userDTO.getAddressProvince() != null) {
            entity.setAddressProvince(userDTO.getAddressProvince());
        }
        // Thêm các thuộc tính khác tùy theo cấu trúc của UserEntity và UserDTO

        return entity;
    }
}
