package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

interface FilmStorage {

    Film addFilm(Film film);
    Film updateFilm(Film film);
    Collection<Film> findAllFilms();
    boolean isFilmById(Integer id);
    Film findFilmById(Integer id);


}
