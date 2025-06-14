package com.handicrafts.service.impl;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.RoleEntity;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.RoleRepository;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsernameAndIsEnableIsTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return (UserDetails) user;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        try {
            // Kiểm tra nếu email đã tồn tại
            if (existsByEmail(userDTO.getEmail())) {
                return null;
            }

            // Mã hóa mật khẩu nếu có
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }

            // Tạo token xác nhận
            String confirmToken = generateConfirmToken();
            userDTO.setConfirmToken(confirmToken);

            // Thiết lập các giá trị mặc định
            userDTO.setCreatedAt(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            userDTO.setIsEnable(false);
            userDTO.setStatus(true);
            userDTO.setProvider("LOCAL");

            // Gán role USER cho người dùng mới
            Optional<RoleEntity> userRoleOpt = Optional.ofNullable(roleRepository.findByName("ROLE_USER"));
            if (!userRoleOpt.isPresent()) {
                throw new RuntimeException("Role USER not found");
            }
            RoleEntity userRole = userRoleOpt.get();

            // Đảm bảo rằng trường roles trong UserDTO được khởi tạo
            if (userDTO.getRoles() == null) {
                userDTO.setRoles(new ArrayList<>());
            }
            userDTO.getRoles().clear();  // Xóa bất kỳ role nào đã có
            userDTO.getRoles().add(userRole.getName());

            // Lưu user vào database
            UserEntity userEntity = convertToEntity(userDTO);
            UserEntity savedUser = userRepository.save(userEntity);

            // Gửi email xác nhận
            try {
                sendConfirmationEmail(userDTO.getEmail(), confirmToken);
            } catch (Exception e) {
                // Log lỗi nhưng không làm gián đoạn quá trình tạo người dùng
                System.err.println("Error sending confirmation email: " + e.getMessage());
                // Logger.error("Error sending confirmation email", e);
            }

            return convertToDTO(savedUser);
        } catch (Exception e) {
            // Log lỗi để debug
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }
    }

    private UserEntity convertToEntity(UserDTO userDTO) {
        UserEntity entity = new UserEntity();

        // Set các thông tin cơ bản
        entity.setEmail(userDTO.getEmail());
        entity.setPassword(userDTO.getPassword());

        // Sửa lại tên thuộc tính cho đúng - kiểm tra xem nên là fullName hay fullname
        entity.setFullname(userDTO.getFullName());

        // Thêm username nếu cần
        if (userDTO.getUsername() != null) {
            entity.setUsername(userDTO.getUsername());
        }

        // Set ngày tạo
        if (userDTO.getCreatedAt() != null) {
            entity.setCreatedAt(userDTO.getCreatedAt());
        }

        // Set trạng thái
        if (userDTO.getIsEnable() != null) {
            entity.setIsEnable(userDTO.getIsEnable());
        }

        if (userDTO.getStatus() != null) {
            entity.setStatus(userDTO.getStatus());
        }

        if (userDTO.getProvider() != null) {
            entity.setProvider(userDTO.getProvider());
        }

        // Chuyển đổi danh sách roles từ tên thành đối tượng RoleEntity
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            try {
                Set<RoleEntity> roles = new HashSet<>();
                for (String roleName : userDTO.getRoles()) {
                    Optional<RoleEntity> roleOpt = Optional.ofNullable(roleRepository.findByName(roleName));
                    if (roleOpt.isPresent()) {
                        roles.add(roleOpt.get());
                    } else {
                        throw new RuntimeException("Role not found: " + roleName);
                    }
                }
                entity.setRoles(roles);
            } catch (Exception e) {
                throw new RuntimeException("Error while processing roles: " + e.getMessage(), e);
            }
        }

        return entity;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        Optional<UserEntity> userOptional = userRepository.findById(userDTO.getId());
        if (!userOptional.isPresent()) {
            return null;
        }

        UserEntity existingUser = userOptional.get();

        // Cập nhật thông tin người dùng
        if (userDTO.getUsername() != null) {
            existingUser.setUsername(userDTO.getUsername());
        }
        if (userDTO.getFullName() != null) {
            existingUser.setFullname(userDTO.getFullName());
        }
        if (userDTO.getPhone() != null) {
            existingUser.setPhone(userDTO.getPhone());
        }
        if (userDTO.getBirthDate() != null) {
            existingUser.setBirthdate(userDTO.getBirthDate());
        }
        if (userDTO.getGender() != null) {
            existingUser.setGender(userDTO.getGender());
        }

        existingUser.setUpdatedAt(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        UserEntity updatedUser = userRepository.save(existingUser);
        return convertToDTO(updatedUser);
    }

    @Override
    public Optional<UserDTO> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDTO> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public void activateUser(String activationToken) {
        UserEntity user = userRepository.findByConfirmToken(activationToken)
                .orElseThrow(() -> new RuntimeException("Invalid activation token"));

        user.setIsEnable(true);
        userRepository.save(user);
    }

    @Override
    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void resetPassword(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Tạo mật khẩu ngẫu nhiên
        String randomPassword = generateRandomPassword();

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(randomPassword));
        userRepository.save(user);

        // Gửi email với mật khẩu mới
        sendPasswordResetEmail(email, randomPassword);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    public UserDTO convertToDTO(UserEntity user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullname()); // Sửa: getFullname() thay vì getFullName()
        userDTO.setPhone(user.getPhone());
        userDTO.setBirthDate(user.getBirthdate()); // Sửa: getBirthdate() thay vì getBirthDate()
        userDTO.setGender(user.getGender());

        // Không thấy trường enabled trong entity, có thể dựa vào status
        // Hoặc nếu có trường is_enable nhưng không hiển thị trong hình
        userDTO.setIsEnable(user.getIsEnable()); // Nếu có trường isEnable

        userDTO.setStatus(user.getStatus()); // Sửa: getStatus() thay vì isStatus()
        userDTO.setProvider(user.getProvider());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());

        // Không bao gồm mật khẩu trong DTO
        userDTO.setPassword(null);

        // Chuyển đổi roles
        if (user.getRoles() != null) {
            userDTO.setRoles(user.getRoles().stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toList()));
        }

        return userDTO;
    }

//    @Override
//    public UserEntity convertToEntity(UserDTO userDTO) {
//        if (userDTO == null) {
//            return null;
//        }
//
//        UserEntity user = new UserEntity();
//
//        // Nếu là cập nhật user đã tồn tại
//        if (userDTO.getId() != null) {
//            userRepository.findById(userDTO.getId())
//                    .ifPresent(existingUser -> {
//                        // Giữ nguyên các trường không thay đổi
//                        user.setCreatedAt(existingUser.getCreatedAt());
//                        user.setPassword(existingUser.getPassword());
//                        user.setProvider(existingUser.getProvider());
//                        user.setIsEnable(existingUser.getIsEnable());
//                    });
//        }
//
//        user.setId(userDTO.getId());
//        user.setUsername(userDTO.getUsername());
//        user.setEmail(userDTO.getEmail());
//        user.setFullname(userDTO.getFullName());
//        user.setPhone(userDTO.getPhone());
//        user.setBirthdate(userDTO.getBirthDate());
//        user.setGender(userDTO.getGender());
//        user.setStatus(userDTO.getStatus());
//
//        // Cập nhật mật khẩu nếu có
//        if (userDTO.getPassword() != null) {
//            user.setPassword(userDTO.getPassword());
//        }
//
//        // Cập nhật ngày tạo/cập nhật
//        if (userDTO.getCreatedAt() != null) {
//            user.setCreatedAt(userDTO.getCreatedAt());
//        }
//        user.setUpdatedAt(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//
//        // Chuyển đổi roles
//        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
//            Set<RoleEntity> roles = userDTO.getRoles().stream()
//                    .map(roleName -> roleRepository.findByName(roleName)
//                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
//                    .collect(Collectors.toSet());
//            user.setRoles(roles);
//        }
//
//        return user;
//    }

//    @Override
//    public UserDTO processSocialLogin(String email, String name, String socialId, String socialType) {
//        // Kiểm tra nếu user đã tồn tại với email này
//        Optional<UserEntity> existingUserOpt = userRepository.findByEmail(email);
//
//        if (existingUserOpt.isPresent()) {
//            UserEntity existingUser = existingUserOpt.get();
//            // Nếu user đã tồn tại, trả về thông tin user
//            return convertToDTO(existingUser);
//        } else {
//            // Tạo user mới từ thông tin social login
//            UserDTO newUserDTO = new UserDTO();
//            newUserDTO.setEmail(email);
//            newUserDTO.setFullName(name);
//            newUserDTO.setUsername(email.split("@")[0] + "_" + socialType.toLowerCase());
//            newUserDTO.setIsEnable(true);
//            newUserDTO.setStatus(true);
//            newUserDTO.setProvider(socialType);
//            newUserDTO.setCreatedAt(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())));
//
//            // Gán role USER cho người dùng mới
//            RoleEntity userRole = roleRepository.findByName("ROLE_USER")
//                    .orElseThrow(() -> new RuntimeException("Role USER not found"));
//            newUserDTO.setRoles(Collections.singletonList(userRole.getName()));
//
//            // Lưu user vào database
//            UserEntity savedUser = userRepository.save(convertToEntity(newUserDTO));
//            return convertToDTO(savedUser);
//        }
//    }

    // Helper methods
    private String generateConfirmToken() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private String generateRandomPassword() {
        return String.format("%08d", new Random().nextInt(99999999));
    }

    private void sendConfirmationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Handicrafts - Xác nhận email để tạo tài khoản");
        message.setFrom("handicrafts@gmail.com");
        message.setText("Code xác nhận mail của bạn là: " + token + ". Vui lòng nhập code để xác nhận email");
        mailSender.send(message);
    }

    private void sendPasswordResetEmail(String email, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Handicrafts - Xác nhận đặt lại mật khẩu");
        message.setFrom("handicrafts@gmail.com");
        message.setText("Chúng tôi đã tạo mật khẩu mới cho tài khoản của bạn, mật khẩu là: " + newPassword +
                ". Để bảo mật tài khoản vui lòng đăng nhập và thay đổi mật khẩu");
        mailSender.send(message);
    }
}
