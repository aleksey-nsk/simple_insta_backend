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

@Component
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("");
        log.debug("Method doFilterInternal()");
        log.debug("  request: " + request);
        log.debug("  response: " + response);
        log.debug("  filterChain: " + filterChain);

        try {
            String jwt = getJwtFromRequest(request);
            log.debug("  jwt: " + jwt);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                log.debug("  userId: " + userId);

                // Посмотрим есть ли юзер в БД с таким id
                User userDetails = customUserDetailsService.loadUserById(userId);
//                log.debug("  userDetails: " + userDetails);

                // Главное передать данные юзера
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, Collections.emptyList()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Зададим контекст для SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Всё прошло нормально. Можем аутентифицировать пользователя
            }

        } catch (Exception e) {
            // Что-то пошло не так. Выскочила ошибка. Не можем задать аутентификацию пользователя
            e.printStackTrace();
            log.error("Could not set user authentication");
        }

        // Добавим наш фильтр в цепочку фильтров
        // Т.е. мы как бы внедрились в процесс запросов и ответов
        filterChain.doFilter(request, response);
    }

    // Метод который будет помогоать брать JWT токен прямо из запроса,
    // который будет поступать к нам на сервер
    private String getJwtFromRequest(HttpServletRequest request) {
        log.debug("");
        log.debug("Method getJwtFromRequest()");

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
