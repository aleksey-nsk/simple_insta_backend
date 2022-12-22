package com.example.simple_insta_backend.validator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

// Данный валидатор поможет справляться
// с теми или иными ошибками, которые будут приходить на наш сервер
//
// Данным сервисом-валидатором можем пользоваться в любом контроллере
// для валидации тех или иных ошибок

@Service
@Log4j2
public class ResponseErrorValidation {

    public ResponseEntity<Object> mapValidationService(BindingResult bindingResult) {
        log.debug("");
        log.debug("Method mapValidationService()");
        log.debug("  bindingResult: " + bindingResult);

        // Объект типа BindingResult будет содержать в себе ошибки
        // Например пришёл объект типа LoginRequest с пустым username
        // ты будет ошибка @NotEmpty(message = "Username cannot be empty")
        // Ошибка попадёт именно в объект BindingResult

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            // getAllErrors может содержать объекты
            if (!CollectionUtils.isEmpty(bindingResult.getAllErrors())) {
                for (ObjectError error : bindingResult.getAllErrors()) {
                    errorMap.put(error.getCode(), error.getDefaultMessage());
                }
            }

            // getFieldErrors говорит именно об ошибках филдов
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            log.debug("  errorMap: " + errorMap);

            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }

        // Если всё хорошо, и ошибок нет, то возвращаем null
        return null;
    }
}
