package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {

    public static final String FIND_ALL = "SELECT * FROM MPA ORDER BY MPA_ID ASC";
    public static final String FIND_BY_ID = "SELECT * FROM MPA WHERE MPA_ID = ?";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> findAll() {
        return findMany(FIND_ALL);
    }

    public Mpa findById(int id) {
        return findOne(FIND_BY_ID, id).orElseThrow(()-> new NotFoundException("MPA с таким id нет: " + id));
    }


}



