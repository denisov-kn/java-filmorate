package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @Validated(Marker.Create.class)
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Входящий объект: " + user);
        user.setId(getNextId());
        checkLogin(user);
        users.put(user.getId(),user);
        log.info("Созданный объект: " + user);
        return user;
    }

    @Validated(Marker.Update.class)
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Входящий объект: " + user);
        if (!users.containsKey(user.getId()))
            throw new NotFoundException("Пользователь с таким id: " + user.getId() + " не найден");
        checkLogin(user);
        users.replace(user.getId(), user);
        log.info("Обновленный объект:" + user);
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Возвращаемый массив пользователей: " + users.values());
        return new ArrayList<>(users.values());
    }

    private Integer getNextId() {
        Integer currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkLogin(User user) {
        if (user.getName() == null)
            user.setName(user.getLogin());
    }
}

