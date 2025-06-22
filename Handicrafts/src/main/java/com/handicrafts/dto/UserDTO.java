package com.handicrafts.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class UserDTO {
    private Integer id;
    private String email;
    private String fullName;
    private String firstName;
    private String lastName;
    private String username;
    private Date birthDate;
    private Boolean gender;
    private String phone;
    private String password;
    private Boolean status;
    private String confirmToken;
    private Boolean isEnable;
    private String provider;
    private Date createdAt;
    private Date updatedAt;
    private List<String> roles;
    private String addressLine;
    private String addressWard;
    private String addressDistrict;
    private String addressProvince;


    // Constructor mặc định
    public UserDTO() {
    }

    // Getters và Setters
    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Boolean getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getConfirmToken() {
        return confirmToken;
    }

    public Boolean getIsEnable() {
        return isEnable;
    }

    public String getProvider() {
        return provider;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public List<String> getRoles() {
        return roles;
    }

    // Bạn cũng có thể thêm phương thức để cập nhật fullName dựa trên firstName và lastName
    public void updateFullName() {
        if (firstName != null && lastName != null) {
            this.fullName = firstName + " " + lastName;
        } else if (firstName != null) {
            this.fullName = firstName;
        } else if (lastName != null) {
            this.fullName = lastName;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getEnable() {
        return isEnable;
    }

    public void setEnable(Boolean enable) {
        isEnable = enable;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    public String getAddressDistrict() {
        return addressDistrict;
    }

    public void setAddressDistrict(String addressDistrict) {
        this.addressDistrict = addressDistrict;
    }

    public String getAddressWard() {
        return addressWard;
    }

    public void setAddressWard(String addressWard) {
        this.addressWard = addressWard;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }
}
