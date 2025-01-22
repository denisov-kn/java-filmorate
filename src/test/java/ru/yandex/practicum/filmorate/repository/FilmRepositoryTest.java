package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.Equals;
import java.time.LocalDate;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Import({FilmRepository.class, FilmRowMapper.class, UserRepository.class, UserRowMapper.class})
@DisplayName("Репозиторий Film")
public class FilmRepositoryTest {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    static Film getFilm() {
        Film film = new Film();
        film.setName("testFilmName" + UUID.randomUUID());
        film.setDuration((int)(Math.random() * 100) + 1);
        film.setReleaseDate(LocalDate.of(2010,1,1));
        Mpa mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("G");
        film.setMpa(mpa);
        film.setGenres(new HashSet<>());
        return film;
    }

    @Test
    @DisplayName("Должен сохранять  / находить  фильм по id")
    public void shouldSaveFindFilmById() {
        Film filmTest = filmRepository.save(getFilm());
        Integer filmTestId = filmTest.getId();

        Optional<Film> film = filmRepository.findById(filmTestId);

        assertThat(film)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(filmTest);

    }

    @Test
    @DisplayName("Должен обновлять фильм")
    public void shouldUpdateFilm() {

        Film film = filmRepository.save(getFilm());
        Integer filmId = film.getId();
        Film filmUpdate = getFilm();
        filmUpdate.setId(filmId);

        filmRepository.update(filmUpdate);

        assertThat(filmRepository.findById(filmId))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(filmUpdate);
    }


    @Test
    @DisplayName("Должен добавлять лайк")
    public void shouldAddLike() {
        Film film = filmRepository.save(getFilm());
        User user = userRepository.save(UserRepositoryTest.getTestUser());
        filmRepository.putLike(film.getId(), user.getId());
        Assertions.assertTrue(filmRepository.isUserLikesFilm(film.getId(), user.getId()),
                "У фильма есть лайк от пользователя");
    }

    @Test
    @DisplayName("Должен удалять лайк")
    public void shouldRemoveLike() {
        Film film = filmRepository.save(getFilm());
        User user = userRepository.save(UserRepositoryTest.getTestUser());

        Assertions.assertFalse(filmRepository.isUserLikesFilm(film.getId(), user.getId()),
                "У фильма не должно быть лайка от пользователя");
        filmRepository.putLike(film.getId(), user.getId());
        Assertions.assertTrue(filmRepository.isUserLikesFilm(film.getId(), user.getId()),
                "У фильма есть лайк от пользователя");
        filmRepository.removeLike(film.getId(), user.getId());
        Assertions.assertFalse(filmRepository.isUserLikesFilm(film.getId(), user.getId()),
                "У фильма не должно быть лайка от пользователя");
    }


    @Test
    @DisplayName("Должен вовращать самые популярные фильма ")
    public void shouldGetPopularFilms() {

        List<Film> listFilms = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Film film = filmRepository.save(getFilm());
            listFilms.add(film);
        }

        List<User> listUsers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = userRepository.save(UserRepositoryTest.getTestUser());
            listUsers.add(user);
        }

        for (int i = 0; i < 5; i++) {

            filmRepository.putLike(listFilms.get(5).getId(), listUsers.get(i).getId());
        }

        for (int i = 0; i < 4; i++) {

            filmRepository.putLike(listFilms.get(10).getId(), listUsers.get(i).getId());
        }

        for (int i = 0; i < 2; i++) {
            filmRepository.putLike(listFilms.get(12).getId(), listUsers.get(i).getId());
        }

        List<Film> popularFilms = new ArrayList<>(filmRepository.getPopularFilms(9));

        Equals.assertEqualsFilm(popularFilms.get(0),listFilms.get(5),
                "Первый фильм в списке - 5");

        Equals.assertEqualsFilm(popularFilms.get(1),listFilms.get(10),
                "Второй фильм в списке - 10");

        Equals.assertEqualsFilm(popularFilms.get(2),listFilms.get(12),
                "Третий фильм в списке - 12");

        Assertions.assertEquals(9, popularFilms.size(),
                "Список должен содержать 9 фильмов");

    }

}






