package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exception.DuplicatedIdFriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User addUser(User user) {

        if (user.getName() == null)
            user.setName(user.getLogin());

        return userRepository.save(user);
    }

    public User updateUser(User user) {
        checkUserById(user.getId());

        if (user.getName() == null)
            user.setName(user.getLogin());

        User updateUser = userRepository.findById(user.getId())
                .map(user1 -> UserMapper.updateUserFields(user1, user))
                .orElseThrow(() -> new NotFoundException("User с таким id: " + user.getId() + " не найден"));

        return userRepository.update(updateUser);
    }

    public List<User> findAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id: " + id + " не найден"));

    }


    public List<User> putFriend(Integer userId, Integer friendId) {

        if (userId.equals(friendId)) {
            throw new DuplicatedIdFriendsException("Нельзя добавить пользователя другом самому себе id: "
                    + userId + " friendId: " + friendId);
        }
        checkUserById(userId);
        checkUserById(friendId);
        return userRepository.putFriends(userId, friendId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        User user = findUserById(userId);
        checkUserById(friendId);
        if (userRepository.isFriends(userId, friendId)) {
            userRepository.removeFriends(userId, friendId);
        }
        return user;
    }

    public List<User> getFriends(Integer userId) {
        checkUserById(userId);
        return userRepository.findFriends(userId);

    }

    public List<User> getMutualFriends(Integer userId, Integer otherId) {
        checkUserById(userId);
        checkUserById(otherId);
        return userRepository.getMutualFriends(userId, otherId);


    }

    private void checkUserById(Integer id) {
        findUserById(id);
    }

}
