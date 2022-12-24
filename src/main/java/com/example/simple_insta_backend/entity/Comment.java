package com.example.simple_insta_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message", columnDefinition = "text", nullable = false)
    private String message;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

//    @PrePersist
//    protected void onCreate() {
//        this.createdDate = LocalDateTime.now();
//    }

    // Юзер сделавший комент
//    @Column(name = "user_id", nullable = false)
//    private Long userId;
    @Column(name = "username", nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", createdDate=" + createdDate +
                ", username='" + username + '\'' +
                '}';
    }
}
