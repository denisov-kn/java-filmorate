package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<GenreDto> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(GenreMapper::genreToDto)
                .collect(Collectors.toList());
    }

    public GenreDto getGenreById(int id) {
        Genre genre = genreRepository.findById(id);
        return GenreMapper.genreToDto(genre);
    }
}
