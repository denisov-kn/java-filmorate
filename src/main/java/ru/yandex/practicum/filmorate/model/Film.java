package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {

    private static final LocalDate MIN_FILM_DATE = LocalDate.parse("1895-12-28", DateFormat.DATE_FORMAT.getFormatter());

    @NotNull (message = "ИД не может быть пустым", groups = Marker.Update.class)
    @Positive (message = "ИД должен быть больше 0", groups = Marker.Update.class)
    private Integer id;

    @NotBlank (message = "Имя фильма не может быть пустым", groups = {Marker.Create.class, Marker.Update.class})
    private String name;

    @Size (message = "Описание фильма не может быть больше 200 символов",
            max = 200,
            groups = {Marker.Create.class, Marker.Update.class
            })
    private String description;

    @NotNull (message = "Дата релиза не может отсутствовать", groups = {Marker.Create.class, Marker.Update.class})
    @Past (message = "Дата релиза не может быть в будущем", groups = {Marker.Create.class, Marker.Update.class})
    private LocalDate releaseDate;

    @NotNull (message = "Длительность фильма не может отсутствовать", groups = {Marker.Create.class, Marker.Update.class})
    @Positive  (message = "Длительность фильма должно быть больше 0", groups = {Marker.Create.class, Marker.Update.class})
    private Integer duration;

    private Set<Integer> likes = new HashSet<>();

    @AssertTrue (message = "Дата фильма не может быть раньше чем 28.12.1895")
    public boolean isValidateReleaseDate() {
        if (releaseDate != null) return releaseDate.isAfter(MIN_FILM_DATE);
        return true;
    }
}
