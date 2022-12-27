package com.example.simple_insta_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

// Этот объект мы будет передавать на клиента и принимать от клиента.
// А раз будем принимать => то сделаем дополнительную валидацию.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String username;

    @NotEmpty
    private String firstname;

    @NotEmpty
    private String lastname;

    private String bio;
}
