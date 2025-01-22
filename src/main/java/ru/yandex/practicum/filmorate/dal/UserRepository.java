package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM USERS";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM USERS WHERE USER_ID = :userId";
    private static final String INSERT_QUERY = "INSERT INTO USERS(EMAIL, LOGIN, USER_NAME, BIRTHDAY)" +
            "VALUES (:email, :login, :userName, :birthday)";
    private static final String UPDATE_QUERY = "UPDATE USERS SET EMAIL = :email, LOGIN = :login, USER_NAME = :userName, BIRTHDAY = :birthday WHERE USER_ID = :userId";
    private static final String INSERT_FRIENDS = "INSERT INTO FRIENDS(USER_ID, FRIEND_USER_ID) VALUES (:userId, :friendUserId)";
    public static final String FIND_FRIENDS_BY_ID = "SELECT U.* FROM USERS U JOIN FRIENDS F ON U.USER_ID = F.FRIEND_USER_ID WHERE F.USER_ID = :userId";
    public static final String FIND_FRIENDS_BY_IDS =  "SELECT U.* FROM USERS U JOIN FRIENDS F ON U.USER_ID = F.USER_ID WHERE F.USER_ID = :userId AND F.FRIEND_USER_ID = :friendUserId";
    public static final String DELETE_FRIENDS = "DELETE FROM FRIENDS WHERE USER_ID = :userId AND FRIEND_USER_ID = :friendUserId";
    public static final String FIND_MUTUAL_FRIENDS = "SELECT U.* FROM USERS U JOIN FRIENDS F1 ON U.USER_ID = F1.FRIEND_USER_ID JOIN FRIENDS F2 ON U.USER_ID = F2.FRIEND_USER_ID WHERE F1.USER_ID = :f1UserId AND F2.USER_ID = :f2UserId ";


    public UserRepository(NamedParameterJdbcOperations jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        return findMany(FIND_ALL_QUERY, params);
    }

    public Optional<User> findById(int userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        return findOne(FIND_BY_ID_QUERY, params);
    }

    public User save(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("userName", user.getName());
        params.addValue("birthday", user.getBirthday());
        Integer id = insert(INSERT_QUERY, params, "USER_ID");
        user.setId(id);
        return user;
    }

    public User update(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("userName", user.getName());
        params.addValue("birthday", user.getBirthday());
        params.addValue("userId", user.getId());
        update(UPDATE_QUERY, params);
        return user;
    }

    public List<User> putFriends(Integer userId, Integer friendsId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("friendUserId", friendsId);
        insert(INSERT_FRIENDS, params, "FRIEND_ID");

        MapSqlParameterSource paramsSearch = new MapSqlParameterSource();
        paramsSearch.addValue("userId", userId);

       return findMany(FIND_FRIENDS_BY_ID, paramsSearch);
    }

    public User removeFriends(Integer userId, Integer friendsId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("friendUserId", friendsId);
        update(DELETE_FRIENDS, params);

       return findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }


    public  Boolean isFriends(Integer userId, Integer friendsId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("friendUserId", friendsId);
        return  findOne(FIND_FRIENDS_BY_IDS, params).isPresent();
    }


    public List<User> findFriends(Integer userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        return findMany(FIND_FRIENDS_BY_ID, params);
    }


    public List<User> getMutualFriends(Integer user1Id, Integer user2Id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("f1UserId", user1Id);
        params.addValue("f2UserId", user2Id);
        return findMany(FIND_MUTUAL_FRIENDS, params);
    }


}
