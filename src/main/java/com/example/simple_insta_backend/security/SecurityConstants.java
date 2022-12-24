package com.example.simple_insta_backend.security;

// Необходимые параметры см. в документации

public class SecurityConstants {

    public static final String AUTH_URLS = "/api/v1/auth/**";
    public static final String SECRET_KEY = "SecretKeyGenJwt12345"; // нужно для генерации jwt-токена
    public static final String TOKEN_PREFIX = "Bearer "; // есть пробел
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";

    // Сделаем так чтобы jwt токен мог иссякнуть
//    public static final long EXPIRATION_TIME = 600_000; // 600_000 мс == 10 минут
    public static final long EXPIRATION_TIME = 6_000_000; // 6_000_000 мс == 100 минут
}
