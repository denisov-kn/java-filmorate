package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

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
    //    log.info("Возвращаемый массив фильмов: " + films.values());
        return filmService.findAllFilms();
    }




}
