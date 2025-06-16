package com.handicrafts.service.impl;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.service.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(UserDTO dto) {
        UserEntity entity = convertToEntity(dto);
        entity.setStatus(0); // chưa kích hoạt
        entity.setRoleId(1); // mặc định role user
        UserEntity saved = userRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    public UserDTO updateUser(UserDTO dto) {
        UserEntity entity = convertToEntity(dto);
        UserEntity saved = userRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    public Optional<UserDTO> findById(Integer id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public Optional<UserDTO> getUserById(Integer id) {
        return userRepository.findUserById(id).map(this::convertToDTO);
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByEmail(username).map(this::convertToDTO);
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::convertToDTO);
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
    @Transactional
    public void activateUser(String activationToken) {
        Optional<UserEntity> userOpt = userRepository.findByVerifiedCode(activationToken);
        userOpt.ifPresent(user -> {
            userRepository.activeAccount(user.getEmail());
            userRepository.setEmptyCode(user.getEmail());
        });
    }

    @Override
    @Transactional
    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        userOpt.ifPresent(user -> userRepository.saveRenewPasswordByEmail(user.getEmail(), newPassword));
    }

    @Override
    @Transactional
    public void resetPassword(String email) {
        String newPassword = "123456"; // TODO: generate random hoặc gửi qua email
        userRepository.saveRenewPasswordByEmail(email, newPassword);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByEmail(username).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDTO convertToDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFullName(entity.getFirstName() + " " + entity.getLastName());
        dto.setPassword(entity.getPassword());
       // dto.setPhone(entity.getPhone());
        dto.setConfirmToken(entity.getVerifiedCode());
        dto.setIsEnable(entity.getStatus() != null && entity.getStatus() == 1);
        dto.setProvider(entity.getViaOAuth());
        dto.setCreatedAt(entity.getCreatedDate());
        dto.setUpdatedAt(entity.getModifiedDate());

        if (entity.getRole() != null) {
            dto.setRoles(List.of(entity.getRole().getName()));
        }
        return dto;
    }

    @Override
    public UserEntity convertToEntity(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setFirstName(dto.getFullName() != null ? dto.getFullName().split(" ")[0] : null);
        entity.setLastName(dto.getFullName() != null ? dto.getFullName().split(" ").length > 1
                ? dto.getFullName().substring(dto.getFullName().indexOf(" ") + 1)
                : "" : "");
        //entity.setPhone(dto.getPhone());
        entity.setVerifiedCode(dto.getConfirmToken());
        entity.setViaOAuth(dto.getProvider());
        entity.setCreatedDate((Timestamp) dto.getCreatedAt());
        entity.setModifiedDate((Timestamp) dto.getUpdatedAt());
        return entity;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + username));

        String[] roles = user.getRole() != null ? new String[]{user.getRole().getName()} : new String[]{"USER"};
        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(roles)
                .build();
    }
}
