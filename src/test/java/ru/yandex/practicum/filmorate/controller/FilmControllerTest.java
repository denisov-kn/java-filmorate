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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.utils.Equals;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@ActiveProfiles("test")
@DisplayName("Контролер модели Film")
class FilmControllerTest {

    private static final Validator validator;
    private FilmController filmController;
    private InMemoryFilmStorage inMemoryFilmStorage;
    private FilmService filmService;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();


    }

    @BeforeEach
    public void beforeEach() {
        inMemoryFilmStorage  = new InMemoryFilmStorage();
        filmService = new FilmService(inMemoryFilmStorage);
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
        newFilm.setName("Фильм2");
        newFilm.setId(1);
        newFilm.setName("Фильм1");
        newFilm.setDescription("Описание1");
        newFilm.setDuration(100);
        newFilm.setReleaseDate(LocalDate.of(1999, 3, 1));
        Equals.assertEqualsFilm(newFilm, filmController.updateFilm(newFilm), "фильм должен обновляться");
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





}
