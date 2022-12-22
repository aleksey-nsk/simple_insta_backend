package com.example.simple_insta_backend.controller;

// Этот контроллер будет отвечать
// за авторизацию пользователей

import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.payload.request.LoginRequest;
import com.example.simple_insta_backend.payload.request.SignupRequest;
import com.example.simple_insta_backend.payload.response.JwtTokenSuccessResponse;
import com.example.simple_insta_backend.payload.response.MessageResponse;
import com.example.simple_insta_backend.security.JwtTokenProvider;
import com.example.simple_insta_backend.security.SecurityConstants;
import com.example.simple_insta_backend.service.UserService;
import com.example.simple_insta_backend.validator.ResponseErrorValidation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

// Контроллер отвечающий за регистрацию
// и аутентификацию

//@CrossOrigin // что это???
@RestController
@RequestMapping("/api/v1/auth")
//@PreAuthorize("permitAll()") // что это???
@Log4j2
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // При регистрации будем засылать /api/v1/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid SignupRequest signupRequest, BindingResult bindingResult) {
        log.debug("");
        log.debug("Method registerUser()");
        log.debug("  signupRequest: " + signupRequest);
        log.debug("  bindingResult: " + bindingResult);

        // Принимаем BindingResult и сразу смотрим, есть ли в нём какие-нибудь ошибки
        // Валидируем объект SignupRequest
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        // Мы регистрируемся, поэтому самого юзера
        // на UI возвращать не будем
        User created = userService.createUser(signupRequest);
        log.debug("  created: " + created);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    // Аутентификация юзера:
    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) {
        log.debug("");
        log.debug("Method authenticateUser()");
        log.debug("  loginRequest: " + loginRequest);
        log.debug("  bindingResult: " + bindingResult);

        // Здесь валидируем объект LoginRequest
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword())
        );

        // Задаём Security Context нашему приложению
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        log.debug("  jwt: " + jwt);

        // Это передаём на клиент (на Ангуляр)
        return ResponseEntity.ok(new JwtTokenSuccessResponse(true, jwt));
    }
}
