package com.example.simple_insta_backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenSuccessResponse {

    private boolean success;
    private String token;
}
