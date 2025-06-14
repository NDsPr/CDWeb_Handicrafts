package com.handicrafts.repository;

import com.handicrafts.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    // Tìm user theo username và đã được kích hoạt
    Optional<UserEntity> findByUsernameAndIsEnableIsTrue(String username);

    // Tìm user theo email
    Optional<UserEntity> findByEmail(String email);

    // Tìm user theo email và đã được kích hoạt
    Optional<UserEntity> findByEmailAndIsEnableIsTrue(String email);

    // Tìm user theo email và đã được kích hoạt và status là true
    Optional<UserEntity> findByEmailAndIsEnableIsTrueAndStatusIsTrue(String email);

    // Tìm user theo email (không phân biệt hoa thường)
    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<UserEntity> findByEmailIgnoreCase(@Param("email") String email);

    // Tìm user theo email, isEnable và status
    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.email) = LOWER(:email) AND u.isEnable = :isEnable AND u.status = :status")
    Optional<UserEntity> findByEmailIgnoreCaseAndIsEnableAndStatus(
            @Param("email") String email,
            @Param("isEnable") boolean isEnable,
            @Param("status") boolean status);

    // Tìm user theo userID
    Optional<UserEntity> findByUserID(int userID);
    /**
     * Tìm người dùng theo tên đăng nhập
     * @param username Tên đăng nhập cần tìm
     * @return Optional chứa UserEntity nếu tìm thấy, ngược lại là Optional rỗng
     */
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmailAndIsEnableTrue(String email);

    // Tìm user theo token xác nhận
    Optional<UserEntity> findByConfirmToken(String confirmToken);

    // Lấy confirm token theo userID
    @Query("SELECT u.confirmToken FROM UserEntity u WHERE u.userID = :userID")
    String getConfirmTokenById(@Param("userID") int userID);

    // Kiểm tra username đã tồn tại chưa
    boolean existsByUsername(String username);

    // Kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);

    // Cập nhật thông tin người dùng
    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.username = :username, u.fullname = :fullname, u.birthdate = :birthdate, " +
            "u.gender = :gender, u.phone = :phone, u.updatedAt = :updatedAt WHERE u.userID = :userID")
    void updateUser(
            @Param("userID") int userID,
            @Param("username") String username,
            @Param("fullname") String fullname,
            @Param("birthdate") LocalDate birthdate,
            @Param("gender") boolean gender,
            @Param("phone") String phone,
            @Param("updatedAt") LocalDate updatedAt);

    // Cập nhật mật khẩu
    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.password = :password WHERE u.userID = :userID")
    void updatePass(@Param("password") String password, @Param("userID") int userID);

    // Xóa user theo userID
    @Modifying
    @Transactional
    @Query("DELETE FROM UserEntity u WHERE u.userID = :userID")
    void deleteByUserID(@Param("userID") int userID);

    // Cập nhật trạng thái kích hoạt
    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.isEnable = :isEnable WHERE u.userID = :userID")
    void updateEnableStatus(@Param("isEnable") boolean isEnable, @Param("userID") int userID);

    // Cập nhật token xác nhận
    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.confirmToken = :confirmToken WHERE u.userID = :userID")
    void updateConfirmToken(@Param("confirmToken") String confirmToken, @Param("userID") int userID);
}
