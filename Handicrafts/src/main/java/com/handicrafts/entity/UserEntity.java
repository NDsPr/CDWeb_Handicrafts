package com.handicrafts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID")
    private int id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String fullname;

    @Column(nullable = false)
    private String username;

    @Column
    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @Column
    private Boolean gender;

    @Column(length = 12)
    private String phone;

    @Column(length = 70)
    private String password;

    @Column
    private Boolean status;

    @Column(name = "is_enable")
    private Boolean isEnable;

    @Column(length = 25)
    private String provider;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    // Thêm mối quan hệ với Role
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;
}
