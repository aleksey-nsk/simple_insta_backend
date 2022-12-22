package com.example.simple_insta_backend.controller;

// Этот REST-контроллер нужен для проверки авторизации

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Log4j2
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        log.debug("");
        log.debug("HelloController -> method hello()");
        return "Hello";
    }

    // Сюда доступ разрешён только для USER и ADMIN
    @GetMapping("/user")
    public String user() {
        log.debug("");
        log.debug("HelloController -> method user()");
        return "User";
    }

    // Сюда доступ разрешён только для ADMIN
    @GetMapping("/admin")
    public String admin() {
        log.debug("");
        log.debug("HelloController -> method admin()");
        return "Admin";
    }
}
