package com.mybiblestudywebapp.persistence;

import com.mybiblestudywebapp.persistence.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mybiblestudywebapp.main.Constants.COMMENT_ID;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/18/19
 */
@Component
public class CommentDao implements UpdatableDao<Comment> {

    private final Logger logger = LoggerFactory.getLogger(CommentDao.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CommentDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

    /**
     * Update comment
     * @param comment
     * @return true on success
     */
    @Override
    public boolean update(Comment comment) {
        String sql = "UPDATE comments SET comment = :comment " +
                "WHERE comment_id = :commentId";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("comment", comment.getCommentText())
                .addValue(COMMENT_ID, comment.getCommentId());
        int rows = 0;
        rows = namedParameterJdbcTemplate.update(sql, params, holder);
        return rows > 0;
    }

    /**
     * Delete by comment_id
     * @param comment
     * @return
     */
    @Transactional
    @Override
    public boolean delete(Comment comment) {
        String sql = "DELETE FROM comments WHERE comment_id = :commentId";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COMMENT_ID, comment.getCommentId());
        int rows;
        rows = namedParameterJdbcTemplate.update(sql, params);
        return rows > 0;
    }

    /**
     * Get comment by comment_id
     * @param id
     * @return
     */
    @Override
    public Optional<Comment> get(long id) {
        String sql = "SELECT * FROM comments WHERE comment_id = :commentId";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COMMENT_ID, id);
        Comment result = null;

        try {
            result = namedParameterJdbcTemplate.queryForObject(sql, params, CommentDao::mapRow);
        } catch (DataAccessException e) {
            String errMsg = "Could not get comment_id: " + id + "\n" + e.getMessage();
            logger.info(errMsg);
        }

        return Optional.ofNullable(result);
    }

    /**
     * Get all comments for a note
     * @param args contains key noteId or noteId and "userOwnsNote" which prevents foreign users from accessing
     *             comments of private notes.
     * @return
     */
    @Override
    public Optional<List<Comment>> get(Map<String, Object> args) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM comments AS c");

        if (!(args.keySet().contains("userOwnsNote"))) {
            // user is not the owner of this note so filter out private notes
            sqlBuilder.append(" JOIN notes AS n ON n.note_id = c.note_id " +
                    "AND n.priv = false");
        }

        sqlBuilder.append(" WHERE c.note_id = :noteId " +
                "ORDER BY c.created_at");
        String sql = sqlBuilder.toString();

        SqlParameterSource params = new MapSqlParameterSource(args);
        List<Comment> result = null;

        try {
            result = namedParameterJdbcTemplate.query(sql, params, CommentDao::mapRow);
        } catch (DataAccessException e) {
            String errMsg = "Could not get comments for note_id: " + args.get("noteId") + "\n" +
                    e.getMessage();
            logger.info(errMsg);
        }

        return Optional.ofNullable(result);
    }

    @Override
    public List<Comment> getAll() {
        return new ArrayList<>();
    }

    static Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();
        comment.setCommentId(rs.getInt("comment_id"));
        comment.setNoteId(rs.getInt("note_id"));
        comment.setUserId(rs.getInt("user_id"));
        comment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        comment.setCommentText(rs.getString("comment"));
        return comment;
    }
}
