package com.example.simple_insta_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_bytes")
    private byte[] imageBytes;

    //    @JsonIgnore // не передавать на клиента
    @Column(name = "user_id")
    private Long userId;

    //    @JsonIgnore
    @Column(name = "post_id")
    private Long postId;
}
