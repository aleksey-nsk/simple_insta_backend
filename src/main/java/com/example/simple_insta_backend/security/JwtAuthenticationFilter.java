package com.example.simple_insta_backend.security;

import com.example.simple_insta_backend.entity.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

// Данный класс - это фильтр, проверяющий JWT-токен при каждом запросе.
//
// После того, как JWT-токен выдан, клиент его отправляет при каждом запросе.
// И проверять этот токен надо при каждом запросе (и извлекать из него данные пользователя).
// Для этого используем данный фильтр (он расширяет OncePerRequestFilter).

@Component
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);

                // Посмотрим есть ли юзер с таким id в БД
                User userDetails = customUserDetailsService.loadUserById(userId);

                // Передаём данные юзера
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, Collections.emptyList()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Зададим контекст для SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Всё прошло нормально. Можем аутентифицировать пользователя
            }

        } catch (Exception e) { // что-то пошло не так. Выскочила ошибка. Не можем задать аутентификацию пользователя
            e.printStackTrace();
            log.error("Could not set user authentication");
        }

        // Добавим наш фильтр в цепочку фильтров.
        // То есть мы как бы внедрились в процесс запросов и ответов
        filterChain.doFilter(request, response);
    }

    // Метод, который будет помогать извлекать JWT токен прямо из запроса,
    // поступающего к нам на сервер
    private String getJwtFromRequest(HttpServletRequest request) {
        log.debug("");
        log.debug("Извлекаем JWT-токен из запроса");

        String bearToken = request.getHeader(SecurityConstants.HEADER_STRING);
        log.debug("  bearToken: " + bearToken);

        String jwt = null;

        if (StringUtils.hasText(bearToken) && bearToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            jwt = bearToken.split(" ")[1];
        }

        log.debug("  jwt: " + jwt);
        return jwt;
    }
}
