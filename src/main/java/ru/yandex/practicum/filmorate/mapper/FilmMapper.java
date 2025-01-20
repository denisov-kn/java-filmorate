package ru.yandex.practicum.filmorate.mapper;
import ru.yandex.practicum.filmorate.model.Film;


public class FilmMapper {

    public static Film updateFilmFields(Film film, Film newFilm) {
        film.setName(newFilm.getName());
        film.setReleaseDate(newFilm.getReleaseDate());
        film.setDuration(newFilm.getDuration());
        if (newFilm.hasGenres()) {
            film.setGenres(newFilm.getGenres());
        }

        if (newFilm.hasDescription()) {
            film.setDescription(newFilm.getDescription());
        }

        if (newFilm.hasMpa()) {
            film.setMpa(newFilm.getMpa());
        }
        return film;
    }

}
