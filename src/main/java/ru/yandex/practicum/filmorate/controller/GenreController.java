package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/{id}")
    public GenreDto getGenreById(@PathVariable int id) {
        log.info("Входящий id: " + id);
        GenreDto genreDto = genreService.getGenreById(id);
        log.info("Исходящий объект: " + genreDto);
        return genreDto;
    }

    @GetMapping
    public List<GenreDto> getAllGenre() {
        List<GenreDto> genresDto = genreService.getAllGenres();
        log.info("Исходящий объект: " + genresDto);
        return genresDto;
    }



}
