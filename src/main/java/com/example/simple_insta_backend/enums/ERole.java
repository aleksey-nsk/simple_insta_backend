package com.example.simple_insta_backend.enums;

public enum ERole {

    ROLE_USER("user"),
    ROLE_ADMIN("admin");

    private String role;

    ERole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
