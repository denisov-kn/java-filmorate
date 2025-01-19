package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdObject;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {


    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("FILM_ID"));
        film.setName(resultSet.getString("FILM_NAME"));
        film.setDescription(resultSet.getString("DESCRIPTION"));
        film.setReleaseDate(resultSet.getTimestamp("RELEASE_DATE").toLocalDateTime().toLocalDate());
        film.setMpa(new IdObject(resultSet.getInt("RATING_ID")));
        return film;
    }
}
