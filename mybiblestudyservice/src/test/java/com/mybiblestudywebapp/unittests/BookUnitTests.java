package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.utils.persistence.model.Book;
import com.mybiblestudywebapp.persistenceservice.persistence.BookDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/15/19
 */
@Transactional
@Rollback
public class BookUnitTests {

    private BookDao bookDao;
    private Book book;

    public BookUnitTests() {
        JdbcTemplate jdbcTemplate = DbConnectionTest.getJdbcTemplate();
        bookDao = new BookDao(jdbcTemplate, new NamedParameterJdbcTemplate(jdbcTemplate));
    }

    @Before
    public void setUp() {
        book = null;
    }

    @After
    public void tearDown() {
        book = null;
    }

    @Test
    public void testGetAll() throws Exception {
        var books = bookDao.getAll();
        int size = books.size();
        Assert.assertEquals(66, size);
    }

    @Test
    public void testGet() throws Exception {
        book = bookDao.get(1l).get();
        Assert.assertEquals(1, book.getBookId());
        Assert.assertEquals("Genesis", book.getTitle());
    }

    @Test
    public void testGetUnique() throws Exception {
        Map<String, Object> args = new HashMap<>();
        args.put("title", "Genesis");
        book = bookDao.get(args).get().get(0);
        Assert.assertEquals(1, book.getBookId());
        Assert.assertEquals("Genesis", book.getTitle());
    }
}
