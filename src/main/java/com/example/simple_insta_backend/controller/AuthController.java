package com.example.simple_insta_backend.controller;

import com.example.simple_insta_backend.payload.request.LoginRequest;
import com.example.simple_insta_backend.payload.request.SignupRequest;
import com.example.simple_insta_backend.payload.response.JwtTokenSuccessResponse;
import com.example.simple_insta_backend.payload.response.MessageResponse;
import com.example.simple_insta_backend.security.JwtTokenProvider;
import com.example.simple_insta_backend.security.SecurityConstants;
import com.example.simple_insta_backend.service.UserService;
import com.example.simple_insta_backend.validator.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

// Контроллер отвечающий за РЕГИСТРАЦИЮ и АУТЕНТИФИКАЦИЮ

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // РЕГИСТРАЦИЯ юзера
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid SignupRequest signupRequest, BindingResult bindingResult) {

        // Принимаем BindingResult и сразу смотрим, есть ли в нём какие-нибудь ошибки. Валидируем объект SignupRequest
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        userService.createUser(signupRequest);

        // Мы регистрируемся, поэтому самого юзера на UI возвращать не будем
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    // АУТЕНТИФИКАЦИЯ юзера
    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) {

        // Валидируем объект LoginRequest
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword())
        );

        // Задаём Security Context нашему приложению
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);

        // Это передаём на клиент (на Angular)
        return ResponseEntity.ok(new JwtTokenSuccessResponse(true, jwt));
    }
}
