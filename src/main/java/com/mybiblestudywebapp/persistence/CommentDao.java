package com.mybiblestudywebapp.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/18/19
 */
public class CommentDao implements UpdatableDao<Comment> {

    private static final Logger logger = LoggerFactory.getLogger(CommentDao.class);
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CommentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    /**
     * Save Comment to DB
     * @param comment
     * @return comment_id or -1 if failed
     */
    @Override
    public long save(Comment comment) {
        String sql = "INSERT INTO comments (user_id, note_id) " +
                "VALUES (:userId, :noteId) " +
                "RETURNING comment_id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", comment.getUserId())
                .addValue("noteId", comment.getNoteId());

        long commentId = -1;
        try {
            commentId = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        } catch (DataAccessException e) {
            logger.info("Could not add comment for user_id: " + comment.getUserId() +
                    " note_id: " + comment.getNoteId() +
                    "\n" + e.getMessage());
        }
        return commentId;
    }

    @Override
    public boolean update(Comment comment) {
        return false;
    }

    @Override
    public boolean delete(Comment comment) {
        return false;
    }

    @Override
    public Optional<Comment> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Comment> getUnique(String uniqueKey) {
        return Optional.empty();
    }

    @Override
    public List<Comment> getAll() {
        return null;
    }
}
