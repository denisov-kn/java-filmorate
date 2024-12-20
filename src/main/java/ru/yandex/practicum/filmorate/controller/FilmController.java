package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;


    @Validated(Marker.Create.class)
    @PostMapping
    public Film createFilm(@RequestBody @Valid  Film film) {
        log.info("Входящая объект: " + film);
        filmService.createFilm(film);
        log.info("Созданный объект " + film);
        return film;
    }

    @Validated(Marker.Update.class)
    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Входящая объект: " + film);
        filmService.updateFilm(film);
        log.info("Обновленный объект: " + film);
        return film;
    }

    @GetMapping
    public Collection<Film> findAll() {
        Collection<Film> films = filmService.findAllFilms();
        log.info("Возвращаемый массив фильмов: " + films);
        return films;
    }

    @GetMapping ("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        log.info("Входящая id: " + id);
        Film film = filmService.findFilmById(id);
        log.info("Возвращаемы объект: " + film);
        return film;
    }

    @PutMapping ("/{id}/like/{userId}")
    public Film putLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Входящий id фильма: " + id);
        log.info("Входящий userId: " + userId);
        Film film = filmService.putLike(id, userId);
        log.info("Возвращаемы объект: " + film);
        return film;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Входящий id фильма: " + id);
        log.info("Входящий userId: " + userId);
        Film film = filmService.deleteLike(id, userId);
        log.info("Возвращаемы объект: " + film);
        return film;
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") final Integer count) {
        log.info("Входящий count: " + count);
        Collection<Film> films = filmService.getPopularFilms(count);
        log.info("Возвращаемый массив фильмов: " + films);
        return films;
    }
}
