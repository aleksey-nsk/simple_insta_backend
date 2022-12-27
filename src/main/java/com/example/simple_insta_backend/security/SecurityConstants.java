package com.example.simple_insta_backend.security;

// Необходимые параметры

public class SecurityConstants {

    // JWT-токен на сервере не хранится, а просто каждый раз проверяется на подлинность.
    // На сервере хранится только "секретный ключ"
    public static final String SECRET_KEY = "SecretKeyGenJwt12345";

    public static final String AUTH_URLS = "/api/v1/auth/**";
    public static final String TOKEN_PREFIX = "Bearer "; // пробел обязателен
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";

    // Сделаем так, чтобы jwt-токен мог протухнуть
//    public static final long EXPIRATION_TIME = 600_000; // 600_000 мс == 10 минут
    public static final long EXPIRATION_TIME = 6_000_000; // 6_000_000 мс == 100 минут
//    public static final long EXPIRATION_TIME = 60_000; // 60_000 мс == 1 минута
}
