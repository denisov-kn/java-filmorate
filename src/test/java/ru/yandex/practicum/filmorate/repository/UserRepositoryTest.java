package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.Equals;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Import({UserRepository.class, UserRowMapper.class})
@DisplayName("Репозиторий User")
public class UserRepositoryTest {
    private final UserRepository userRepository;

    static User getTestUser() {
        User user = new User();
        user.setEmail("test" + UUID.randomUUID() + "@test.com");
        user.setName("test");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setLogin("test" +  UUID.randomUUID());
        return user;
    }

    @Test
    @DisplayName("Должен находить / сохранять пользователя по id")
    public void shouldSaveFindUserById() {

        User userTest = userRepository.save(getTestUser());
        Integer userId = userTest.getId();

        Optional<User> user = userRepository.findById(userId);


        assertThat(user)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(userTest);
    }


    @Test
    @DisplayName("Должен находить всех пользователей")
    public void shouldFindAllUsers() {

        userRepository.save(getTestUser());
        userRepository.save(getTestUser());
        userRepository.save(getTestUser());

        Assertions.assertEquals(3, userRepository.findAll().size(),"Должно быть 3 пользователя");
    }

    @Test
    @DisplayName("Должен обновлять пользователя")
    public void shouldUpdateUser() {
        Integer userid = userRepository.save(getTestUser()).getId();
        User userUpdate = getTestUser();
        userUpdate.setName("updateUserName");
        userUpdate.setLogin("updateUserLogin");
        userUpdate.setEmail("updateUserEmail@test.com");
        userUpdate.setId(userid);

        userRepository.update(userUpdate);
        assertThat(userRepository.findById(userid))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(userUpdate);
    }

    @Test
    @DisplayName("Должен добавлять в друзья / находить  друзей")
    public void shouldAddFindFriend() {
        User user = userRepository.save(getTestUser());
        Integer userId = user.getId();
        User friend1 = userRepository.save(getTestUser());
        Integer friend1Id = friend1.getId();
        User friend2 = userRepository.save(getTestUser());
        Integer friend2Id = friend2.getId();

        userRepository.putFriends(userId, friend1Id);
        userRepository.putFriends(userId, friend2Id);
        Assertions.assertEquals(2, userRepository.findFriends(userId).size(),
                "У пользователя должно быть два друга");
        Assertions.assertEquals(0, userRepository.findFriends(friend1Id).size(),
                "Т.к. дружба односторонняя у друга1 не должно быть друзей");
        Assertions.assertEquals(0, userRepository.findFriends(friend2Id).size(),
                "Т.к. дружба односторонняя у друга2 не должно быть друзей");

    }


    @Test
    @DisplayName("Должен находить общих друзей")
    public void shouldFindMutualFriends() {
        User user1 = userRepository.save(getTestUser());
        Integer user1Id = user1.getId();
        User user2 = userRepository.save(getTestUser());
        Integer user2Id = user2.getId();
        User user3 = userRepository.save(getTestUser());
        Integer user3Id = user3.getId();
        User user4 = userRepository.save(getTestUser());
        Integer user4Id = user4.getId();

        userRepository.putFriends(user1Id, user2Id);
        userRepository.putFriends(user1Id, user3Id);
        userRepository.putFriends(user3Id, user2Id);
        userRepository.putFriends(user3Id, user4Id);
        userRepository.putFriends(user3Id, user1Id);

        List<User> mutualFriends = userRepository.getMutualFriends(user1Id, user3Id);

        Assertions.assertEquals(1, mutualFriends.size(), "Должно быть 1 общий друг");
        Equals.assertEqualsUser(user2, mutualFriends.getFirst(), "Общим другом должен быть пользователь2");
    }
}
