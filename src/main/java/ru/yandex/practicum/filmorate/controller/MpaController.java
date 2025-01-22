package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RequestMapping("/mpa")
@RequiredArgsConstructor
@RestController
public class MpaController {

    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable Integer id) {
        log.info("Входящий id mpa: " + id);
        Mpa mpa = mpaService.getMpa(id);
        log.info("Возвращаемы объект: " + mpa);
        return mpa;
    }

    @GetMapping
    public List<Mpa> getAllMpa() {
        List<Mpa> mpa = mpaService.getAllMpa();
        log.info("Возвращаемы объект: " + mpa);
        return mpa;
    }

}
