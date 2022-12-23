package com.example.simple_insta_backend.service;

import com.example.simple_insta_backend.dto.UserDto;
import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.payload.request.SignupRequest;

import java.security.Principal;

/**
 * @author Aleksey Zhdanov
 * @version 1
 */
public interface UserService {

    /**
     * <p>Создаёт нового пользователя</p>
     *
     * @param userIn Данные пользователя для добавления
     * @return Сохранённый в БД пользователь
     */
    User createUser(SignupRequest userIn);

    User updateUser(UserDto userDto, Principal principal);
}
