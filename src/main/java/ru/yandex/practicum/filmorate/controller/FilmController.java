package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {

    private Map<Integer, Film> films = new HashMap<>();


    @Validated(Marker.Create.class)
    @PostMapping
    public Film createFilm(@RequestBody @Valid  Film film) {
        log.info("Входящая объект: " + film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Созданный объект " + film);
        return film;
    }

    @Validated(Marker.Update.class)
    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Входящая объект: " + film);
        if (!films.containsKey(film.getId()))
            throw new NotFoundException("Фильм с таким id: " + film.getId() + " не найден");
        films.replace(film.getId(), film);
        log.info("Обновленный объект: " + film);
        return  film;
    }


    @GetMapping
    public Collection<Film> findAll() {
        log.info("Возвращаемый массив фильмов: " + films.values());
        return new ArrayList<>(films.values());
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
