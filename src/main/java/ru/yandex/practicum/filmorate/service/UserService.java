package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    public User addUser(User user) {
        checkLogin(user);
        inMemoryUserStorage.addUser(user);
        return user;
    }

    public User updateUser(User user) {
        if (!inMemoryUserStorage.isUserById(user.getId()))
            throw new NotFoundException("Пользователь с таким id: " + user.getId() + " не найден");
        checkLogin(user);
        inMemoryUserStorage.updateUser(user);
        return user;
    }


    public Collection<User> findAllUsers() {
        return inMemoryUserStorage.findAllUsers();
    }


    private void checkLogin(User user) {
        if (user.getName() == null)
            user.setName(user.getLogin());
    }
}
