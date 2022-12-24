package com.example.simple_insta_backend.controller;

// Этот REST-контроллер нужен для проверки авторизации

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
@Log4j2
public class HelloController {

    @GetMapping("/all")
    public String helloAll() {
        log.debug("");
        log.debug("HelloController -> method helloAll()");
        return "Hello All";
    }

    // Сюда доступ разрешён только для USER и ADMIN
    @GetMapping("/user")
    public String helloUser() {
        log.debug("");
        log.debug("HelloController -> method helloUser()");
        return "Hello User";
    }

    // Сюда доступ разрешён только для ADMIN
    @GetMapping("/admin")
    public String helloAdmin() {
        log.debug("");
        log.debug("HelloController -> method helloAdmin()");
        return "Hello Admin";
    }
}
