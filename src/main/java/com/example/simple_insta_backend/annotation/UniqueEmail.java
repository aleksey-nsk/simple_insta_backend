package com.example.simple_insta_backend.annotation;

import com.example.simple_insta_backend.validator.UniqueEmailValidator;
import com.example.simple_insta_backend.validator.UniqueUsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = UniqueEmailValidator.class) // валидатор свой создадим
@Documented
public @interface UniqueEmail {

    String message() default "This email already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
