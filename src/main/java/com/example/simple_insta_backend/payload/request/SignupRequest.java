package com.example.simple_insta_backend.payload.request;

import com.example.simple_insta_backend.annotation.PasswordMatches;
import com.example.simple_insta_backend.annotation.UniqueEmail;
import com.example.simple_insta_backend.annotation.UniqueUsername;
import com.example.simple_insta_backend.annotation.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

// Объект данного класса будем использовать
// при регистрации нового пользователя

@Data
@PasswordMatches // собственная аннотация на уровне класса
public class SignupRequest {

    @NotEmpty(message = "Please enter your username")
    @UniqueUsername // собственная аннотация
    private String username;

    @NotBlank(message = "User email is required")
    @Email(message = "It should have email format")
    @ValidEmail
    @UniqueEmail
    private String email;

    @NotEmpty(message = "Please enter your firstname")
    private String firstname;

    @NotEmpty(message = "Please enter your lastname")
    private String lastname;

    @NotEmpty(message = "Password is required")
    @Size(min = 5, message = "Password should be 5 or more symbols")
    private String password;

    private String confirmPassword;
}
