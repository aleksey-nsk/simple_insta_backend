package com.example.simple_insta_backend.service.impl;

import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.entity.enums.ERole;
import com.example.simple_insta_backend.exception.UserExistException;
import com.example.simple_insta_backend.payload.request.SignupRequest;
import com.example.simple_insta_backend.repository.UserRepository;
import com.example.simple_insta_backend.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User createUser(SignupRequest userIn) {
        log.debug("");
        log.debug("Method createUser()");
        log.debug("  userIn: " + userIn);

        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setUsername(userIn.getUsername());
        user.setLastname(userIn.getLastname());
        user.setFirstname(userIn.getFirstname());
        user.setCreatedDate(LocalDateTime.now());

        // Сначала кодируем пароль, а затем сохраняем его в БД
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));

        // Сразу зададим роль юзер
        user.getRoles().add(ERole.ROLE_USER);
        log.debug("  user: " + user);

        try {
            log.debug("Saving User: {}", userIn.getUsername());
            User savedUser = userRepository.save(user);
            log.debug("  savedUser: " + savedUser);

//            // Сохранённой роли указать id юзера
//            Role savedRole = savedUser.getRoles().stream().findFirst().get();
//            log.debug("  savedRole: " + savedRole);
//            roleRepository.updateRoleSetUser(savedUser, savedRole.getId());

            return savedUser;

        } catch (Exception e) {
            log.error("Error during registration: {}", e.getMessage());
            throw new UserExistException("The user already exist. Please check credentials");
        }
    }
}
