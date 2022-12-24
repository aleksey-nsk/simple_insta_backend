package com.example.simple_insta_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    @NotEmpty
    private String message;

//    private Long userId;
    private String username;
}
