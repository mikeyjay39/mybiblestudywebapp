package com.mybiblestudywebapp.persistenceservice.persistence;

import com.mybiblestudywebapp.utils.persistence.model.Book;
import com.mybiblestudywebapp.utils.persistence.model.Testament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mybiblestudywebapp.main.Constants.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/15/19
 */
@Component
public class BookDao implements Dao<Book> {

    private static final Logger logger = LoggerFactory.getLogger(BookDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public BookDao(JdbcTemplate jdbcTemplate,
                   NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<Book> get(long id) {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        var queryResult = jdbcTemplate.queryForList(sql, id);
        return Optional.ofNullable(buildBook(queryResult.get(0)));
    }

    /**
     * Get book based on its title
     *
     * @param args title of the book
     * @return
     */
    @Override
    public Optional<List<Book>> get(Map<String, Object> args) {
        String sql = "SELECT * FROM books WHERE title = :title";
        SqlParameterSource params = new MapSqlParameterSource(args);
        List<Book> result = null;
        try {
            result = namedParameterJdbcTemplate.query(sql, params, BookDao::mapRow);
        } catch (DataAccessException e) {
            String errMsg = "Could not get book with title: " + args.get(TITLE + "\n" +
                    e.getMessage());
            logger.error(errMsg);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public List<Book> getAll() {
        List<Book> results = new ArrayList<>();
        String sql = "SELECT * FROM books";
        var result = jdbcTemplate.queryForList(sql);
        for (var row : result) {
            results.add(buildBook(row));
        }
        return results;
    }

    /**
     * Builds Book object from returned SQL row
     *
     * @param row
     * @return
     */
    private Book buildBook(Map<String, Object> row) {

        if (row == null) {
            return null;
        }

        Book book = new Book();
        book.setBookId((long) row.get("book_id"));
        book.setTestament((Testament.valueOf((String) row.get("testament"))));
        book.setTitle((String) row.get(TITLE));
        return book;
    }

    static Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString(TITLE));
        book.setTestament(Testament.valueOf(rs.getString("testament")));
        return book;
    }
}
