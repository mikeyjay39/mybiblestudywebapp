package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.model.Comment;
import com.mybiblestudywebapp.persistence.CommentDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/18/19
 */
public class CommentDaoTest {

    private Comment comment;
    private CommentDao commentDao;

    @Before
    public void setUp() throws Exception {
        comment = null;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DbConnectionTest.rebuildEmbeddedDataBase());
        commentDao = new CommentDao(new NamedParameterJdbcTemplate(jdbcTemplate));
    }

    @After
    public void tearDown() {
        comment = null;
    }

    @Transactional
    @Test
    @Rollback
    public void save() {
        save1();
    }

    @Transactional
    @Test
    @Rollback
    public void update() {
        comment = getComment();
        String newCommentField = "This is the new comment text";
        Assert.assertNotEquals(newCommentField, comment.getCommentText());
        long id = comment.getCommentId();
        comment.setCommentText(newCommentField);
        boolean result = commentDao.update(comment);
        Assert.assertTrue(result);
        Comment updatedComment = commentDao.get(id).get();
        Assert.assertEquals(newCommentField, updatedComment.getCommentText());
    }

    @Transactional
    @Test
    @Rollback
    public void delete() {
        comment = commentDao.get(1).get();
        boolean result = commentDao.delete(comment);
        Assert.assertTrue(result);
        Optional<Comment> comment1 = commentDao.get(1);
        Assert.assertTrue(comment1.isEmpty());
    }

    @Test
    @Rollback
    public void get() {
        getComment();
    }

    @Test
    @Rollback
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

    private Comment getComment() {
        comment = commentDao.get(1).orElse(null);
        Assert.assertNotNull(comment);
        Assert.assertEquals(2, comment.getUserId());
        return comment;
    }
}
