package com.example.simple_insta_backend.facade;

import com.example.simple_insta_backend.dto.UserDto;
import com.example.simple_insta_backend.entity.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

// DTO-объекты уже созданы.
//
// Теперь создадим фасады, которые будут мапить нашу модель из БД
// на DTO, чтобы потом отдать клиенту.
// Клиенту будем возвращать UserDto

@Component
@Log4j2
public class UserFacade {

    public UserDto userToUserDto(User user) {
        log.debug("");
        log.debug("Method userToUserDto()");
        log.debug("  user: " + user);

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setBio(user.getBio());

        log.debug("  userDto: " + userDto);
        return userDto;
    }
}
