package com.example.simple_insta_backend.security;

import com.example.simple_insta_backend.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JWTTokenProvider {

//    log.debug("Список всех клиентов: " + list);

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis()); // текущее время
        Date expiration = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME); // когда токен иссякнет

        // Далее будем создавать claims. Этот как раз тот объект который будем передавать в JSON Web Token-е
        // Он и будет содержать данные юзера
        String userId = Long.toString(user.getId());

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getEmail());
        claimsMap.put("firstname", user.getFirstname());
        claimsMap.put("lastname", user.getLastname());

        // Именно классы библиотеки io.jsonwebtoken
        String compact = Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();

        return compact;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException |
                UnsupportedJwtException | IllegalArgumentException exception) {
            log.error(exception.getMessage());
            return false;
        }
    }

    // Напишем последний метод, который будет брать данные (в данном случае id) из токена
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJwt(token)
                .getBody();

        String id = (String) claims.get("id");
        return Long.parseLong(id);
    }
}
