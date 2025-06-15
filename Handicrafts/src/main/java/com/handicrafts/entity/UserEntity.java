package com.handicrafts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 30)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Column(nullable = false)
    private Integer roleId;

    @Column(nullable = false)
    private Integer status;

    @Column(length = 500)
    private String verifiedCode;

    @Column(length = 255)
    private String changePwHash;

    private Timestamp expiredTime;

    @Column(length = 100)
    private String addressLine;

    @Column(length = 100)
    private String addressWard;

    @Column(length = 100)
    private String addressDistrict;

    @Column(length = 100)
    private String addressProvince;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

    @Column(length = 255)
    private String viaOAuth;

    // Mối quan hệ với Role
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId", referencedColumnName = "id", insertable = false, updatable = false)
    private RoleEntity role;
}
