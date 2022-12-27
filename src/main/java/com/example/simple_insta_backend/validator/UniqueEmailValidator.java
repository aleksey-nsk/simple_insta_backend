package com.example.simple_insta_backend.validator;

import com.example.simple_insta_backend.annotation.UniqueEmail;
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
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        Optional<User> optional = userRepository.findUserByEmail(email);

        if (optional.isPresent()) {
            log.error("");
            log.error("В БД уже есть пользователь с email='" + email + "'");
            return false;
        }

        return true;
    }
}
