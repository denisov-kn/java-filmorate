package ru.yandex.practicum.filmorate.mapper;
import ru.yandex.practicum.filmorate.model.Film;


public class FilmMapper {

    /*

    public static Film mapToFilm(Film film1) {
        Film film = new Film();
        film.setMpa(film1.getMpa());
        film.setName(film1.getName());
        film.setReleaseDate(film1.getReleaseDate());
        film.setDuration(film1.getDuration());
        film.setGenres(film1.getGenres());
        film.setDescription(film1.getDescription());
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

*/
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
