package com.example.simple_insta_backend.payload.request;

import com.example.simple_insta_backend.annotation.PasswordMatches;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {

    @NotBlank(message = "User email is required")
    @Email(message = "It should have email format")
//    @ValidEmail
    private String email;

    @NotEmpty(message = "Please enter your nickname")
    private String nickname;

    @NotEmpty(message = "Please enter your firstname")
    private String firstname;

    @NotEmpty(message = "Please enter your lastname")
    private String lastname;

    @NotEmpty(message = "Password is required")
    @Size(min = 6)
    private String password;

    private String confirmPassword;
}
