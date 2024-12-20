package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<Integer, Set<Integer>> likes = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
       films.replace(film.getId(), film);
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

    @Override
    public void putLike(Integer filmId, Integer userId) {
        if (!likes.containsKey(filmId))
            likes.put(filmId, new HashSet<>());
        likes.get(filmId).add(userId);
    }

    @Override
    public boolean isUserLikesFilm(Integer filmId, Integer userId) {
        return likes.containsKey(filmId) && likes.get(filmId).contains(userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        likes.get(filmId).remove(userId);
    }

    @Override
    public Collection<Film> getPopularFilms(Integer count) {
        return likes.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()))
                .limit(count)
                .map(entry -> films.get(entry.getKey()))
                .collect(Collectors.toList());
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
