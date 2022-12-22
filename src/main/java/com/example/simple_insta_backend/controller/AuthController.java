//package com.example.simple_insta_backend.controller;
//
//// Этот контроллер будет отвечать
//// за авторизацию пользователей
//
//import com.example.simple_insta_backend.security.payload.request.LoginRequest;
//import com.example.simple_insta_backend.security.payload.request.SignupRequest;
//import com.example.simple_insta_backend.security.payload.response.JwtTokenSuccessResponse;
//import com.example.simple_insta_backend.security.payload.response.MessageResponse;
//import com.example.simple_insta_backend.security.JwtTokenProvider;
//import com.example.simple_insta_backend.security.SecurityConstants;
//import com.example.simple_insta_backend.service.impl.UserService;
//import com.example.simple_insta_backend.security.validator.ResponseErrorValidation;
//import liquibase.repackaged.org.apache.commons.lang3.ObjectUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//
////@CrossOrigin // что это???
//@RestController
//@RequestMapping("/api/auth")
//@PreAuthorize("permitAll()") // что это???
//public class AuthController {
//
//    @Autowired
//    private ResponseErrorValidation responseErrorValidation;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//
//    // При регистрации будем засылать /api/auth/signup
//    @PostMapping("/signup")
//    public ResponseEntity<Object> registerUser(@RequestBody @Valid SignupRequest signupRequest, BindingResult bindingResult) {
//        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
//        if (!ObjectUtils.isEmpty(errors)) return errors;
//
//        userService.createUser(signupRequest);
//        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
//    }
//
//    // Авторизация юзера:
//    @PostMapping("/signin")
//    public ResponseEntity<Object> authenticateUser(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) {
//        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
//        if (!ObjectUtils.isEmpty(errors)) return errors;
//
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                loginRequest.getUsername(),
//                loginRequest.getPassword()
//        ));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
//
//        // Это передаём на клиент (на Ангуляр)
//        return ResponseEntity.ok(new JwtTokenSuccessResponse(true, jwt));
//    }
//}
