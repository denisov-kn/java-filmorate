package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;

import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    public Film createFilm(Film film) {

        try {
            mpaRepository.findById(film.getMpa().getId());
            if(film.hasGenres()) {
                Set<Integer> notExistGenreIds = genreRepository.genresNotExist(film.getGenres());
                if (!notExistGenreIds.isEmpty())
                    throw new BadRequestException("Genre c такими id не существует:" + notExistGenreIds);
            }
        } catch (NotFoundException exception) {
            throw new BadRequestException("MPA c таким id не существует: " + film.getMpa().getId());
        }

        return filmRepository.save(film);
    }



    public Film findFilmById(Integer filmId) {

        return filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с таким id не найден: " + filmId));
    }


    public Film updateFilm(Film film) {

        Film updateFilm = filmRepository.findById(film.getId())
                .map(film1 -> FilmMapper.updateFilmFields(film1, film))
                .orElseThrow(() -> new NotFoundException("Фильм с таким id: " + film.getId() + " не найден"));

        return filmRepository.update(updateFilm);
    }

    public List<Film> findAllFilms() {
        return filmRepository.findAll();
    }

    public Film putLike(Integer filmId, Integer userId) {
        checkUserById(userId);
        return filmRepository.putLike(filmId, userId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        checkUserById(userId);
        Film film = findFilmById(filmId);
        filmRepository.removeLike(filmId, userId);
        return film;
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmRepository.getPopularFilms(count);
    }

    private void checkUserById(Integer id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Пользователь с таким id: " + id + " не найден");
        }
    }

}
