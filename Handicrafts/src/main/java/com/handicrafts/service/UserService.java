package com.handicrafts.service;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    // Tạo mới người dùng
    UserDTO createUser(UserDTO userDTO);

    // Cập nhật thông tin người dùng
    UserDTO updateUser(UserDTO userDTO);
    Optional<UserDTO> findById(Integer id);

    // Lấy thông tin người dùng theo ID
    Optional<UserDTO> getUserById(Integer id);

    // Lấy thông tin người dùng theo username
    Optional<UserDTO> getUserByUsername(String username);

    // Lấy thông tin người dùng theo email
    Optional<UserDTO> getUserByEmail(String email);

    // Lấy danh sách tất cả người dùng
    List<UserDTO> getAllUsers();

    // Xóa người dùng theo ID
    void deleteUser(Integer id);

    // Kích hoạt tài khoản người dùng
    void activateUser(String activationToken);

    // Đổi mật khẩu
    void changePassword(Integer userId, String oldPassword, String newPassword);

    // Khôi phục mật khẩu
    void resetPassword(String email);

    // Kiểm tra username đã tồn tại chưa
    boolean existsByUsername(String username);

    // Kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);

    UserDTO convertToDTO(UserEntity user);
}
