package com.example.simple_insta_backend.payload.response;

import lombok.Data;

// Когда будет возникать ошибка 401
// то будем создавать объект этого класса
// и выдавать его клиенту

//@Getter
@Data
public class InvalidLoginPasswordResponse {

//    private String username;
//    private String password;
//
//    public InvalidLoginResponse() {
//        this.username = "Invalid Username";
//        this.password = "Invalid Password";
//    }

    private String invalid;

    public InvalidLoginPasswordResponse() {
        this.invalid = "Invalid username or password";
    }
}
