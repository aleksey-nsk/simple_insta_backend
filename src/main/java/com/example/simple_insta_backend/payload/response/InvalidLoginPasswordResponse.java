package com.example.simple_insta_backend.payload.response;

import lombok.Data;

// Если возникнет ошибка "401 Unauthorized",
// тогда создадим объект этого класса,
// и отдадим его клиенту

@Data
public class InvalidLoginPasswordResponse {

    private String invalid;

    public InvalidLoginPasswordResponse() {
        this.invalid = "Invalid username or password";
    }
}
