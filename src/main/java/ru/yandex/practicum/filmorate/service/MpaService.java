package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaRepository mpaRepository;

    public MpaDto getMpa(Integer id) {
        Mpa mpa = mpaRepository.findById(id);
        return MpaMapper.MapToDto(mpa);
    }

    public List<MpaDto> getAllMpa() {
        return mpaRepository.findAll().stream()
                .map(MpaMapper::MapToDto)
                .collect(Collectors.toList());
    }

}
