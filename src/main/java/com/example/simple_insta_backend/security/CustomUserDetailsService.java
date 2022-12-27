package com.example.simple_insta_backend.security;

import com.example.simple_insta_backend.entity.Post;
import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.repository.PostRepository;
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

// Аутентификация с пользовательским UserDetailsService.
//
// Мы переопределяем метод loadUserByUsername(), чтобы Spring Security понимал,
// как взять пользователя по его имени из хранилища.
// Имея этот метод, Spring Security может сравнить переданный пароль с настоящим
// и аутентифицировать пользователя (либо не аутентифицировать).

@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    // Реализовать извлечение пользователя из БД
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.debug("");
        log.debug("CustomUserDetailsService -> method loadUserByUsername(), username: '" + username + "'");

        User user = userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        log.debug("  user: " + user);

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        User buildUser = new User();
        buildUser.setId(user.getId());
        buildUser.setUsername(user.getUsername());
        buildUser.setPassword(user.getPassword());
        buildUser.setEmail(user.getEmail());
        buildUser.setAuthorities(authorities);
        log.debug("  buildUser: " + buildUser);

        return buildUser;
    }

    public User loadUserById(Long id) {
        log.debug("");
        log.debug("Method loadUserById()");
        log.debug("  id: " + id);

        User user = userRepository.findUserById(id).orElse(null);

        if (user != null) {
            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList());
            user.setAuthorities(authorities);

            List<Post> userPosts = postRepository.findAllByUserOrderByCreatedDateDesc(user);
            user.setPosts(userPosts);
        }

        log.debug("  user: " + user);
        return user;
    }
}
