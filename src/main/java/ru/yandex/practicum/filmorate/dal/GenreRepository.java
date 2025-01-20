package ru.yandex.practicum.filmorate.dal;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class GenreRepository extends BaseRepository<Genre> {

    MapSqlParameterSource parameters = new MapSqlParameterSource();

    public static final String FIND_ALL = "SELECT * FROM GENRES";
    public static final String FIND_BY_ID = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
    public static final String FIND_BY_IDS = "SELECT * FROM GENRES WHERE GENRE_ID IN ";



    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Genre findById(Integer id) {
        return findOne(FIND_BY_ID, id).orElseThrow(() -> new NotFoundException("Genre с таким id не айден: " + id));
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL);
    }

    public Set<Integer> genresNotExist(Set<Genre> genres) {

        Set<Integer> genreIds = genres.stream()
               .map(Genre::getId)
               .collect(Collectors.toSet());

        String inClause = genreIds.stream()
               .map(id -> "?")
               .collect(Collectors.joining(", "));

        String query = FIND_BY_IDS + "(" + inClause + ")";

        List<Integer> findIds = findMany(query, genreIds.toArray()).stream()
                .map(Genre::getId)
                .toList();

        return genreIds.stream()
                .filter(id -> !findIds.contains(id))
                .collect(Collectors.toSet());
    }
}
