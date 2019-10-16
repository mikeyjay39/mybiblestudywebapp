package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.Book;
import com.mybiblestudywebapp.persistence.BookDao;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/15/19
 */
public class BookUnitTests {

    private BookDao bookDao;
    private Book book;

    public BookUnitTests() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(DbConnectionTest.getEmbeddedPostgres());
        bookDao = new BookDao(jdbcTemplate);
    }

    @Test
    public void testGetAll() throws Exception {
        var books = bookDao.getAll();
        int size = books.size();
        Assert.assertEquals(66, size);
    }
}
