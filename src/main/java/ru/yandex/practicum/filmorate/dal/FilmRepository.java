package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
            "JOIN MPA ON MPA.MPA_ID = FILMS.MPA_ID ";
    private static final String WHERE_ID_QUERY = " WHERE FILMS.FILM_ID = ? ";
    private static final String FIND_GENRES =" LEFT JOIN FILMS_GENRES ON FILMS_GENRES.FILM_ID = FILMS.FILM_ID " +
            " LEFT JOIN GENRES ON GENRES.GENRE_ID = FILMS_GENRES.GENRE_ID ";
    private static final String INSERT_QUERY = "INSERT INTO FILMS(MPA_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION)" +
            "VALUES (?, ?, ?, ?, ?)";
    public static final String INSERT_GENRES_QUERY = "INSERT INTO FILMS_GENRES(FILM_ID, GENRE_ID) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE FILMS SET MPA_ID = ?, FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?   WHERE FILM_ID = ?";
    private static final String INSET_LIKES = "INSERT INTO LIKES(FILM_ID, USER_ID) VALUES(?, ?)";
    public static final String DELETE_LIKES = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
    /* private static final String FIND_POPULAR_FILM_QUERY = "SELECT F.*, COUNT(L.LIKE_ID) AS LIKE_COUNT, G.GENRE_ID, G.GENRE_NAME, M.MPA_ID, M.MPA_NAME " +
                    "FROM FILMS F " +
                    "JOIN MPA M ON M.MPA_ID = F.MPA_ID " +
                    "JOIN FILMS_GENRES FG ON FG.FILM_ID = F.FILM_ID " +
                    "JOIN GENRES G ON G.GENRE_ID = FG.GENRE_ID " +
                    "LEFT JOIN LIKES L ON F.FILM_ID = L.FILM_ID " +
                    "GROUP BY F.FILM_ID, G.GENRE_ID, G.GENRE_NAME, M.MPA_ID, M.MPA_NAME " +
                    "ORDER BY LIKE_COUNT DESC " +
                    "LIMIT ?";
 */
    private static final String FIND_POPULAR_FILM_QUERY  = " SELECT F.*, COUNT(L.LIKE_ID) AS LIKE_COUNT FROM FILMS F LEFT JOIN LIKES L ON F.FILM_ID = L.FILM_ID GROUP BY F.FILM_ID ORDER BY LIKE_COUNT DESC  LIMIT ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY + FIND_GENRES);
    }

    public Optional<Film> findById(int filmId) {



      /*  Film film = findOne(FIND_ALL_QUERY  + WHERE_ID_QUERY, filmId)
                .orElseThrow(()-> new NotFoundException( "Film not found" ));

        if(film.hasGenres())
            return findOne(FIND_ALL_QUERY + FIND_GENRES + WHERE_ID_QUERY, filmId);
        else
            return Optional.of(film);
            */


        return findOne(FIND_ALL_QUERY + FIND_GENRES + WHERE_ID_QUERY, filmId);

    }

    public Film save(Film film) {




        Integer id = insert(
                INSERT_QUERY,
                film.getMpa().getId(),
                film.getName(),
                film.getDescription(),
                Timestamp.valueOf(film.getReleaseDate().atStartOfDay()),
                film.getDuration()
        );
        film.setId(id);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbc.update(INSERT_GENRES_QUERY, film.getId(), genre.getId());
            }
        }
        return film;
    }

    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getMpa().getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );
        return film;
    }

    public Film putLike(Integer filmId, Integer userId) {
      insert(
              INSET_LIKES,
              filmId,
              userId
      );
      return findById(filmId).orElseThrow(()-> new NotFoundException( "Film not found" ));
    }

    public Film removeLike(Integer filmId, Integer userId) {
        update(
                DELETE_LIKES,
                filmId,
                userId
        );
        return findById(filmId).orElseThrow(()-> new NotFoundException( "Film not found" ));
    }

    public List<Film> getPopularFilms(Integer count) {
        return findMany(FIND_POPULAR_FILM_QUERY, count);
    }



}
