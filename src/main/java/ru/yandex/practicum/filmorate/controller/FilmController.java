package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;


    @Validated
    @PostMapping
    public FilmDto createFilm(@RequestBody @Valid NewFilmRequest request) {
        log.info("Входящая объект: " + request);
        FilmDto filmDto =  filmService.createFilm(request);
        log.info("Созданный объект " + filmDto);
        return filmDto;
    }

    @Validated
    @PutMapping
    public FilmDto updateFilm(@RequestBody @Valid UpdateFilmRequest request) {
        log.info("Входящая объект: " + request);
        FilmDto filmDto = filmService.updateFilm(request);
        log.info("Обновленный объект: " + filmDto);
        return filmDto;
    }

    @GetMapping
    public List<FilmDto> findAll() {
        List<FilmDto> films = filmService.findAllFilms();
        log.info("Возвращаемый массив фильмов: " + films);
        return films;
    }

    @GetMapping ("/{id}")
    public FilmDto getFilmById(@PathVariable Integer id) {
        log.info("Входящая id: " + id);
        FilmDto filmDto = filmService.findFilmById(id);
        log.info("Возвращаемы объект: " + filmDto);
        return filmDto;
    }

    @PutMapping ("/{id}/like/{userId}")
    public FilmDto putLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Входящий id фильма: " + id);
        log.info("Входящий userId: " + userId);
        FilmDto filmDto = filmService.putLike(id, userId);
        log.info("Возвращаемы объект: " + filmDto);
        return filmDto;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public FilmDto deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Входящий id фильма: " + id);
        log.info("Входящий userId: " + userId);
        FilmDto filmDto = filmService.deleteLike(id, userId);
        log.info("Возвращаемы объект: " + filmDto);
        return filmDto;
    }

    @GetMapping("/popular")
    public List<FilmDto> getPopularFilms(@RequestParam(defaultValue = "10") final Integer count) {
        log.info("Входящий count: " + count);
        List<FilmDto> filmsDto = filmService.getPopularFilms(count);
        log.info("Возвращаемый массив фильмов: " + filmsDto);
        return filmsDto;
    }
}
