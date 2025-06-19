package com.handicrafts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomizeDTO {
    private Integer id;

    @NotBlank(message = "Welcome title is required")
    @Size(max = 200, message = "Welcome title cannot exceed 200 characters")
    private String welcomeTitle;

    @NotBlank(message = "Welcome description is required")
    @Size(max = 1000, message = "Welcome description cannot exceed 1000 characters")
    private String welcomeDes;

    // Thêm các ràng buộc tương tự cho các trường khác

    private String productTitle;
    private String productDes;
    private String prTitle1;
    private String prDes1;
    private String prIcon1;
    private String prContentTitle1;
    private String prContentDes1;
    private String prLink1;
    private String prLink1InStorage;
    private String prTitle2;
    private String prDes2;
    private String prContent2;
    private String prLink2;
    private String prLink2InStorage;
    private String background;
    private String backgroundInStorage;
    private String footerLeft;
    private String footerMiddle;
    private String facebookLink;
    private String twitterLink;
    private String instagramLink;
    private String linkedinLink;
}
