package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    Collection<User> findAllUsers();

    boolean isUserById(Integer id);

    Optional<User> findUserById(Integer id);

    Set<Integer> putFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    boolean isFriends(Integer userId, Integer friendId);

    Set<Integer> getFriends(Integer userId);

    Collection<User> getMutualFriends(Integer userId, Integer otherId);


}
