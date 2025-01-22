package ru.yandex.practicum.filmorate.dal.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class FilmRowMapper implements RowMapper<Film> {


    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("FILM_ID"));
        film.setName(resultSet.getString("FILM_NAME"));
        film.setDescription(resultSet.getString("DESCRIPTION"));
        film.setReleaseDate(resultSet.getTimestamp("RELEASE_DATE").toLocalDateTime().toLocalDate());
        film.setDuration(resultSet.getInt("DURATION"));

        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("MPA_ID"));
        if (hasColumn(resultSet, "MPA_NAME")) {
            mpa.setName(resultSet.getString("MPA_NAME"));
        }
        film.setMpa(mpa);

        Set<Genre> genres = new HashSet<>();
        if (hasColumn(resultSet, "GENRE_ID")) {
            do {
                int genreId = resultSet.getInt("GENRE_ID");
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(resultSet.getString("GENRE_NAME"));
                if (!resultSet.wasNull())
                    genres.add(genre);
            } while (resultSet.next() && resultSet.getInt("FILM_ID") == film.getId());
            film.setGenres(new HashSet<>(genres));
        } else film.setGenres(null);


        return film;
    }

    private boolean hasColumn(ResultSet resultSet, String columnName) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            if (metaData.getColumnName(i).equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
    }
}
