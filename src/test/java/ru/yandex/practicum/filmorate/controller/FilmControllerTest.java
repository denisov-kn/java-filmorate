package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.utils.Equals;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@ActiveProfiles("test")
@SpringBootTest
@DisplayName("Контролер модели Film")
class FilmControllerTest {

    private static final Validator validator;
    private FilmController filmController;
    private UserController userController;
    private InMemoryFilmStorage  inMemoryFilmStorage;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();


    }

    @BeforeEach
    public void beforeEach() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        UserService userService = new UserService(inMemoryUserStorage);
        FilmService filmService = new FilmService(inMemoryFilmStorage, inMemoryUserStorage);
        userController = new UserController(userService);
        filmController = new FilmController(filmService);

    }

    @Test
    @DisplayName("должен создавать фильм")
    public void shouldCreateFilm() {
        Film film = new Film();
        film.setName("Фильм1");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000,1,1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film, Marker.Create.class);
        Assertions.assertEquals(0, violations.size(), "список нарушений должен быть пустым");
    }

    @Test
    @DisplayName("должен возвращать все фильмы")
    public void shouldGetAllFilms() {
        Film film = new Film();
        film.setName("Фильм1");
        Film film2 = new Film();
        film2.setName("Фильм2");
        filmController.createFilm(film);
        filmController.createFilm(film2);

        Collection<Film> films = filmController.findAll();
        Assertions.assertEquals(2, films.size(),"в памяти должно быть 2 фильма");
    }

    @Test
    @DisplayName("не должен обновлять фильм с несуществующим id")
    public void shouldNotUpdateFilmWithFakeId() {
        Film film = new Film();
        film.setName("Фильм1");
        filmController.createFilm(film);
        Film newFilm = new Film();
        newFilm.setName("Фильм2");
        newFilm.setId(99);
        Assertions.assertThrows(NotFoundException.class,() -> filmController.updateFilm(newFilm),
                "фильм с несуществующим id не должен обновляться");
    }

    @Test
    @DisplayName("должен обновлять фильм")
    public void shouldUpdateFilm() {
        Film film = new Film();
        film.setName("Фильм1");
        film.setDescription("Описание1");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000, 2, 13));

        filmController.createFilm(film);

        Film newFilm = new Film();
        newFilm.setId(film.getId());
        newFilm.setName("Фильм2");
        newFilm.setDescription("Описание2");
        newFilm.setDuration(66);
        newFilm.setReleaseDate(LocalDate.of(1999, 3, 1));
        filmController.updateFilm(newFilm);
        Equals.assertEqualsFilm(newFilm, inMemoryFilmStorage.findFilmById(film.getId()), "фильм должен обновляться");
    }

    @Test
    @DisplayName("должен правильно валидировать пустое название фильма")
    public void shouldValidateName() {
        Film film = new Film();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, Marker.Create.class);

        Assertions.assertEquals(true, violations.stream()
                        .anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                                && violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()
                                .equals("NotBlank")),
                "должна быть violation полем name и аннотацией NotBlank"
        );
    }

    @Test
    @DisplayName("должен правильно валидировать длинное описание фильма")
    public void shouldValidateDescription() {
        Film film = new Film();
        film.setName("Фильм1");
        film.setDescription("A".repeat(201));
        Set<ConstraintViolation<Film>> violations = validator.validate(film, Marker.Create.class);

        Assertions.assertEquals(true, violations.stream()
                        .anyMatch(violation -> violation.getPropertyPath().toString().equals("description")
                                && violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()
                                .equals("Size")),
                "должна быть violation полем description и аннотацией Size"
        );
    }

    @Test
    @DisplayName("должен правильно валидировать дату если она в будущем")
    public void shouldValidateReleaseDateFuture() {
        Film film = new Film();
        film.setName("Фильм1");
        film.setReleaseDate(LocalDate.of(2100,1,1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film, Marker.Create.class);

        Assertions.assertEquals(true, violations.stream()
                        .anyMatch(violation -> violation.getPropertyPath().toString().equals("releaseDate")
                                && violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()
                                .equals("Past")),
                "должна быть violation полем releaseDate и аннотацией Past"
        );
    }

    @Test
    @DisplayName("должен правильно валидировать дату релиза, если она раньше чем был создан первый фильм")
    public void shouldValidateReleaseDateBeforeFilmsCreate() {
        Film film = new Film();
        film.setName("Фильм1");
        film.setReleaseDate(LocalDate.of(1894,12,28));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        Assertions.assertEquals(true, violations.stream()
                        .anyMatch(violation -> violation.getPropertyPath().toString().equals("validateReleaseDate")
                                && violation.getMessage().equals("Дата фильма не может быть раньше чем 28.12.1895")),
                "должна быть violation по методу validateReleaseDate и верным сообщением"
        );
    }

    @Test
    @DisplayName("должен находить фильм по id")
    public void shouldFindFilmById() {
        Film film = new Film();
        film.setName("Фильм1");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000,1,1));

        filmController.createFilm(film);

        Assertions.assertEquals(film, filmController.getFilmById(film.getId()),
                "Фильм должен находиться по id");

    }

    @Test
    @DisplayName("не должен находить фильм с несуществующим id")
    public void shouldNotFindFilmByFakeId() {
        Film film = new Film();
        film.setName("Фильм1");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000,1,1));

        filmController.createFilm(film);


        Assertions.assertThrows(NotFoundException.class,() -> filmController.getFilmById(9999),
                "фильм с несуществующим id не должен быть найден");
    }


    @Test
    @DisplayName("должен корректно ставить лайк")
    public void shouldPutLike() {
        Film film = new Film();
        film.setName("Фильм1");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000,1,1));

        filmController.createFilm(film);

        User user = new User();
        user.setLogin("12 3 df");
        user.setEmail("12356@mail.ru");

        userController.createUser(user);

        filmController.putLike(film.getId(), user.getId());

        Assertions.assertTrue(inMemoryFilmStorage.isUserLikesFilm(film.getId(), user.getId()),
                "У фильма должен быть лайк от пользователя");

    }

    @Test
    @DisplayName("лайк не должен ставится если фильм не существует")
    public void shouldNotPutLikeIfFilmDoesNotExist() {
        Film film = new Film();
        film.setName("Фильм1");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000,1,1));

        filmController.createFilm(film);

        User user = new User();
        user.setLogin("12 3 df");
        user.setEmail("12356@mail.ru");

        userController.createUser(user);

        Assertions.assertThrows(NotFoundException.class,() -> filmController.putLike(999, user.getId()),
                "лайк не должен ставится если фильм не существует");

    }


    @Test
    @DisplayName("лайк не должен ставится если пользователь не существует")
    public void shouldNotPutLikeIfUserDoesNotExist() {
        Film film = new Film();
        film.setName("Фильм1");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000,1,1));

        filmController.createFilm(film);

        User user = new User();
        user.setLogin("12 3 df");
        user.setEmail("12356@mail.ru");

        userController.createUser(user);

        Assertions.assertThrows(NotFoundException.class,() -> filmController.putLike(film.getId(), 999),
                "лайк не должен ставится если фильм не существует");
    }

    @Test
    @DisplayName("должен корректно удаляться лайк")
    public void shouldDeleteLike() {

        Film film = new Film();
        film.setName("Фильм1");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000,1,1));
        filmController.createFilm(film);

        User user1 = new User();
        user1.setLogin("12 3 df");
        user1.setEmail("12356@mail.ru");
        userController.createUser(user1);

        User user2 = new User();
        user2.setLogin("12356 df");
        user2.setEmail("1235666@mail.ru");
        userController.createUser(user2);

        filmController.putLike(film.getId(), user1.getId());
        filmController.putLike(film.getId(), user2.getId());

        Assertions.assertTrue(inMemoryFilmStorage.isUserLikesFilm(film.getId(), user1.getId()),
                "У фильма должен быть лайк от пользователя1 до удаления");

        filmController.deleteLike(film.getId(), user1.getId());

        Assertions.assertFalse(inMemoryFilmStorage.isUserLikesFilm(film.getId(), user1.getId()),
                "У фильма не должен быть лайка от пользователя1 после удаления");
        Assertions.assertTrue(inMemoryFilmStorage.isUserLikesFilm(film.getId(), user2.getId()),
                "Лайк от пользователя2 должен остаться");
    }

    @Test
    @DisplayName("должен корректно выводится список популярных фильмов")
    public void shouldGetPopularFilms() {

        List<Film> listFilms = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Film film = new Film();
            film.setName("Фильм1");
            film.setDuration(100);
            film.setReleaseDate(LocalDate.of(2000,1,1));
            filmController.createFilm(film);
            listFilms.add(film);
        }

        List<User> listUsers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setLogin("12df" + i);
            user.setEmail("12356" + i + "@mail.ru");
            userController.createUser(user);
            listUsers.add(user);
        }

        for (int i = 0; i < 5; i++) {

            filmController.putLike(listFilms.get(5).getId(), listUsers.get(i).getId());
        }

        for (int i = 0; i < 4; i++) {

            filmController.putLike(listFilms.get(10).getId(), listUsers.get(i).getId());
        }

        for (int i = 0; i < 2; i++) {
            filmController.putLike(listFilms.get(12).getId(), listUsers.get(i).getId());
        }

        List<Film> popularFilms = new ArrayList<>(filmController.getPopularFilms(9));

        Assertions.assertEquals(popularFilms.get(0),listFilms.get(5),
                "Первый фильм в списке - 5");

        Assertions.assertEquals(popularFilms.get(1),listFilms.get(10),
                "Второй фильм в списке - 10");

        Assertions.assertEquals(popularFilms.get(2),listFilms.get(12),
                "Третий фильм в списке - 12");

        Assertions.assertEquals(3, popularFilms.size(),
                "Список должен содержать 9 фильмов");

    }

}
