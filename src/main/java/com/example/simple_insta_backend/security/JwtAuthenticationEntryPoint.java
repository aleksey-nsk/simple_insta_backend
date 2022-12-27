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

// Этот класс будет возвращать нам объект, который будет содержать
// информацию о том, что параметры, которые мы внесли, не правильные.
//
// То есть этот класс будет служить для того, чтобы ловить ошибку аутентификации
// и выдавать статус 401

@Component
@Log4j2
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error("");
        log.error("JwtAuthenticationEntryPoint -> method commence()");
        log.error("  " + authException.getMessage());

        InvalidLoginPasswordResponse invalidResponse = new InvalidLoginPasswordResponse();
        String jsonInvalidResponse = new Gson().toJson(invalidResponse);
        log.error("  " + jsonInvalidResponse);

        // response - объект, который будет возвращаться клиенту
        response.setContentType(SecurityConstants.CONTENT_TYPE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // UNAUTHORIZED 401
        response.getWriter().println(jsonInvalidResponse);
    }
}
