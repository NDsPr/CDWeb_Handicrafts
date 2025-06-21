package com.handicrafts.service.impl;

import com.handicrafts.config.PasswordEncoderConfig;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.RoleRepository;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.service.IUserService;
import com.handicrafts.util.EncryptPasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements IUserService {
    @Autowired
    private PasswordEncoder passwordEncoder; // Đúng: inject bean PasswordEncoder

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmailIgnoreCaseAndStatusAndVerifiedCodeIsNull(email, 1);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email + " không tồn tại trong database");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // Tìm tất cả role name của user
        List<String> roleNames = roleRepository.findRoleNameByUserId(userEntity.getId());

        // Tạo grantedAuthority với roleNames tương ứng
        for (String name : roleNames) {
            grantedAuthorities.add(new SimpleGrantedAuthority(name));
        }

        // Tạo đối tượng userdetail
        return User.withUsername(userEntity.getEmail())
                .password(userEntity.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        if (existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        UserEntity userEntity = convertToEntity(userDTO);
        // Sử dụng EncryptPasswordUtil thay vì passwordEncoder
        userEntity.setPassword(EncryptPasswordUtil.encryptPassword(userDTO.getPassword()));
        userEntity.setStatus(1); // Mặc định là active

        userEntity.setRoleId(1);
        userEntity.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        UserEntity savedUser = userRepository.save(userEntity);
        return convertToDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        Optional<UserEntity> existingUser = userRepository.findById(userDTO.getId());

        if (existingUser.isPresent()) {
            UserEntity userEntity = existingUser.get();

            // Cập nhật thông tin cơ bản
            if (userDTO.getFullName() != null) {
                String[] nameParts = userDTO.getFullName().split(" ", 2);
                userEntity.setFirstName(nameParts.length > 0 ? nameParts[0] : "");
                userEntity.setLastName(nameParts.length > 1 ? nameParts[1] : "");
            }

            // Cập nhật địa chỉ nếu có
            if (userDTO.getProvider() != null) userEntity.setAddressProvince(userDTO.getProvider());

            userEntity.setModifiedDate(new Timestamp(System.currentTimeMillis()));

            UserEntity updatedUser = userRepository.save(userEntity);
            return convertToDTO(updatedUser);
        }

        throw new RuntimeException("Không tìm thấy người dùng với ID: " + userDTO.getId());
    }

    @Override
    public Optional<UserDTO> findById(Integer id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public Optional<UserDTO> getUserById(Integer id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        // Vì entity không có username, nên có thể dùng email thay thế
        return Optional.ofNullable(findByEmail(username));
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        return Optional.ofNullable(findByEmail(email));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activateUser(String verifiedCode) {
        UserEntity user = userRepository.findByVerifiedCode(verifiedCode);

        if (user != null) {
            user.setVerifiedCode(null); // Xóa mã xác thực
            user.setStatus(1); // Kích hoạt tài khoản
            userRepository.save(user);
        } else {
            throw new RuntimeException("Mã xác thực không hợp lệ");
        }
    }

    @Override
    @Transactional
    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();

            // Sử dụng EncryptPasswordUtil để kiểm tra mật khẩu
            if (EncryptPasswordUtil.checkPassword(oldPassword, user.getPassword())) {
                // Mã hóa mật khẩu mới
                user.setPassword(EncryptPasswordUtil.encryptPassword(newPassword));
                user.setModifiedDate(new Timestamp(System.currentTimeMillis()));
                userRepository.save(user);
            } else {
                throw new RuntimeException("Mật khẩu cũ không chính xác");
            }
        } else {
            throw new RuntimeException("Không tìm thấy người dùng");
        }
    }

    @Override
    @Transactional
    public void resetPassword(String email) {
        UserEntity user = userRepository.findByEmail(email);

        if (user != null) {
            // Tạo mã xác thực để reset password
            String resetToken = generateVerifiedCode();
            Timestamp expiredTime = new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000); // 24 giờ

            user.setChangePwHash(resetToken);
            user.setExpiredTime(expiredTime);
            userRepository.save(user);

            // Gửi email reset password (implement riêng)
            // sendResetPasswordEmail(user.getEmail(), resetToken);
        } else {
            throw new RuntimeException("Email không tồn tại trong hệ thống");
        }
    }

    // Sử dụng phương thức tạo mã xác thực từ CodeVerifyService
    private String generateVerifiedCode() {
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

    @Override
    public boolean existsByUsername(String username) {
        // Trong hệ thống này, username chính là email của người dùng
        return userRepository.existsByEmail(username);
    }


    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDTO convertToDTO(UserEntity user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());

        // Ghép firstName và lastName thành fullName
        String fullName = (user.getFirstName() != null ? user.getFirstName() : "") +
                (user.getLastName() != null ? " " + user.getLastName() : "");
        dto.setFullName(fullName.trim());

        // Set username (có thể dùng email)
        dto.setUsername(user.getEmail());

        // Set các trường khác
        dto.setStatus(user.getStatus() == 1);
        dto.setProvider(user.getViaOAuth());

        // Chuyển đổi Timestamp thành Date
        if (user.getCreatedDate() != null) {
            dto.setCreatedAt(new Date(user.getCreatedDate().getTime()));
        }
        if (user.getModifiedDate() != null) {
            dto.setUpdatedAt(new Date(user.getModifiedDate().getTime()));
        }

        // Lấy danh sách roles
        List<String> roles = roleRepository.findRoleNameByUserId(user.getId());
        dto.setRoles(roles);

        // Thêm các trường địa chỉ
        dto.setProvider(user.getAddressProvince());

        return dto;
    }

    @Override
    public UserEntity convertToEntity(UserDTO dto) {
        if (dto == null) return null;

        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());

        // Tách fullName thành firstName và lastName
        if (dto.getFullName() != null) {
            String[] nameParts = dto.getFullName().split(" ", 2);
            entity.setFirstName(nameParts.length > 0 ? nameParts[0] : "");
            entity.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        }

        // Set các trường khác
        if (dto.getPassword() != null) {
            // Mật khẩu sẽ được mã hóa ở service
            entity.setPassword(dto.getPassword());
        }

        entity.setStatus(dto.getStatus() ? 1 : 0);
        entity.setViaOAuth(dto.getProvider());

        // Chuyển đổi Date thành Timestamp
        if (dto.getCreatedAt() != null) {
            entity.setCreatedDate(new Timestamp(dto.getCreatedAt().getTime()));
        }
        if (dto.getUpdatedAt() != null) {
            entity.setModifiedDate(new Timestamp(dto.getUpdatedAt().getTime()));
        }

        // Thêm các trường địa chỉ
        entity.setAddressProvince(dto.getProvider());

        return entity;
    }

    @Override
    public UserDTO findByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        return convertToDTO(user);
    }

    @Override
    public UserDTO findByUsername(String username) {
        // Vì entity không có username, nên có thể dùng email thay thế
        return findByEmail(username);
    }

    @Override
    public UserDTO getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity entity = userRepository.findByEmail(auth.getName());

        if (entity == null) {
            return new UserDTO();
        }

        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    // Thêm phương thức kiểm tra đăng nhập hợp lệ dựa trên CodeVerifyService
    public boolean isValidLogin(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        return EncryptPasswordUtil.checkPassword(password, user.getPassword());
    }
    @Override
    public void processOAuthPostLogin(String email, String name, String provider) {
        // Kiểm tra xem email đã tồn tại trong database chưa
        boolean userExists = userRepository.existsByEmail(email);

        if (!userExists) {
            // Nếu chưa tồn tại, tạo mới tài khoản
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);

            // Phân tách tên thành firstName và lastName (nếu có thể)
            String[] nameParts = name.split(" ", 2);
            if (nameParts.length > 0) {
                newUser.setFirstName(nameParts[0]);
                if (nameParts.length > 1) {
                    newUser.setLastName(nameParts[1]);
                }
            }

            // Đặt một mật khẩu ngẫu nhiên vì người dùng sẽ đăng nhập bằng OAuth
            newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

            // Lưu nhà cung cấp OAuth vào trường viaOAuth
            newUser.setViaOAuth(provider);

            // Đặt các giá trị mặc định
            newUser.setStatus(1); // Giả sử 1 là trạng thái kích hoạt
            newUser.setRoleId(2); // Giả sử 2 là roleId cho ROLE_USER

            // Đặt thời gian tạo
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            newUser.setCreatedDate(currentTime);
            newUser.setModifiedDate(currentTime);

            // Lưu người dùng mới vào database
            userRepository.save(newUser);
        }
    }

    @Override
    public void changeInformation(UserDTO userDTO) {
        UserEntity entity = userRepository.findByEmail(userDTO.getEmail());
        if (entity == null) {
            throw new RuntimeException("Không tìm thấy người dùng");
        }

        // Cập nhật thông tin từ fullName
        if (userDTO.getFullName() != null) {
            String[] nameParts = userDTO.getFullName().split(" ", 2);
            if (nameParts.length > 0) {
                entity.setFirstName(nameParts[0]);
            }
            if (nameParts.length > 1) {
                entity.setLastName(nameParts[1]);
            }
        }

        // Cập nhật trạng thái nếu có
        if (userDTO.getStatus() != null) {
            entity.setStatus(userDTO.getStatus() ? 1 : 0);
        }

        // Cập nhật các thông tin địa chỉ nếu có
//        if (userDTO.getAddressLine() != null) entity.setAddressLine(userDTO.getAddressLine());
//        if (userDTO.getAddressWard() != null) entity.setAddressWard(userDTO.getAddressWard());
//        if (userDTO.getAddressDistrict() != null) entity.setAddressDistrict(userDTO.getAddressDistrict());
//        if (userDTO.getAddressProvince() != null) entity.setAddressProvince(userDTO.getAddressProvince());

        // Cập nhật provider nếu có (tương ứng với viaOAuth trong entity)
        if (userDTO.getProvider() != null) {
            entity.setViaOAuth(userDTO.getProvider());
        }

        // Cập nhật thời gian chỉnh sửa
        entity.setModifiedDate(new Timestamp(System.currentTimeMillis()));

        // Lưu thay đổi
        userRepository.save(entity);
    }



    @Override
    public boolean checkPass(String email, String oldPassword) {
        UserEntity entity = userRepository.findByEmail(email);
        if (entity == null) {
            throw new RuntimeException("Không tìm thấy người dùng");
        }

        // Kiểm tra mật khẩu cũ có khớp không
        return EncryptPasswordUtil.checkPassword(oldPassword, entity.getPassword());
    }

    @Override
    public void changePassword(String newPassword, String email) {
        UserEntity entity = userRepository.findByEmail(email);
        if (entity == null) {
            throw new RuntimeException("Không tìm thấy người dùng");
        }

        // Mã hóa mật khẩu mới
        String encodedPassword = EncryptPasswordUtil.encryptPassword(newPassword);
        entity.setPassword(encodedPassword);

        // Cập nhật thời gian chỉnh sửa
        entity.setModifiedDate(new Timestamp(System.currentTimeMillis()));

        // Lưu thay đổi
        userRepository.save(entity);
    }



}
