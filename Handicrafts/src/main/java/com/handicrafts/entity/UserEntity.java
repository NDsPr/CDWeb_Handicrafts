package com.handicrafts.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;
    private String email;
    @Column(name = "fullname")
    private String fullname;
    @Column(name = "username")
    private String username;
    private LocalDate birthdate;
    private boolean gender;
    private String phone;
    private String password;
    @Column(name = "confirm_token")
    private String confirmToken;
    private boolean status;
    @Column(name = "is_enable")
    private boolean isEnable;
    @Column
    private String provider;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "updated_at")
    private LocalDate updatedAt;
    @ManyToMany
    @JoinTable(name = "roleuser",
            //joinColumns thì chỉ tới class hiện tai, là khóa ngoại 1
            joinColumns = @JoinColumn(name = "userID"),
            //inverse thì chỉ tới list role bên dưới là khóa ngoại 2
            inverseJoinColumns = @JoinColumn(name = "roleID"))
    private List<RoleEntity> roles = new ArrayList<>();


}
