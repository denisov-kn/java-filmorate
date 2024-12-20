package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedIdFriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;
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

    public Collection<User> findAllUsers() {
        return inMemoryUserStorage.findAllUsers();
    }

    public User findUserById(Integer id) {
        checkUserById(id);
        return inMemoryUserStorage.findUserById(id);
    }

    public Set<Integer> putFriend(Integer userId, Integer friendId) {

        if (userId.equals(friendId)) {
            throw new DuplicatedIdFriendsException("Нельзя добавить пользователя другом самому себе id: "
                    + userId + " friendId: " + friendId);
        }

        checkUserById(userId);
        checkUserById(friendId);
        return inMemoryUserStorage.putFriend(userId, friendId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        User user = findUserById(userId);
        checkUserById(friendId);
        if (!inMemoryUserStorage.isFriends(userId, friendId))
           return user;
        inMemoryUserStorage.deleteFriend(userId, friendId);
        return user;
    }

    public Collection<User> getFriends(Integer userId) {
        checkUserById(userId);
        return inMemoryUserStorage.getFriends(userId).stream()
                .map(inMemoryUserStorage::findUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> getMutualFriends(Integer userId, Integer otherId) {
        checkUserById(userId);
        checkUserById(otherId);

        return inMemoryUserStorage.getMutualFriends(userId, otherId);

    }

    private void checkUserById(Integer id) {
        if (!inMemoryUserStorage.isUserById(id))
            throw new NotFoundException("Пользователь с таким id: " + id + " не найден");
    }
    private void checkLogin(User user) {
        if (user.getName() == null)
            user.setName(user.getLogin());
    }
}
