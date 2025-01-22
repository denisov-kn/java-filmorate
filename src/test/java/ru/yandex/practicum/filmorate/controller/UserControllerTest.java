package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exception.DuplicatedIdFriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.Equals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Контроллер модели User")
class UserControllerTest {

    @Autowired
    private  Validator validator;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;



    @Test
    @DisplayName("должен создавать пользователя")
    public void shouldCreateUser() {
        User user = new User();
        user.setEmail("123@gmail.com");
        user.setLogin("123");
        user.setBirthday(LocalDate.of(2000,1,1));
        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.Create.class);
        Assertions.assertEquals(0, violations.size(), "список нарушений должен быть пустым");
    }

    @Test
    @DisplayName("должен возвращать всех пользователей")
    public void shouldGetAllUsers() {

        User user1 = new User();
        user1.setEmail("123@mail.ru");
        user1.setLogin("123df");
        user1.setBirthday(LocalDate.of(2000,1,1));
        User user2 = new User();
        user2.setEmail("456@mail.ru");
        user2.setLogin("567df");
        user2.setBirthday(LocalDate.of(2000,1,1));

        userController.createUser(user1);
        userController.createUser(user2);

        Collection<User> users = userController.findAll();
        System.out.println(users);
        Assertions.assertEquals(2, users.size(),"в памяти должно быть 2 пользователя");
    }

    @Test
    @DisplayName("не должен обновлять пользователя с несуществующим id")
    public void shouldNotUpdateUserWithFakeId() {
        User user1 = new User();
        user1.setEmail("123@mail.ru");
        user1.setLogin("123df");
        user1.setBirthday(LocalDate.of(2000,1,1));
        userController.createUser(user1);
        User user2 = new User();
        user2.setEmail("456@mail.ru");
        user2.setLogin("567df");
        user2.setId(99);
        user2.setBirthday(LocalDate.of(2000,1,1));
        Assertions.assertThrows(NotFoundException.class,() -> userController.updateUser(user2),
                "пользователь с несуществующим id не должен обновляться");
    }

    @Test
    @DisplayName("должен обновлять пользователя")
    public void shouldUpdateUser() {
        User user1 = new User();
        user1.setEmail("123@mail.ru");
        user1.setLogin("123df");
        user1.setName("vik");
        user1.setBirthday(LocalDate.of(1986,2,15));

        Integer id = userController.createUser(user1).getId();

        User user2 = new User();
        user2.setId(id);
        user2.setEmail("12356@mail.ru");
        user2.setLogin("123565df");
        user2.setName("vik34");
        user2.setBirthday(LocalDate.of(1984,5,20));

        Equals.assertEqualsUser(user2, userController.updateUser(user2), "пользователь должен обновляться");
    }

    @Test
    @DisplayName("не должен создавать пользователя с пустым email")
    public void shouldNotCreateUserWithBlankEmail() {
        User user = new User();
        user.setLogin("123df");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.Create.class);

        Assertions.assertEquals(true, violations.stream()
                        .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                                && violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()
                                .equals("NotBlank")),
                "должна быть violation поле email и аннотация NotBlank"
        );

    }

    @Test
    @DisplayName("не должен создавать пользователя с email не по формату")
    public void shouldNotCreateUserWithNotFormatEmail() {
        User user = new User();
        user.setLogin("123df");
        user.setEmail("12356-mail");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.Create.class);

        Assertions.assertEquals(true, violations.stream()
                        .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                                && violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()
                                .equals("Email")),
                "должна быть violation поле email и аннотация Email"
        );
    }

    @Test
    @DisplayName("не должен создавать пользователя c пустым логином")
    public void shouldNotCreateUserWithBlankLogin() {
        User user = new User();
        user.setEmail("12356@mail.ru");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.Create.class);

        Assertions.assertEquals(true, violations.stream()
                        .anyMatch(violation -> violation.getPropertyPath().toString().equals("login")
                                && violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()
                                .equals("NotBlank")),
                "должна быть violation поле login и аннотация NotBlank"
        );
    }

    @Test
    @DisplayName("должен правильно валидировать login если он содержит пробелы")
    public void shouldValidateLoginIfContainsSpace() {

        User user = new User();
        user.setLogin("12 3 df");
        user.setEmail("12356@mail.ru");


        Set<ConstraintViolation<User>> violations = validator.validate(user);

        Assertions.assertEquals(true, violations.stream()
                        .anyMatch(violation -> violation.getPropertyPath().toString().equals("validateLogin")
                                && violation.getMessage().equals("Логин не может содержать пробелы")),
                "должна быть violation по методу isValidateLogin и верным сообщением"
        );
    }

    @Test
    @DisplayName("должен правильно валидировать день рождения если он в будущем")
    public void shouldValidateBirthdayIfItInFuture() {
        User user = new User();
        user.setLogin("123df");
        user.setEmail("12356@mail.ru");
        user.setBirthday(LocalDate.of(2040, 12,2));

        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.Create.class);

        Assertions.assertEquals(true, violations.stream()
                        .anyMatch(violation -> violation.getPropertyPath().toString().equals("birthday")
                                && violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()
                                .equals("Past")),
                "должна быть violation поле birthday и аннотация Past"
        );
    }

    @Test
    @DisplayName("должен находить пользователя по id")
    public void shouldFindUserById() {
        User user = new User();
        user.setLogin("123df");
        user.setEmail("12356@mail.ru");
        user.setBirthday(LocalDate.of(2015, 12,2));

        userController.createUser(user);

        Assertions.assertEquals(user, userController.findUserById(user.getId()),
                "Пользователь должен находиться по id");

    }

    @Test
    @DisplayName("не должен находить пользователь с несуществующим id")
    public void shouldNotFindUserByFakeId() {
        User user = new User();
        user.setLogin("123df");
        user.setEmail("12356@mail.ru");
        user.setBirthday(LocalDate.of(2005, 12,2));

        userController.createUser(user);


        Assertions.assertThrows(NotFoundException.class,() -> userController.findUserById(9999),
                "пользователь с несуществующим id не должен быть найден");
    }


    @Test
    @DisplayName("должен корректно добавлять друзей")
    public void shouldPutFriend() {
        User user1 = new User();
        user1.setLogin("123df");
        user1.setEmail("12356@mail.ru");
        user1.setBirthday(LocalDate.of(2015, 12,2));
        userController.createUser(user1);

        User user2 = new User();
        user2.setLogin("12345df");
        user2.setEmail("12356454@mail.ru");
        user2.setBirthday(LocalDate.of(2015, 12,2));
        userController.createUser(user2);

        userController.putFriend(user1.getId(), user2.getId());

        Assertions.assertTrue(userRepository.isFriends(user1.getId(), user2.getId()),
                "У пользователя1 должен быть друг пользователь2");

    }

    @Test
    @DisplayName("не должен добавлять другом несуществующего пользователя")
    public void shouldNotPutFriend() {
        User user1 = new User();
        user1.setLogin("123df");
        user1.setEmail("12356@mail.ru");
        user1.setBirthday(LocalDate.of(2015, 12,2));
        userController.createUser(user1);

        User user2 = new User();
        user2.setLogin("12345df");
        user2.setEmail("12356454@mail.ru");
        user2.setBirthday(LocalDate.of(2015, 12,2));
        userController.createUser(user2);

        Assertions.assertThrows(NotFoundException.class,() -> userController.putFriend(user1.getId(), 9999),
                "не должен добавлять другом несуществующего пользователя");
        Assertions.assertThrows(NotFoundException.class,() -> userController.putFriend(99999, user2.getId()),
                "не должен добавлять другом несуществующего пользователя");
    }

    @Test
    @DisplayName("Должен получать список друзей")
    public void shouldGetFriends() {
        User user1 = new User();
        user1.setLogin("123df");
        user1.setEmail("12356@mail.ru");
        user1.setBirthday(LocalDate.of(2015, 12,2));
        userController.createUser(user1);

        User user2 = new User();
        user2.setLogin("12345df");
        user2.setEmail("12356454@mail.ru");
        user2.setBirthday(LocalDate.of(2015, 12,2));
        userController.createUser(user2);

        User user3 = new User();
        user3.setLogin("1234545df");
        user3.setEmail("123543546454@mail.ru");
        user3.setBirthday(LocalDate.of(2015, 12,2));
        userController.createUser(user3);

        userController.putFriend(user2.getId(), user1.getId());
        userController.putFriend(user2.getId(), user3.getId());

        Assertions.assertTrue(userController.getFriends(user2.getId()).contains(user1),
                "У пользователя2 должен быть друг пользователь1 в списке друзей");
        Assertions.assertTrue(userController.getFriends(user2.getId()).contains(user3),
                "У пользователя2 должен быть друг пользователь3 в списке друзей");
        Assertions.assertEquals(2, userController.getFriends(user2.getId()).size(),
                "У пользователя должно быть 2 друга");
    }


    @Test
    @DisplayName("Должен получать общих друзей")
    public void shouldGetMutualFriends() {

        List<User> listUsers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setLogin("123" + i);
            user.setEmail("12356" + i + "@mail.ru");
            user.setBirthday(LocalDate.of(2015, 12,2));
            userController.createUser(user);
            listUsers.add(user);
        }

        userController.putFriend(listUsers.get(0).getId(), listUsers.get(1).getId());
        userController.putFriend(listUsers.get(0).getId(), listUsers.get(2).getId());
        userController.putFriend(listUsers.get(0).getId(), listUsers.get(3).getId());
        userController.putFriend(listUsers.get(4).getId(), listUsers.get(2).getId());
        userController.putFriend(listUsers.get(4).getId(), listUsers.get(3).getId());


        Assertions.assertTrue(userController.getMutualFriends(listUsers.get(0).getId(),
                        listUsers.get(4).getId()).contains(listUsers.get(2)),
                "У пользователей 0 и 4  должен быть друг пользователь2 в списке общих друзей");
        Assertions.assertTrue(userController.getMutualFriends(listUsers.get(0).getId(),
                        listUsers.get(4).getId()).contains(listUsers.get(3)),
                "У пользователей 0 и 4  должен быть друг пользователь3 в списке общих друзей");
        Assertions.assertFalse(userController.getMutualFriends(listUsers.get(0).getId(),
                listUsers.get(4).getId()).contains(listUsers.get(1)),
                "У пользователей 0 и 4  не должен быть друг пользователь1 в списке общих друзей");
        Assertions.assertEquals(2, userController.getMutualFriends(listUsers.get(0).getId(),
                        listUsers.get(4).getId()).size(),
                "У пользователей должно быть 2 общих друга");
    }

    @Test
    @DisplayName("пользователь не должен добавлять другом самого себя")
    public void shouldNotPutFriendHimself() {
        User user1 = new User();
        user1.setLogin("123df");
        user1.setEmail("12356@mail.ru");
        user1.setBirthday(LocalDate.of(2012, 12,2));
        userController.createUser(user1);

        Assertions.assertThrows(DuplicatedIdFriendsException.class,
                () -> userController.putFriend(user1.getId(), user1.getId()),
                "пользователь не должен добавлять другом самого себя");
    }


}
