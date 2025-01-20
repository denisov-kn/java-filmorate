package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRepository;

import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaRepository mpaRepository;

    public Mpa getMpa(Integer id) {
        return mpaRepository.findById(id);

    }

    public List<Mpa> getAllMpa() {
        return mpaRepository.findAll();
    }

}
