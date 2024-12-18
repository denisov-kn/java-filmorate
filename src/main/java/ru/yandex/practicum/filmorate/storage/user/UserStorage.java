package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

interface UserStorage {
    User addUser(User user);
    User updateUser(User user);
    Collection<User> findAllUsers();
    boolean isUserById(Integer id);
    User findUserById(Integer id);
}
