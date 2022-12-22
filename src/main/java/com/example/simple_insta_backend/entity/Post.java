package com.example.simple_insta_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Название
    @Column(name = "topic")
    private String topic;

    // Небольшой описание
    @Column(name = "caption")
    private String caption;

    // Локация
    @Column(name = "location")
    private String location;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

//    @PrePersist
//    protected void onCreate() {
//        this.createdDate = LocalDateTime.now();
//    }

    // Сколько лайков у поста
//    private Integer likes;

    // Сет юзеров которые лайкнули пост
//    @ElementCollection(targetClass = String.class)
//    private Set<String> likedUsers = new HashSet<>();

    // Юзер который создал данный пост
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER) // когда грузим пост, то сразу хотим видеть коменты
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Like> likes;
}
