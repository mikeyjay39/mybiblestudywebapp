package com.mybiblestudywebapp.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.rowset.JdbcRowSet;
import java.sql.Types;
import java.util.List;
import java.util.Map;
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

        return rows > 0 ? true : false;
    }

    @Override
    public boolean update(Note note, Map<String, Object> params) {
        return false;
    }

    @Override
    public boolean delete(Note note) {
        return false;
    }

    @Override
    public Optional<Note> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Note> getUnique(String uniqueKey) {
        return Optional.empty();
    }

    @Override
    public List<Note> getAll() {
        return null;
    }
}
