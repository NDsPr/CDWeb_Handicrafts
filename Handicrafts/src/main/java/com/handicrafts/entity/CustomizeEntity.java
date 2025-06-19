package com.handicrafts.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customize_pages")
@Data
public class CustomizeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "welcomeTitle", nullable = false, length = 200)
    private String welcomeTitle;

    @Column(name = "welcomeDes", nullable = false, length = 1000)
    private String welcomeDes;

    @Column(name = "productTitle", nullable = false, length = 200)
    private String productTitle;

    @Column(name = "productDes", nullable = false, length = 1000)
    private String productDes;

    @Column(name = "prTitle1", nullable = false, length = 200)
    private String prTitle1;

    @Column(name = "prDes1", nullable = false, length = 1000)
    private String prDes1;

    @Column(name = "prIcon1", nullable = false, length = 1000)
    private String prIcon1;

    @Column(name = "prContentTitle1", nullable = false, length = 500)
    private String prContentTitle1;

    @Column(name = "prContentDes1", nullable = false, columnDefinition = "TEXT")
    private String prContentDes1;

    @Column(name = "prLink1", nullable = false, columnDefinition = "TEXT")
    private String prLink1;

    @Column(name = "prLink1InStorage", nullable = false, length = 200)
    private String prLink1InStorage;

    @Column(name = "prTitle2", nullable = false, length = 200)
    private String prTitle2;

    @Column(name = "prDes2", nullable = false, length = 1000)
    private String prDes2;

    @Column(name = "prContent2", nullable = false, columnDefinition = "TEXT")
    private String prContent2;

    @Column(name = "prLink2", nullable = false, columnDefinition = "TEXT")
    private String prLink2;

    @Column(name = "prLink2InStorage", nullable = false, length = 200)
    private String prLink2InStorage;

    @Column(name = "background", nullable = false, columnDefinition = "TEXT")
    private String background;

    @Column(name = "backgroundInStorage", nullable = false, length = 200)
    private String backgroundInStorage;

    @Column(name = "footerLeft", nullable = false, length = 1000)
    private String footerLeft;

    @Column(name = "footerMiddle", nullable = false, length = 1000)
    private String footerMiddle;

    @Column(name = "facebookLink", length = 300)
    private String facebookLink;

    @Column(name = "twitterLink", length = 300)
    private String twitterLink;

    @Column(name = "instagramLink", length = 300)
    private String instagramLink;

    @Column(name = "linkedinLink", length = 300)
    private String linkedinLink;
}
