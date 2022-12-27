package com.example.simple_insta_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String topic;
    private String caption;
    private String location;
    private String username;
    private Integer likes;
    private Set<String> usersLiked;
}
