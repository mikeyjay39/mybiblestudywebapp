package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.Comment;
import com.mybiblestudywebapp.persistence.CommentDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/18/19
 */
public class CommentDaoTest {

    private Comment comment;
    private CommentDao commentDao;

    public CommentDaoTest() {
        commentDao = new CommentDao(new JdbcTemplate(DbConnectionTest.getEmbeddedPostgres()));
    }

    @Before
    public void setUp() {
        comment = null;
    }

    @After
    public void tearDown() {
        comment = null;
    }

    @Test
    public void save() {
        save1();
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void get() {
    }

    @Test
    public void getAll() {
    }

    private long save1() {
        comment = new Comment();
        comment.setUserId(1);
        comment.setNoteId(1);
        long result = commentDao.save(comment);
        Assert.assertTrue(result > -1);
        return result;
    }
}
