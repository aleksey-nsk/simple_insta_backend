package com.example.simple_insta_backend.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

// Это тот объект, который мы будем передавать на сервер,
// когда будем пытаться авторизироваться на нашем сайте

@Data
public class LoginRequest {

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
