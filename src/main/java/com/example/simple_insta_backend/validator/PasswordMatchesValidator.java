package com.example.simple_insta_backend.validator;

import com.example.simple_insta_backend.annotation.PasswordMatches;
import com.example.simple_insta_backend.payload.request.SignupRequest;
import lombok.extern.log4j.Log4j2;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Log4j2
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        log.debug("");
        log.debug("Method isValid()");

        SignupRequest userSignupRequest = (SignupRequest) obj;
        log.debug("  userSignupRequest: " + userSignupRequest);

        // Сама проверка:
        return userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword());
    }
}