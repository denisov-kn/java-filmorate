package ru.yandex.practicum.filmorate.dto.film;


import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.DateFormat;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.IdObject;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class NewFilmRequest {
    private static final LocalDate MIN_FILM_DATE = LocalDate.parse("1895-12-28", DateFormat.DATE_FORMAT.getFormatter());

    @NotBlank(message = "Имя фильма не может быть пустым")
    private String name;

    @Size(message = "Описание фильма не может быть больше 200 символов", max = 200)
    private String description;

    @NotNull (message = "Дата релиза не может отсутствовать")
    @Past(message = "Дата релиза не может быть в будущем")
    private LocalDate releaseDate;

    @NotNull (message = "Длительность фильма не может отсутствовать")
    @Positive  (message = "Длительность фильма должно быть больше 0")
    private Integer duration;

    private Mpa mpa;

    private List<Genre> genres;

    @AssertTrue (message = "Дата фильма не может быть раньше чем 28.12.1895")
    public boolean isValidateReleaseDate() {
        if (releaseDate != null) return releaseDate.isAfter(MIN_FILM_DATE);
        return true;
    }
}


