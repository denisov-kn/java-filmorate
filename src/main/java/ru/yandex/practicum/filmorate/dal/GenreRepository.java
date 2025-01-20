package ru.yandex.practicum.filmorate.dal;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class GenreRepository extends BaseRepository<Genre> {


    public static final String FIND_ALL = "SELECT * FROM GENRES";
    public static final String FIND_BY_ID = "SELECT * FROM GENRES WHERE GENRE_ID = :genreId";
    public static final String FIND_BY_IDS = "SELECT * FROM GENRES WHERE GENRE_ID IN (:ids)";


    public GenreRepository(NamedParameterJdbcOperations jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Genre findById(Integer id) {
        MapSqlParameterSource mapFind = new MapSqlParameterSource();
        mapFind.addValue("genreId", id);
        return findOne(FIND_BY_ID, mapFind).orElseThrow(() -> new NotFoundException("Genre с таким id не айден: " + id));
    }

    public List<Genre> findAll() {
        MapSqlParameterSource mapFindAll = new MapSqlParameterSource();
        return findMany(FIND_ALL, mapFindAll);
    }

    public Set<Integer> genresNotExist(Set<Genre> genres) {

        Set<Integer> genreIds = genres.stream()
               .map(Genre::getId)
               .collect(Collectors.toSet());


        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("ids", genreIds);

        List<Integer> findIds = findMany(FIND_BY_IDS, map).stream()
                .map(Genre::getId)
                .toList();

        return genreIds.stream()
                .filter(id -> !findIds.contains(id))
                .collect(Collectors.toSet());
    }
}
