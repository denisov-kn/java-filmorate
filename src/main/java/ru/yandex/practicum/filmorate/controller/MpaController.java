package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RequestMapping("/mpa")
@RequiredArgsConstructor
@RestController
public class MpaController {

    private final MpaService mpaService;

    @GetMapping("/{id}")
    public MpaDto getMpa(@PathVariable Integer id) {
        log.info("Входящий id mpa: " + id);
        MpaDto mpaDto = mpaService.getMpa(id);
        log.info("Возвращаемы объект: " + mpaDto);
        return mpaDto;
    }

    @GetMapping
    public List<MpaDto> getAllMpa() {
        List<MpaDto> mpaDto = mpaService.getAllMpa();
        log.info("Возвращаемы объект: " + mpaDto);
        return mpaDto;
    }

}
