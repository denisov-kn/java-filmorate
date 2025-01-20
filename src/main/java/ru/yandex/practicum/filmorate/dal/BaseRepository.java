package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<T> {

    protected final NamedParameterJdbcOperations jdbc;
    protected final RowMapper<T> mapper;

    protected Optional<T> findOne(String query, MapSqlParameterSource parameters) {
        try {
            T result = jdbc.queryForObject(query,parameters, mapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, MapSqlParameterSource parameters) {
        return jdbc.query(query, parameters, mapper);
    }

    protected void update(String query, MapSqlParameterSource parameters) {
        int rowsUpdated = jdbc.update(query, parameters);
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    protected Integer insert(String query, MapSqlParameterSource parameters, String keyID) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(query, parameters, keyHolder, new String[] {keyID});

        Number generatedKey = keyHolder.getKey();
        return generatedKey != null ? generatedKey.intValue() : null;
    }

}
