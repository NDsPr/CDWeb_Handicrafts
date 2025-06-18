package com.handicrafts.repository;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.UserEntity;
import jakarta.persistence.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity save(UserEntity entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        return entity;
    }

    public Optional<UserEntity> findById(Integer id) {
        UserEntity entity = entityManager.find(UserEntity.class, id);
        return Optional.ofNullable(entity);
    }

    public List<UserEntity> findAll() {
        return entityManager.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();
    }

    public void deleteById(Integer id) {
        UserEntity entity = entityManager.find(UserEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    public UserEntity findByEmail(String email) {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class);
            query.setParameter("email", email);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Integer findIdByEmail(String email) {
        try {
            TypedQuery<Integer> query = entityManager.createQuery(
                    "SELECT u.id FROM UserEntity u WHERE u.email = :email", Integer.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Optional<UserEntity> findUserById(Integer id) {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.id = :id", UserEntity.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<UserEntity> findActiveAccountByEmail(String email) {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.email = :email AND u.status = 1", UserEntity.class);
            query.setParameter("email", email);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<UserEntity> findByVerifiedCode(String verifiedCode) {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.verifiedCode = :verifiedCode", UserEntity.class);
            query.setParameter("verifiedCode", verifiedCode);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public String checkOAuthAccount(String email) {
        try {
            TypedQuery<String> query = entityManager.createQuery(
                    "SELECT u.viaOAuth FROM UserEntity u WHERE u.email = :email", String.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Optional<UserEntity> findUserByOrderId(Integer orderId) {
        try {
            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.id = (SELECT o.user.id FROM OrderEntity o WHERE o.id = :orderId)",
                    UserEntity.class);
            query.setParameter("orderId", orderId);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public void setEmptyCode(String email) {
        Query query = entityManager.createQuery(
                "UPDATE UserEntity u SET u.verifiedCode = '' WHERE u.email = :email");
        query.setParameter("email", email);
        query.executeUpdate();
    }

    @Transactional
    public void saveNewCodeByEmail(String email, String verifiedCode) {
        Query query = entityManager.createQuery(
                "UPDATE UserEntity u SET u.verifiedCode = :verifiedCode WHERE u.email = :email");
        query.setParameter("email", email);
        query.setParameter("verifiedCode", verifiedCode);
        query.executeUpdate();
    }

    @Transactional
    public int saveRenewPasswordByEmail(String email, String password) {
        Query query = entityManager.createQuery(
                "UPDATE UserEntity u SET u.password = :password WHERE u.email = :email");
        query.setParameter("email", email);
        query.setParameter("password", password);
        return query.executeUpdate();
    }

    @Transactional
    public void activeAccount(String email) {
        Query query = entityManager.createQuery(
                "UPDATE UserEntity u SET u.status = 1 WHERE u.email = :email");
        query.setParameter("email", email);
        query.executeUpdate();
    }

    @Transactional
    public void saveKeyByEmail(String email, String key) {
        Query query = entityManager.createQuery(
                "UPDATE UserEntity u SET u.changePwHash = :key WHERE u.email = :email");
        query.setParameter("email", email);
        query.setParameter("key", key);
        query.executeUpdate();
    }

    @Transactional
    public void setEmptyKey(String email) {
        Query query = entityManager.createQuery(
                "UPDATE UserEntity u SET u.changePwHash = '' WHERE u.email = :email");
        query.setParameter("email", email);
        query.executeUpdate();
    }

    public String getChangePwHashByEmail(String email) {
        try {
            TypedQuery<String> query = entityManager.createQuery(
                    "SELECT u.changePwHash FROM UserEntity u WHERE u.email = :email", String.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public String getHashedPasswordByEmail(String email) {
        try {
            TypedQuery<String> query = entityManager.createQuery(
                    "SELECT u.password FROM UserEntity u WHERE u.email = :email", String.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public int getRecordsTotal() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM UserEntity u", Long.class);
        return query.getSingleResult().intValue();
    }

    public int getRecordsFiltered(String searchValue) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM UserEntity u WHERE u.email LIKE :search OR " +
                        "u.firstName LIKE :search OR u.lastName LIKE :search OR " +
                        "CAST(u.roleId AS string) LIKE :search OR " +
                        "CAST(u.status AS string) LIKE :search OR " +
                        "u.addressLine LIKE :search OR " +
                        "u.addressWard LIKE :search OR " +
                        "u.addressDistrict LIKE :search OR " +
                        "u.addressProvince LIKE :search", Long.class);
        query.setParameter("search", "%" + searchValue + "%");
        return query.getSingleResult().intValue();
    }

    public List<UserEntity> getUsersDatatable(String searchValue, String columnOrder, String orderDir, Pageable pageable) {
        String queryStr = "SELECT u FROM UserEntity u WHERE " +
                "(:searchValue IS NULL OR " +
                "u.email LIKE :search OR " +
                "u.firstName LIKE :search OR " +
                "u.lastName LIKE :search OR " +
                "CAST(u.roleId AS string) LIKE :search OR " +
                "CAST(u.status AS string) LIKE :search OR " +
                "u.addressLine LIKE :search OR " +
                "u.addressWard LIKE :search OR " +
                "u.addressDistrict LIKE :search OR " +
                "u.addressProvince LIKE :search) " +
                "ORDER BY ";

        // Xử lý sắp xếp
        if ("asc".equals(orderDir)) {
            switch (columnOrder) {
                case "id": queryStr += "u.id ASC"; break;
                case "email": queryStr += "u.email ASC"; break;
                case "firstName": queryStr += "u.firstName ASC"; break;
                case "lastName": queryStr += "u.lastName ASC"; break;
                case "roleId": queryStr += "u.roleId ASC"; break;
                case "status": queryStr += "u.status ASC"; break;
                case "createdDate": queryStr += "u.createdDate ASC"; break;
                default: queryStr += "u.id ASC";
            }
        } else {
            switch (columnOrder) {
                case "id": queryStr += "u.id DESC"; break;
                case "email": queryStr += "u.email DESC"; break;
                case "firstName": queryStr += "u.firstName DESC"; break;
                case "lastName": queryStr += "u.lastName DESC"; break;
                case "roleId": queryStr += "u.roleId DESC"; break;
                case "status": queryStr += "u.status DESC"; break;
                case "createdDate": queryStr += "u.createdDate DESC"; break;
                default: queryStr += "u.id DESC";
            }
        }

        TypedQuery<UserEntity> query = entityManager.createQuery(queryStr, UserEntity.class);
        query.setParameter("searchValue", searchValue);
        query.setParameter("search", "%" + searchValue + "%");
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    public Integer getUserStatus(String email) {
        try {
            TypedQuery<Integer> query = entityManager.createQuery(
                    "SELECT u.status FROM UserEntity u WHERE u.email = :email", Integer.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public int updateAccount(String firstName, String lastName, String addressLine,
                             String addressWard, String addressDistrict,
                             String addressProvince, Integer id) {
        Query query = entityManager.createQuery(
                "UPDATE UserEntity u SET u.firstName = :firstName, u.lastName = :lastName, " +
                        "u.addressLine = :addressLine, u.addressWard = :addressWard, " +
                        "u.addressDistrict = :addressDistrict, u.addressProvince = :addressProvince, " +
                        "u.modifiedDate = :now WHERE u.id = :id");
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        query.setParameter("addressLine", addressLine);
        query.setParameter("addressWard", addressWard);
        query.setParameter("addressDistrict", addressDistrict);
        query.setParameter("addressProvince", addressProvince);
        query.setParameter("now", Timestamp.valueOf(LocalDateTime.now()));
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    @Transactional
    public void updateAccountForAdmin(Integer roleId, Integer status, Integer id) {
        Query query = entityManager.createQuery(
                "UPDATE UserEntity u SET u.roleId = :roleId, u.status = :status, " +
                        "u.modifiedDate = :now WHERE u.id = :id");
        query.setParameter("roleId", roleId);
        query.setParameter("status", status);
        query.setParameter("now", Timestamp.valueOf(LocalDateTime.now()));
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public UserEntity findByUsername(String username) {
        try {
            return entityManager.createQuery(
                            "SELECT u FROM UserEntity u WHERE u.email = :username OR u.firstName = :username OR u.lastName = :username",
                            UserEntity.class
                    ).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Đăng ký tài khoản thường (với verified code, trạng thái mặc định là chưa active)
     */
    @Transactional
    public int createInRegister(UserDTO userDTO) {
        UserEntity user = new UserEntity();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // Ensure password is encoded before this point
        user.setFirstName(userDTO.getFullName());
        user.setRoleId(1); // Default role
        user.setStatus(2); // Unverified status
        user.setVerifiedCode(userDTO.getConfirmToken());
        user.setCreatedDate((Timestamp) new Date());
        user.setModifiedDate((Timestamp) new Date());

        try {
            entityManager.persist(user);
            return user.getId(); // Assuming getId() returns the generated ID
        } catch (PersistenceException e) {
            // Handle potential duplicate email or other constraints
            return -1;
        }
    }


    /**
     * Đăng ký tài khoản thông qua OAuth (status = active ngay lập tức)
     */
    @Transactional
    public int createOAuth(UserDTO userDTO) {
        UserEntity user = new UserEntity();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // Ensure password is handled appropriately
        user.setRoleId(1); // Default role
        user.setStatus(1); // Active status for OAuth
        user.setAddressProvince(userDTO.getProvider());
        user.setCreatedDate((Timestamp) new Date());
        user.setModifiedDate((Timestamp) new Date());

        try {
            entityManager.persist(user);
            return user.getId(); // Assuming getId() returns the generated ID
        } catch (PersistenceException e) {
            // Handle potential duplicate email or other constraints
            return -1;
        }
    }

    public Integer countTotalUsers() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM UserEntity u", Long.class);
        return query.getSingleResult().intValue();
    }

    public Integer countAdminUsers() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM UserEntity u WHERE u.roleId = 2", Long.class);
        // Giả sử roleId = 2 là admin, điều chỉnh theo cấu trúc thực tế của bạn
        return query.getSingleResult().intValue();
    }

    public List<Integer> countUser() {
        List<Integer> result = new ArrayList<>();
        result.add(countTotalUsers());
        result.add(countAdminUsers());
        return result;
    }

    public UserEntity findByEmailIgnoreCaseAndStatusAndVerifiedCodeIsNull(String email, int i) {
    }
}
