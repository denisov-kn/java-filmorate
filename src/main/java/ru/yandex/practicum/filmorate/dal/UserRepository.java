package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM USERS";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM USERS WHERE USER_ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO USERS(EMAIL, LOGIN, USER_NAME, BIRTHDAY)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
    private static final String INSERT_FRIENDS = "INSERT INTO FRIENDS(USER_ID, FRIEND_USER_ID) VALUES (?, ?)";
    public static final String FIND_FRIENDS_BY_ID = "SELECT U.* FROM USERS U JOIN FRIENDS F ON U.USER_ID = F.FRIEND_USER_ID WHERE F.USER_ID = ?";
    public static final String FIND_FRIENDS_BY_IDS =  "SELECT U.* FROM USERS U JOIN FRIENDS F ON U.USER_ID = F.USER_ID WHERE F.USER_ID = ? AND F.FRIEND_USER_ID = ?";
    public static final String DELETE_FRIENDS = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_USER_ID = ?";
    public static final String FIND_MUTUAL_FRIENDS = "SELECT U.* FROM USERS U JOIN FRIENDS F1 ON U.USER_ID = F1.FRIEND_USER_ID JOIN FRIENDS F2 ON U.USER_ID = F2.FRIEND_USER_ID WHERE F1.USER_ID = ? AND F2.USER_ID = ?";


    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<User> findById(int userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public User save(User user) {
        Integer id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public List<User> putFriends(Integer userId, Integer friendsId) {
        insert(
                INSERT_FRIENDS,
                userId,
                friendsId
        );

       return findMany(FIND_FRIENDS_BY_ID, userId);
    }

    public User removeFriends(Integer userId, Integer friendsId) {
       update(
               DELETE_FRIENDS,
               userId,
               friendsId
       );

       return findById(userId).orElseThrow(()->new NotFoundException("User not found"));
    }


    public  Boolean isFriends(Integer userId, Integer friendsId) {
        return  findOne(FIND_FRIENDS_BY_IDS, userId, friendsId).isPresent();
    }


    public List<User> findFriends(Integer userId) {
        return findMany(FIND_FRIENDS_BY_ID, userId);
    }


    public List<User> getMutualFriends(Integer userId, Integer friendsId) {
        return findMany(FIND_MUTUAL_FRIENDS, userId, friendsId);
    }


}
