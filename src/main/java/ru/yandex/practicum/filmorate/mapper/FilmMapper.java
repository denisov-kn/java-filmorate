package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;


public class FilmMapper {

    public static Film mapToFilm(NewFilmRequest request) {
        Film film = new Film();
        film.setMpa(request.getMpa());
        film.setName(request.getName());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        film.setGenres(request.getGenres());
        film.setDescription(request.getDescription());
        return film;
    }

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        filmDto.setGenres(film.getGenres());
        filmDto.setDescription(film.getDescription());
        filmDto.setMpa(film.getMpa());
        return  filmDto;
    }


    public static Film updateFilmFields(Film film, UpdateFilmRequest request) {
        film.setName(request.getName());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        if (request.hasGenres()) {
            film.setGenres(request.getGenres());
        }

        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }

        if (request.hasMpa()) {
            film.setMpa(request.getMpa());
        }
        return film;
    }

}
