package com.example.simple_insta_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "middlename")
    private String middlename;

    @Column(name = "nickname", unique = true, updatable = false)
    private String nickname;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", length = 3000)
    private String password;

    @Column(name = "bio", columnDefinition = "text")
    private String bio;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<UserRole> roles;

    // Что это?
    //    @Transient
    //    private Collection<? extends GrantedAuthority> authorities;
}
