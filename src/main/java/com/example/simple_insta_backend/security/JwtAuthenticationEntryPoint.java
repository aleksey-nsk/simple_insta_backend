package com.example.simple_insta_backend.security;

import com.example.simple_insta_backend.payload.response.InvalidLoginPasswordResponse;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Этот класс будет возвращать нам объект, который будет содержать что
// параметры, которые мы внесли, не правильные.
// Т.е. этот класс будет служить для того чтобы ловить ошибку аутентификации
// и выдавать статус 401

@Component
@Log4j2
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.debug("");
        log.debug("Method commence()");
        log.debug("  request: " + request);
        log.debug("  response: " + response);
        log.debug("  authException: " + authException);

        InvalidLoginPasswordResponse invalidResponse = new InvalidLoginPasswordResponse();
        log.debug("  invalidResponse: " + invalidResponse);

        String jsonInvalidResponse = new Gson().toJson(invalidResponse);
        log.debug("  jsonInvalidResponse: " + jsonInvalidResponse);

        // response - объект, который будет возвращаться клиенту
        response.setContentType(SecurityConstants.CONTENT_TYPE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().println(jsonInvalidResponse);
    }
}
