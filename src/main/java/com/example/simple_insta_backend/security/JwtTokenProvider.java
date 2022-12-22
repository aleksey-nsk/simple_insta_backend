package com.example.simple_insta_backend.security;

import com.example.simple_insta_backend.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// Данный класс будет создавать наш токен

@Component
@Log4j2
public class JwtTokenProvider {

    // Генерация jwt-токена
    public String generateToken(Authentication authentication) {
        log.debug("");
        log.debug("Method generateToken()");

        User user = (User) authentication.getPrincipal();
        log.debug("  user: " + user);

        Date now = new Date(System.currentTimeMillis()); // текущее время
        Date expiration = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME); // когда токен иссякнет
        log.debug("  now: " + now);
        log.debug("  expiration: " + expiration);

        String userId = Long.toString(user.getId());

        // Далее будем создавать claims. Этот как раз тот объект который будем передавать в JSON Web Token-е
        // Он и будет содержать данные юзера
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", user.getUsername());
        claims.put("firstname", user.getFirstname());
        claims.put("lastname", user.getLastname());
        log.debug("  claims: " + claims);

        // Используем библиотеку io.jsonwebtoken
        // для построения токена
        String compact = Jwts.builder()
                .setSubject(userId)
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET_KEY)
                .compact();

        log.debug("  compact: " + compact);
        return compact;
    }

    // Валидация токена
    public boolean validateToken(String token) {
        log.debug("");
        log.debug("Method validateToken()");
        log.debug("  token: " + token);

        try {
            // Пытаемся спарсить токен и взять из него claims
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET_KEY)
                    .parseClaimsJws(token);
            log.debug("  OK");
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException |
                UnsupportedJwtException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    // Напишем последний метод, который будет брать данные (в данном случае id) из токена
    public Long getUserIdFromToken(String token) {
        log.debug("");
        log.debug("Method getUserIdFromToken()");
        log.debug("  token: " + token);

        // Получим claims
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        log.debug("  claims: " + claims);

        String id = (String) claims.get("id");
        log.debug("  id: " + id);
        return Long.parseLong(id);
    }
}
