package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    public Film createFilm(Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if (!inMemoryFilmStorage.isFilmById(film.getId()))
            throw new NotFoundException("Фильм с таким id: " + film.getId() + " не найден");
        return inMemoryFilmStorage.updateFilm(film);

    }

    public Collection<Film> findAllFilms() {
       return inMemoryFilmStorage.findAllFilms();
    }
}
