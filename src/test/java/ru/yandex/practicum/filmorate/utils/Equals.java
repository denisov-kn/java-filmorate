package ru.yandex.practicum.filmorate.utils;

import org.junit.jupiter.api.Assertions;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public class Equals {
    public static void assertEqualsFilm(Film expected, Film actual, String message) {
        Assertions.assertEquals(expected.getId(), actual.getId(), message + " - Id");
        Assertions.assertEquals(expected.getName(), actual.getName(), message + " - Name");
        Assertions.assertEquals(expected.getDescription(), actual.getDescription(), message + " - Description");
        Assertions.assertEquals(expected.getDuration(), actual.getDuration(), message + " - Duration");
        Assertions.assertEquals(expected.getReleaseDate(), actual.getReleaseDate(), message + " - ReleaseDate");
    }

    public static void assertEqualsUser(User expected, User actual, String message) {

        Assertions.assertEquals(expected.getId(), actual.getId(), message + " - Id");
        Assertions.assertEquals(expected.getName(), actual.getName(), message + " - Name");
        Assertions.assertEquals(expected.getLogin(), actual.getLogin(), message + " - Login");
        Assertions.assertEquals(expected.getEmail(), actual.getEmail(), message + " - Email");
        Assertions.assertEquals(expected.getBirthday(), actual.getBirthday(), message + " - Birthday");
    }
}
