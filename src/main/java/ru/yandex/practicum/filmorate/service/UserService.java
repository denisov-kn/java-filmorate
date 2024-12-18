package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
        checkUserById(user.getId());
        checkLogin(user);
        inMemoryUserStorage.updateUser(user);
        return user;
    }

    public void checkUserById(Integer id) {
        if (!inMemoryUserStorage.isUserById(id))
            throw new NotFoundException("Пользователь с таким id: " + id + " не найден");
    }


    public Collection<User> findAllUsers() {
        return inMemoryUserStorage.findAllUsers();
    }

    public User findUserById(Integer id) {
        checkUserById(id);
        return inMemoryUserStorage.findUserById(id);
    }

    public User putFriend(Integer userId, Integer friendId) {
        User user = findUserById(userId);
        User userFriend = findUserById(friendId);
        user.getFriends().add(friendId);
        userFriend.getFriends().add(userId);
        return user;
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        User user = findUserById(userId);
        User userFriend = findUserById(friendId);
        user.getFriends().remove(friendId);
        userFriend.getFriends().remove(userId);
        return user;
    }

    public Collection<User> getFriends(Integer userId) {
        User user = findUserById(userId);

        return user.getFriends().stream()
                .map(inMemoryUserStorage::findUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> getMutualFriends(Integer userId, Integer otherId) {
        User user = findUserById(userId);
        User otherUser = findUserById(otherId);

        Set<Integer> userFriends = new java.util.HashSet<>(Set.copyOf(user.getFriends()));
        Set<Integer> userOtherFriends = Set.copyOf(otherUser.getFriends());

        userFriends.retainAll(userOtherFriends);

        return userFriends.stream()
                .map(inMemoryUserStorage::findUserById)
                .collect(Collectors.toList());

    }



    private void checkLogin(User user) {
        if (user.getName() == null)
            user.setName(user.getLogin());
    }
}
