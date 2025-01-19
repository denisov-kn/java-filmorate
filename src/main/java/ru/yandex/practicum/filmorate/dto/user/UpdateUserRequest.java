package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateUserRequest {

    @NotNull (message = "ИД не может быть пустым")
    @Positive (message = "ИД должен быть больше 0")
    private Integer id;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email не соответствует формату")
    private String email;

    @NotBlank (message = "Логин не может быть пустым")
    private String login;

    private String name;

    @NotNull(message = "День рождения не может отсутствовать")
    @Past(message = "День рождения не может быть в будущем")
    private LocalDate birthday;

    @AssertTrue(message = "Логин не может содержать пробелы")
    public boolean isValidateLogin() {
        return !login.matches(".*\\s.*");
    }


    public boolean hasName() {
        return name != null && !name.isEmpty();
    }
}
