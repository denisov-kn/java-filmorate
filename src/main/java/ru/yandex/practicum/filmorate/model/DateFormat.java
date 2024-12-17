package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import java.time.format.DateTimeFormatter;

@Getter
public enum DateFormat {
    DATE_FORMAT("yyyy-MM-dd");

    private final DateTimeFormatter formatter;

    DateFormat(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);

    }

}

