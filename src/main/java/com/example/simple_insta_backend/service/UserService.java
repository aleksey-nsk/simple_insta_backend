package com.example.simple_insta_backend.service;

import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.security.SignupRequest;

/**
 * @author Aleksey Zhdanov
 * @version 1
 */
public interface UserService {

    /**
     * <p>Создаёт нового клиента</p>
     *
     * @param userIn Данные клиента для добавления
     * @return Сохранённый в БД клиент
     */
    User createUser(SignupRequest userIn);
}
