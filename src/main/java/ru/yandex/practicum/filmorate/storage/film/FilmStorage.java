package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> findAllFilms();

    boolean isFilmById(Integer id);

    Film findFilmById(Integer id);

    void putLike(Integer filmId, Integer userId);

    boolean isUserLikesFilm(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    Collection<Film> getPopularFilms(Integer count);

}
