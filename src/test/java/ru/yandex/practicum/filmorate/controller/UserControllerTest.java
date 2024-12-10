package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.Equals;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@ActiveProfiles("test")
@DisplayName("Контроллер модели User")
class UserControllerTest {

    private static final Validator validator;
    private UserController userController;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
    }

    @Test
    @DisplayName("должен создавать пользователя")
    public void shouldCreateUser() {
        User user = new User();
        user.setEmail("123@gmail.com");
        user.setLogin("123");
        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.Create.class);
        Assertions.assertEquals(0, violations.size(), "список нарушений должен быть пустым");
    }

    @Test
    @DisplayName("должен возвращать всех пользователей")
    public void shouldGetAllUsers() {

        User user1 = new User();
        user1.setEmail("123@mail.ru");
        user1.setLogin("123df");
        User user2 = new User();
        user2.setEmail("456@mail.ru");
        user2.setLogin("567df");

        userController.createUser(user1);
        userController.createUser(user2);

        Collection<User> users = userController.findAll();
        Assertions.assertEquals(2, users.size(),"в памяти должно быть 2 пользователя");
    }

    @Test
    @DisplayName("не должен обновлять пользователя с несуществующим id")
    public void shouldNotUpdateUserWithFakeId() {
        User user1 = new User();
        user1.setEmail("123@mail.ru");
        user1.setLogin("123df");
        userController.createUser(user1);
        User user2 = new User();
        user2.setEmail("456@mail.ru");
        user2.setLogin("567df");
        user2.setId(99);
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

        userController.createUser(user1);

        User user2 = new User();
        user2.setId(1);
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

}
