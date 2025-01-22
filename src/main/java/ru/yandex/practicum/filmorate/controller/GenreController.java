package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) {
        log.info("Входящий id: " + id);
        Genre genre = genreService.getGenreById(id);
        log.info("Исходящий объект: " + genre);
        return genre;
    }

    @GetMapping
    public List<Genre> getAllGenre() {
        List<Genre> genres = genreService.getAllGenres();
        log.info("Исходящий объект: " + genres);
        return genres;
    }



}
