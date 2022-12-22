package com.example.simple_insta_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// Нет прямой зависимости.
// Будем сохранять изображение либо юзеру (тогда задаём userId, а postId=null)
// Либо сохраняем посту (тогда задаём postId, а userId=null)

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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "image_bytes")
    private byte[] imageBytes;

    //    @JsonIgnore // не передавать на клиента
    @Column(name = "user_id")
    private Long userId;

    //    @JsonIgnore
    @Column(name = "post_id")
    private Long postId;
}
