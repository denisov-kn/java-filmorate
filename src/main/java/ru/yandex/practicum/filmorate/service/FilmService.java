package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    public Film createFilm(Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        findFilmById(film.getId());
        return inMemoryFilmStorage.updateFilm(film);

    }


    public Film findFilmById(Integer id) {
        return inMemoryFilmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с таким id: " + id + " не найден"));
    }

    public Film putLike(Integer filmId, Integer userId) {
        checkUserById(userId);
        Film film = findFilmById(filmId);
        inMemoryFilmStorage.putLike(filmId,userId);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        checkUserById(userId);
        Film film = findFilmById(filmId);
        if (!inMemoryFilmStorage.isUserLikesFilm(filmId,userId))
            throw new NotFoundException("У фильма c id " + filmId
                    + " нет лайка от такого пользователя: UserId" + userId);

        inMemoryFilmStorage.deleteLike(filmId, userId);
        return film;
    }

    public Collection<Film> getPopularFilms(Integer count) {

        return inMemoryFilmStorage.getPopularFilms(count);
    }

    public Collection<Film> findAllFilms() {
       return inMemoryFilmStorage.findAllFilms();
    }

    private void checkUserById(Integer id) {
        if (!inMemoryUserStorage.isUserById(id))
            throw new NotFoundException("Пользователь с таким id: " + id + " не найден");
    }
}
