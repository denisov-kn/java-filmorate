package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {

    public static User mapToUser(NewUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setLogin(request.getLogin());
        user.setBirthday(request.getBirthday());
        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setLogin(user.getLogin());
        userDto.setBirthday(user.getBirthday());
        return  userDto;
    }


    public static User updateUserFields(User user, UpdateUserRequest request) {

        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setBirthday(request.getBirthday());
        if (request.hasName()) {
            user.setName(request.getName());
        }
        return user;
    }
}
