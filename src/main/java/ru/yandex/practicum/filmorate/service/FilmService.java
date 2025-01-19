package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    public FilmDto createFilm(NewFilmRequest request) {

        Film film = FilmMapper.mapToFilm(request);
        /*
        try {
            mpaRepository.findById(request.getMpa().getId());
            List<Integer> notExistGenreIds = genreRepository.genresNotExist(request.getGenres());
            if(!notExistGenreIds.isEmpty())
                throw new BadRequestException("MPA c такими id не существует:" + notExistGenreIds);
        } catch (NotFoundException exception) {
            throw new BadRequestException("MPA c таким id не существует: " + request.getMpa().getId());
        }
        */
        film = filmRepository.save(film);
        return FilmMapper.mapToFilmDto(film);
    }



    public FilmDto findFilmById(Integer filmId) {

        return filmRepository.findById(filmId)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильм с таким id не найден: " + filmId));
    }


    public FilmDto updateFilm(UpdateFilmRequest request) {

        Film updateFilm = filmRepository.findById(request.getId())
                .map(film -> FilmMapper.updateFilmFields(film, request))
                .orElseThrow(() -> new NotFoundException("Фильм с таким id: " + request.getId() + " не найден"));

        updateFilm = filmRepository.update(updateFilm);
        return FilmMapper.mapToFilmDto(updateFilm);
    }

    public List<FilmDto> findAllFilms() {
        return filmRepository.findAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto putLike(Integer filmId, Integer userId) {
        checkUserById(userId);
        Film film  = filmRepository.putLike(filmId, userId);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto deleteLike(Integer filmId, Integer userId) {
        checkUserById(userId);
        FilmDto filmDto = findFilmById(filmId);
        filmRepository.removeLike(filmId, userId);
        return filmDto;
    }

    public List<FilmDto> getPopularFilms(Integer count) {
        return filmRepository.getPopularFilms(count).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }



    private void checkUserById(Integer id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Пользователь с таким id: " + id + " не найден");
        }

    }

}
