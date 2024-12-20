package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Set<Integer>> friends = new HashMap<>();
    private final Map<Integer, Set<Integer>> filmsLikes  = new HashMap<>();

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(),user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.replace(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean isUserById(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public User findUserById(Integer id) {
        return users.get(id);
    }

    @Override
    public Set<Integer> putFriend(Integer userId, Integer friendId) {
        if (!friends.containsKey(userId))
            friends.put(userId, new HashSet<Integer>());
        friends.get(userId).add(friendId);

        if (!friends.containsKey(friendId))
            friends.put(friendId, new HashSet<Integer>());
        friends.get(friendId).add(userId);
        return friends.get(userId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        friends.get(userId).remove(friendId);
        friends.get(friendId).remove(userId);
    }

    @Override
    public boolean isFriends(Integer userId, Integer friendId) {
        if (!friends.containsKey(userId) || !friends.containsKey(friendId))
            return false;
        return friends.get(userId).contains(friendId) && friends.get(friendId).contains(userId);
    }

    @Override
    public Set<Integer> getFriends(Integer userId) {
        if (!friends.containsKey(userId))
            return Set.of();
        return friends.get(userId);
    }

    @Override
    public Collection<User> getMutualFriends(Integer userId, Integer otherId) {
        if (!friends.containsKey(userId) || !friends.containsKey(otherId))
            return List.of();

        Set<Integer> userFriends = new java.util.HashSet<>(Set.copyOf(friends.get(userId)));
        Set<Integer> userOtherFriends = Set.copyOf(friends.get(otherId));

        userFriends.retainAll(userOtherFriends);

        return  userFriends.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }


    private Integer getNextId() {
        Integer currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
