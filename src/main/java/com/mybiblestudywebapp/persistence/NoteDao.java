package com.mybiblestudywebapp.persistence;

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

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public NoteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
    }

    @Override
    public boolean save(Note note) {
        String sql = "INSERT INTO notes (note, user_id, book_id, chapter_id, verse, priv, lang) " +
                "VALUES (:note, :userId, :bookId, :chapterId, :verse, :priv, :lang)";
        int rows = 0;

        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource namedParams = new MapSqlParameterSource()
                .addValue("note", note.getNote())
                .addValue("userId", note.getUserId())
                .addValue("bookId", note.getBookId())
                .addValue("chapterId", note.getChapterId())
                .addValue("verse", note.getVerse())
                .addValue("priv", note.isPriv())
                .addValue("lang", note.getLang());

        rows = namedParameterJdbcTemplate.update(sql, namedParams, holder);

        return rows > 0;
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
        return false;
    }

    @Override
    public Optional<Note> get(long id) {
        String sql = "SELECT * FROM notes WHERE note_id = ?";
        var result = jdbcTemplate.queryForObject(sql, new Object[]{id}, NoteDao::mapRow);
        return Optional.ofNullable(result);
    }

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
