package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {


    private static final String FIND_ALL_QUERY = "SELECT * FROM FILMS " +
            "JOIN MPA ON MPA.MPA_ID = FILMS.MPA_ID " + " LEFT JOIN FILMS_GENRES ON FILMS_GENRES.FILM_ID = FILMS.FILM_ID " +
            " LEFT JOIN GENRES ON GENRES.GENRE_ID = FILMS_GENRES.GENRE_ID ";
    private static final String WHERE_ID_QUERY = " WHERE FILMS.FILM_ID = :id ";
    private static final String INSERT_QUERY = "INSERT INTO FILMS(MPA_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION)" +
            "VALUES (:mpaId, :filmName, :description, :releaseDate, :duration)";
    public static final String INSERT_GENRES_QUERY = "INSERT INTO FILMS_GENRES(FILM_ID, GENRE_ID) VALUES (:filmId, :genreId)";
    private static final String UPDATE_QUERY = "UPDATE FILMS SET MPA_ID = :mpaId, FILM_NAME = :filmId, DESCRIPTION = :description, RELEASE_DATE = :releaseDate, DURATION = :duration   WHERE FILM_ID = :filmId";
    private static final String INSET_LIKES = "INSERT INTO LIKES(FILM_ID, USER_ID) VALUES(:filmId, :userId)";
    public static final String DELETE_LIKES = "DELETE FROM LIKES WHERE FILM_ID = :filmId AND USER_ID = :userId";
    private static final String FIND_POPULAR_FILM_QUERY  = " SELECT F.*, COUNT(L.LIKE_ID) AS LIKE_COUNT FROM FILMS F LEFT JOIN LIKES L ON F.FILM_ID = L.FILM_ID GROUP BY F.FILM_ID ORDER BY LIKE_COUNT DESC  LIMIT :count";

    public FilmRepository(NamedParameterJdbcOperations jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        return findMany(FIND_ALL_QUERY, mapSqlParameterSource);
    }

    public Optional<Film> findById(int filmId) {

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", filmId);
        return findOne(FIND_ALL_QUERY + WHERE_ID_QUERY, mapSqlParameterSource);

    }

    public Film save(Film film) {


        MapSqlParameterSource mapInsert = new MapSqlParameterSource();
        mapInsert.addValue("mpaId", film.getMpa().getId());
        mapInsert.addValue("filmName", film.getName());
        mapInsert.addValue("description", film.getDescription());
        mapInsert.addValue("releaseDate", Timestamp.valueOf(film.getReleaseDate().atStartOfDay()));
        mapInsert.addValue("duration", film.getDuration());

        Integer id = insert(INSERT_QUERY, mapInsert, "FILM_ID");
        film.setId(id);

        String checkQuery = "SELECT COUNT(*) FROM FILMS_GENRES WHERE FILM_ID = :filmId AND GENRE_ID = :genreId";

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                MapSqlParameterSource mapUpdate = new MapSqlParameterSource();
                mapUpdate.addValue("filmId", film.getId());
                mapUpdate.addValue("genreId", genre.getId());

                int count = jdbc.queryForObject(checkQuery, mapUpdate, Integer.class);
                if (count == 0) {
                    jdbc.update(INSERT_GENRES_QUERY, mapUpdate);
                }
            }
        }
        return film;
    }

    public Film update(Film film) {

        MapSqlParameterSource mapUpdate = new MapSqlParameterSource();
        mapUpdate.addValue("mpaId", film.getMpa().getId());
        mapUpdate.addValue("filmName", film.getName());
        mapUpdate.addValue("description", film.getDescription());
        mapUpdate.addValue("releaseDate", Timestamp.valueOf(film.getReleaseDate().atStartOfDay()));
        mapUpdate.addValue("duration", film.getDuration());
        mapUpdate.addValue("filmId", film.getId());

        update(UPDATE_QUERY, mapUpdate);
        return film;
    }

    public Film putLike(Integer filmId, Integer userId) {

        MapSqlParameterSource mapPutLike= new MapSqlParameterSource();
        mapPutLike.addValue("filmId", filmId);
        mapPutLike.addValue("userId", userId);

        insert(INSET_LIKES, mapPutLike, "LIKE_ID");
      return findById(filmId).orElseThrow(()-> new NotFoundException( "Film not found" ));
    }

    public Film removeLike(Integer filmId, Integer userId) {

        MapSqlParameterSource deletePutLike= new MapSqlParameterSource();
        deletePutLike.addValue("filmId", filmId);
        deletePutLike.addValue("userId", userId);

        update(DELETE_LIKES, deletePutLike);
        return findById(filmId).orElseThrow(()-> new NotFoundException( "Film not found" ));
    }

    public List<Film> getPopularFilms(Integer count) {

        MapSqlParameterSource mapCount = new MapSqlParameterSource();
        mapCount.addValue("count", count);
        return findMany(FIND_POPULAR_FILM_QUERY, mapCount);
    }



}
