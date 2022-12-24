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

//    @Autowired
//    private RoleRepository roleRepository;

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
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));

        // Сразу зададим роль юзер
        user.getRoles().add(ERole.ROLE_USER);

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        log.debug("  authorities: " + authorities);
        user.setAuthorities(authorities);

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

    // Обновить юзера.
    // Когда юзер заходит на свой профайл, то у него должна быть возможность
    // обновить свои данные (имя, фамилию, биографию, ДАТУ РОЖДЕНИЯ)
    @Override
    public User updateUser(UserDto userDto, Principal principal) {
        log.debug("");
        log.debug("Method updateUser()");
        log.debug("  userDto: " + userDto);
//        log.debug("  principal: " + principal);

        // Объект Principal будет содержать в себе данные юзера
        // такие как userName, id
        // И мы можем благодаря этому объекту достать нашего пользователя

        // Сначал берём юзера из БД
        User user = getUserByPrincipal(principal);
        log.debug("  user: " + user);

        // Далее задаём ему данные, которые мы получили из объекта DTO
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setBio(userDto.getBio());

        User updated = userRepository.save(user);
        log.debug("  updated: " + updated);

        return updated;
    }

    // Вспомогательный метод: достать юзера из объекта Principal
    @Override
    public User getUserByPrincipal(Principal principal) {
        log.debug("");
        log.debug("Method getUserByPrincipal()");
//        log.debug("  principal: " + principal);

        String username = principal.getName();
        log.debug("  username: " + username);

        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Override
    public User getUserById(Long id) {
        log.debug("");
        log.debug("Method getUserById()");
        log.debug("  id: " + id);

        User user = userRepository.findUserById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.debug("  user: " + user);
        return user;
    }

    // Вспомогательный метод. Взять текущего пользователя
//    public User getCurrentUser(Principal principal) {
//        return getUserByPrincipal(principal);
//    }

}
