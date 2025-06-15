package com.handicrafts.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String description;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String profilePic;

    private Integer status;

    @Column(name = "category_id")
    private Integer categoryId;
}
