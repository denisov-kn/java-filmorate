package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @NotNull (message = "ИД не может быть пустым", groups = Marker.Update.class)
    @Positive (message = "ИД должен быть больше 0", groups = Marker.Update.class)
    private Integer id;

    @NotBlank (message = "Email не может быть пустым", groups = {Marker.Update.class, Marker.Create.class})
    @Email (message = "Email не соответствует формату", groups = {Marker.Update.class, Marker.Create.class})
    private String email;

    @NotBlank (message = "Логин не может быть пустым", groups = {Marker.Update.class, Marker.Create.class})
    private String login;

    private String name;

    private Set<Integer> friends = new HashSet<>();

    private Set<Integer> filmsLikes = new HashSet<>();

    @NotNull (message = "День рождения не может отсутствовать", groups = {Marker.Create.class, Marker.Update.class})
    @Past (message = "День рождения не может быть в будущем", groups = {Marker.Update.class, Marker.Create.class})
    private LocalDate birthday;

    @AssertTrue(message = "Логин не может содержать пробелы")
    public boolean isValidateLogin() {
        return !login.matches(".*\\s.*");
    }
}
