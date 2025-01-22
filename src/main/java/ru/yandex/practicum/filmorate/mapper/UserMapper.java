package ru.yandex.practicum.filmorate.mapper;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {


    public static User updateUserFields(User user, User userUpdate) {

        user.setEmail(userUpdate.getEmail());
        user.setLogin(userUpdate.getLogin());
        user.setBirthday(userUpdate.getBirthday());
        if (userUpdate.hasName()) {
            user.setName(userUpdate.getName());
        }
        return user;
    }
}
