package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Validated(Marker.Create.class)
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Входящий объект: " + user);
        userService.addUser(user);
        log.info("Созданный объект: " + user);
        return user;
    }

    @Validated(Marker.Update.class)
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Входящий объект: " + user);
        userService.updateUser(user);
        log.info("Обновленный объект:" + user);
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
      //  log.info("Возвращаемый массив пользователей: " + users.values());
        return userService.findAllUsers();
    }


}

