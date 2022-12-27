package com.example.simple_insta_backend.controller;

import com.example.simple_insta_backend.dto.UserDto;
import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.facade.UserFacade;
import com.example.simple_insta_backend.service.UserService;
import com.example.simple_insta_backend.validator.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    // Возвращаем юзера, который сейчас авторизирован в системе.
    // Его данные находятся в объекте Principal
    @GetMapping("/")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        UserDto userDto = userFacade.userToUserDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable("userId") String id) {
        User user = userService.getUserById(Long.parseLong(id));
        UserDto userDTO = userFacade.userToUserDto(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@RequestBody @Valid UserDto userDto, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        User updated = userService.updateUser(userDto, principal);
        UserDto updatedDto = userFacade.userToUserDto(updated);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }
}
