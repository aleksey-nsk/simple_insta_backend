package com.example.simple_insta_backend.service.impl;

import com.example.simple_insta_backend.dto.UserDto;
import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.entity.enums.ERole;
import com.example.simple_insta_backend.exception.UserExistException;
import com.example.simple_insta_backend.payload.request.SignupRequest;
import com.example.simple_insta_backend.repository.UserRepository;
import com.example.simple_insta_backend.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
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
        String encodedPswd = passwordEncoder.encode(userIn.getPassword());
        user.setPassword(encodedPswd);

        // Зададим стандартную роль ROLE_USER
        user.getRoles().add(ERole.ROLE_USER);

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        user.setAuthorities(authorities);

        log.debug("  user: " + user);

        try {
            User savedUser = userRepository.save(user);
            log.debug("  savedUser: " + savedUser);
            return savedUser;
        } catch (Exception e) {
            log.error("Error during registration: {}", e.getMessage());
            throw new UserExistException("The user already exist. Please check credentials");
        }
    }

    // Обновить юзера. Когда юзер заходит на свой профайл, то у него
    // должна быть возможность обновить свои данные (имя, фамилию, биографию)
    @Override
    public User updateUser(UserDto userDto, Principal principal) {
        log.debug("");
        log.debug("Обновить юзера");
        log.debug("  userDto: " + userDto);

        User user = getUserByPrincipal(principal);

        // Задаём юзеру данные, полученные из объекта DTO
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setBio(userDto.getBio());

        User updated = userRepository.save(user);
        log.debug("");
        log.debug("  updated: " + updated);
        return updated;
    }

    @Override
    public User getUserByPrincipal(Principal principal) {
        log.debug("");
        log.debug("  Достать юзера из объекта Principal");

        String username = principal.getName();

        User user = userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        log.debug("    user: " + user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        log.debug("");
        log.debug("Получить юзера по id=" + id);

        User user = userRepository.findUserById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.debug("  user: " + user);
        return user;
    }
}
