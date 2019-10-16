package com.mybiblestudywebapp.persistence;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/15/19
 */
public class BookDao implements Dao<Book> {

    private JdbcTemplate jdbcTemplate;

    public BookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Book> get(long id) {
        return null;
    }

    @Override
    public Optional<Book> getUnique(String uniqueKey) {
        return null;
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
     * @param row
     * @return
     */
    private Book buildBook(Map<String, Object> row) {
        Book book = new Book();
        book.setBookId((long)row.get("book_id"));
        book.setTestament((Testament.valueOf((String)row.get("testament"))));
        book.setTitle((String)row.get("title"));
        return book;
    }
}
