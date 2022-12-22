package com.example.simple_insta_backend.service.impl;

import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.repository.UserRepository;
import com.example.simple_insta_backend.security.SignupRequest;
import com.example.simple_insta_backend.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;

    public User createUser(SignupRequest userIn) {
//        User user = new User();
//        user.setEmail(userIn.getEmail());
//        user.setNickname(user.getNickname());
//        user.setLastname(user.getLastname());
//        user.setFirstname(user.getFirstname());
//
//        // Сначала кодируем пароль, а затем сохраняем его в БД
//        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
//
//        // Сразу зададим роль юзер
////        user.getRoles().add(ERole.ROLE_USER);
//
//        try {
//            log.debug("Saving User {}", userIn.getEmail());
//            User saved = userRepository.save(user);
//            return saved;
//        } catch (Exception e) {
//            log.error("Error during registration. {}", e.getMessage());
//            throw new UserExistException("The user already exist. Please check credentials");
//        }

        return null;
    }
}
