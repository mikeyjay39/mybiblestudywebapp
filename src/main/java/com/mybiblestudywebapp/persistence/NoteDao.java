package com.mybiblestudywebapp.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/17/19
 */
public class NoteDao implements UpdatableDao<Note> {

    private static final Logger logger = LoggerFactory.getLogger(NoteDao.class);
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public NoteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
    }

    /**
     * Save Note to DB
     * @param note
     * @return note_id or -1 on failure
     */
    @Override
    public long save(Note note) {
        String sql = "INSERT INTO notes (note, user_id, book_id, chapter_id, verse, priv, lang) " +
                "VALUES (:note, :userId, :bookId, :chapterId, :verse, :priv, :lang) " +
                "RETURNING note_id";
        long noteId = -1;

        SqlParameterSource namedParams = new MapSqlParameterSource()
                .addValue("note", note.getNote())
                .addValue("userId", note.getUserId())
                .addValue("bookId", note.getBookId())
                .addValue("chapterId", note.getChapterId())
                .addValue("verse", note.getVerse())
                .addValue("priv", note.isPriv())
                .addValue("lang", note.getLang());

        try {
            noteId = namedParameterJdbcTemplate.queryForObject(sql, namedParams, Long.class);
        } catch (DataAccessException e) {
            String errMsg = "Could note add note for user_id: " + note.getUserId() +
                    " book_id: " + note.getBookId() + " chapter_id: " + note.getChapterId() +
                    " verse: " + note.getVerse() + "\n" + e.getMessage();
            logger.info(errMsg);
        }

        return noteId;
    }

    @Override
    public boolean update(Note note) {
        note.setLastModified(LocalDateTime.now());
        String sql = "UPDATE notes SET note = :note, book_id = :bookId, chapter_id = :chapterId," +
                "verse = :verse, priv = :priv, lang = :lang, last_modified = :lastModified " +
                "WHERE note_id = :noteId";

        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource namedParams = new MapSqlParameterSource()
                .addValue("noteId", note.getNoteId())
                .addValue("note", note.getNote())
                .addValue("bookId", note.getBookId())
                .addValue("chapterId", note.getChapterId())
                .addValue("verse", note.getVerse())
                .addValue("priv", note.isPriv())
                .addValue("lang", note.getLang())
                .addValue("lastModified", Timestamp.valueOf(note.getLastModified()));

        int rows = 0;
        rows = namedParameterJdbcTemplate.update(sql, namedParams, holder);

        return rows > 0;
    }

    @Override
    public boolean delete(Note note) {
        String sql = "DELETE FROM notes WHERE note_id = :noteId";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("noteId", note.getNoteId());
        int rows = 0;
        rows = namedParameterJdbcTemplate.update(sql, params, holder);
        return rows > 0;
    }

    @Override
    public Optional<Note> get(long id) {
        String sql = "SELECT * FROM notes WHERE note_id = ?";
        Note result = null;
        try {
            result = jdbcTemplate.queryForObject(sql, new Object[]{id}, NoteDao::mapRow);
        } catch(EmptyResultDataAccessException e) {
            logger.info("No result for note_id = " + id + "\n" + e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    /**
     * Deprecated
     * @param uniqueKey
     * @return
     */
    @Override
    public Optional<Note> getUnique(String uniqueKey) {
        return Optional.empty();
    }

    @Override
    public List<Note> getAll() {
        return null;
    }

    private static Note mapRow(ResultSet rs, int rowNum) throws SQLException {
        Note note = new Note();

        note.setNoteId(rs.getInt("note_id"));
        note.setNote(rs.getString("note"));
        note.setUserId(rs.getInt("user_id"));
        note.setBookId(rs.getInt("book_id"));
        note.setChapterId(rs.getInt("chapter_id"));
        note.setVerse(rs.getInt("verse"));
        note.setRanking(rs.getInt("ranking"));
        note.setPriv(rs.getBoolean("priv"));
        note.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        var lastModified = rs.getTimestamp("last_modified");
        if (null != lastModified) {
            note.setLastModified(lastModified.toLocalDateTime());
        }

        return note;
    }
}
