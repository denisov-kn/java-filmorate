package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
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

    @Validated
    @PostMapping
    public UserDto createUser(@Valid @RequestBody NewUserRequest request) {
        log.info("Входящая объект: " + request);
        UserDto userDto = userService.addUser(request);
        log.info("Созданный объект " + userDto);
        return userDto;
    }

    @Validated
    @PutMapping
    public UserDto updateUser(@Valid @RequestBody UpdateUserRequest request) {
        log.info("Входящая объект: " + request);
        UserDto userDto = userService.updateUser(request);
        log.info("Обновленный объект " + userDto);
        return userDto;
    }

    @GetMapping
    public List<UserDto> findAll() {
        List<UserDto> users = userService.findAllUsers();
        log.info("Возвращаемый массив пользователей: " + users);
        return users;
    }

    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable Integer id) {
        log.info("Входящий id пользователя: " + id);
        UserDto userDto = userService.findUserById(id);
        log.info("Возвращаемый объект: " + userDto);
        return userDto;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<UserDto> putFriend(@PathVariable Integer id, @PathVariable Integer friendId) {

        log.info("Входящий id пользователя: " + id);
        log.info("Входящий friendId: " + friendId);
        List<UserDto> friends = userService.putFriend(id,friendId);
        log.info("Возвращаемый список друзей: " + friends);
        return friends;
    }


    @DeleteMapping("/{id}/friends/{friendId}")
    public UserDto deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Входящий id пользователя: " + id);
        log.info("Входящий friendId: " + friendId);
        UserDto userDto = userService.deleteFriend(id,friendId);
        log.info("Возвращаемый объект: " + userDto);
        return userDto;
    }

    @GetMapping("/{id}/friends")
    public List<UserDto> getFriends(@PathVariable Integer id) {
        log.info("Входящий id пользователя: " + id);
        List<UserDto> usersDto = userService.getFriends(id);
        log.info("Возвращаемый массив пользователей: " + usersDto);
        return usersDto;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserDto> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Входящий id пользователя: " + id);
        log.info("Входящий otherId: " + otherId);
        List<UserDto> usersDto = userService.getMutualFriends(id,otherId);
        log.info("Возвращаемый массив пользователей: " + usersDto);
        return usersDto;
    }

}

