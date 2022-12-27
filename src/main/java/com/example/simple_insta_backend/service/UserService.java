package com.example.simple_insta_backend.service;

import com.example.simple_insta_backend.dto.UserDto;
import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.payload.request.SignupRequest;

import java.security.Principal;

public interface UserService {

    User createUser(SignupRequest userIn);

    User updateUser(UserDto userDto, Principal principal);

    User getUserByPrincipal(Principal principal);

    User getUserById(Long id);
}
