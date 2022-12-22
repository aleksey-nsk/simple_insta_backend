package com.example.simple_insta_backend.security;

import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Далее будем использовать этот класс в SecurityConfig,
// а именно в AuthenticationManagerBuilder
//
// CustomUserDetailsService помогает доставать юзера из БД

@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Мы со стороны клиента будем передавать
    // username и password
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.debug("");
        log.debug("Method loadUserByUsername()");

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        log.debug("  user: " + user);

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        log.debug("  authorities: " + authorities);

        User buildUser = new User();
        buildUser.setId(user.getId());
        buildUser.setUsername(user.getUsername());
        buildUser.setPassword(user.getPassword());
        buildUser.setEmail(user.getEmail());
        buildUser.setAuthorities(authorities);
        log.debug("  buildUser: " + buildUser);

        return buildUser;
    }

    // Создадим ещё один метод
    public User loadUserById(Long id) {
        log.debug("");
        log.debug("Method loadUserById()");
        log.debug("  id: " + id);

        User user = userRepository.findUserById(id).orElse(null);
//        log.debug("  user: " + user);

        return user;
    }

//    public static User build(User user) {
//        List<GrantedAuthority> authorities = user.getRoles().stream()
//                .map(userRole -> new SimpleGrantedAuthority(userRole.toString()))
//                .collect(Collectors.toList());
//
//        return new User(
//                user.getId(),
//                user.getUsername(),
//                user.getEmail(),
//                user.getPassword(),
//                authorities
//        );
//    }

}
