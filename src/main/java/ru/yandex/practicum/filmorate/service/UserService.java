package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.DuplicatedIdFriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto addUser(NewUserRequest request) {

        if (request.getName() == null)
            request.setName(request.getLogin());

        User user = UserMapper.mapToUser(request);
        user = userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto updateUser(UpdateUserRequest request) {
        checkUserById(request.getId());

        if (request.getName() == null)
            request.setName(request.getLogin());

        User updateUser = userRepository.findById(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NotFoundException("User с таким id: " + request.getId() + " не найден"));

        updateUser = userRepository.update(updateUser);
        return UserMapper.mapToUserDto(updateUser);
    }

    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto findUserById(Integer id) {
        return userRepository.findById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id: " + id + " не найден"));

    }


    public List<UserDto> putFriend(Integer userId, Integer friendId) {

        if (userId.equals(friendId)) {
            throw new DuplicatedIdFriendsException("Нельзя добавить пользователя другом самому себе id: "
                    + userId + " friendId: " + friendId);
        }
        checkUserById(userId);
        checkUserById(friendId);
        return userRepository.putFriends(userId, friendId).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto deleteFriend(Integer userId, Integer friendId) {
        UserDto userDto = findUserById(userId);
        checkUserById(friendId);
        if (userRepository.isFriends(userId, friendId)) {
            userRepository.removeFriends(userId, friendId);
        }
        return userDto;
    }

    public List<UserDto> getFriends(Integer userId) {
        checkUserById(userId);
        return userRepository.findFriends(userId).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getMutualFriends(Integer userId, Integer otherId) {
        checkUserById(userId);
        checkUserById(otherId);
        return userRepository.getMutualFriends(userId, otherId).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());

    }

    private void checkUserById(Integer id) {
        findUserById(id);
    }

}
