package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

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
        log.info("Входящая объект: " + user);
        User userNew = userService.addUser(user);
        log.info("Созданный объект " + userNew);
        return userNew;
    }

    @Validated(Marker.Update.class)
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Входящая объект: " + user);
        User userUpdate = userService.updateUser(user);
        log.info("Обновленный объект " + userUpdate);
        return userUpdate;
    }

    @GetMapping
    public List<User> findAll() {
        List<User> users = userService.findAllUsers();
        log.info("Возвращаемый массив пользователей: " + users);
        return users;
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Integer id) {
        log.info("Входящий id пользователя: " + id);
        User user = userService.findUserById(id);
        log.info("Возвращаемый объект: " + user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<User> putFriend(@PathVariable Integer id, @PathVariable Integer friendId) {

        log.info("Входящий id пользователя: " + id);
        log.info("Входящий friendId: " + friendId);
        List<User> friends = userService.putFriend(id,friendId);
        log.info("Возвращаемый список друзей: " + friends);
        return friends;
    }


    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Входящий id пользователя: " + id);
        log.info("Входящий friendId: " + friendId);
        User user = userService.deleteFriend(id,friendId);
        log.info("Возвращаемый объект: " + user);
        return user;
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        log.info("Входящий id пользователя: " + id);
        List<User> users = userService.getFriends(id);
        log.info("Возвращаемый массив пользователей: " + users);
        return users;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Входящий id пользователя: " + id);
        log.info("Входящий otherId: " + otherId);
        List<User> users = userService.getMutualFriends(id,otherId);
        log.info("Возвращаемый массив пользователей: " + users);
        return users;
    }

}

