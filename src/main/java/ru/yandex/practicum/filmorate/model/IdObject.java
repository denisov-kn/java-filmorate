package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IdObject {
    private Integer id;

    @JsonCreator
    public IdObject(@JsonProperty("id") Integer id) {
        this.id = id;
    }
}
