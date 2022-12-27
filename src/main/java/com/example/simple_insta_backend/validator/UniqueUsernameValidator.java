package com.example.simple_insta_backend.validator;

import com.example.simple_insta_backend.annotation.UniqueUsername;
import com.example.simple_insta_backend.entity.User;
import com.example.simple_insta_backend.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Component
@Log4j2
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        Optional<User> optional = userRepository.findUserByUsername(username);

        if (optional.isPresent()) {
            log.error("");
            log.error("В БД уже есть пользователь с username='" + username + "'");
            return false;
        }

        return true;
    }
}
