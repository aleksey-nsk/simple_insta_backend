//package com.example.simple_insta_backend.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "roles")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class Role {
//
//    public static final String ROLE_USER = "USER";
//    public static final String ROLE_ADMIN = "ADMIN";
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "role_name")
//    private String roleName;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    public Role(String roleName) {
//        this.roleName = roleName;
//    }
//}
