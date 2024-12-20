package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final UserService userService;

    public Film createFilm(Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        checkFilmById(film.getId());
        return inMemoryFilmStorage.updateFilm(film);

    }


    public Film findFilmById(Integer id) {
        checkFilmById(id);
        return inMemoryFilmStorage.findFilmById(id);
    }

    public Film putLike(Integer filmId, Integer userId) {
        checkFilmById(filmId);
        Film film = inMemoryFilmStorage.findFilmById(filmId);
        film.getLikes().add(userId);
        User user = userService.findUserById(userId);
        user.getFilmsLikes().add(filmId);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        checkFilmById(filmId);
        userService.checkUserById(userId);
        Film film = inMemoryFilmStorage.findFilmById(filmId);

        if (!film.getLikes().contains(userId))
            throw new NotFoundException("У фильма c id " + filmId
                    + " нет лайка от такого пользователя: UserId" + userId);
        film.getLikes().remove(userId);
        User user = userService.findUserById(userId);
        user.getFilmsLikes().remove(userId);
        return film;
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return inMemoryFilmStorage.findAllFilms().stream()
                .sorted((film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Collection<Film> findAllFilms() {
       return inMemoryFilmStorage.findAllFilms();
    }


    private void checkFilmById(Integer id) {
        if (!inMemoryFilmStorage.isFilmById(id))
            throw new NotFoundException("Фильм с таким id: " + id + " не найден");
    }
}
