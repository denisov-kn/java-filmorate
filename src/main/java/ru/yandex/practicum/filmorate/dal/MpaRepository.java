package ru.yandex.practicum.filmorate.dal;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {

    public static final String FIND_ALL = "SELECT * FROM MPA ORDER BY MPA_ID ASC";
    public static final String FIND_BY_ID = "SELECT * FROM MPA WHERE MPA_ID = :mpaId";

    public MpaRepository(NamedParameterJdbcOperations jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> findAll() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        return findMany(FIND_ALL, params);
    }

    public Mpa findById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mpaId", id);
        return findOne(FIND_BY_ID, params).orElseThrow(() -> new NotFoundException("MPA с таким id нет: " + id));
    }


}



