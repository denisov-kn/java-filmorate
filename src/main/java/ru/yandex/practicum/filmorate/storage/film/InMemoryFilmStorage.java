package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
@Component

public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {

        return film;
    }

    @Override
    public Collection<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean isFilmById(Integer id) {
        return films.containsKey(id);
    }

    @Override
    public Film findFilmById(Integer id) {
        return films.get(id);
    }


    private Integer getNextId() {
        Integer currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
