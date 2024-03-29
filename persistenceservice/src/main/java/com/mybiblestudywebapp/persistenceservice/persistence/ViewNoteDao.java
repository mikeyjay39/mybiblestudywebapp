package com.mybiblestudywebapp.persistenceservice.persistence;

import com.mybiblestudywebapp.utils.persistence.model.ViewNote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mybiblestudywebapp.utils.main.Constants.NOTE_ID;
import static com.mybiblestudywebapp.utils.main.Constants.VIEW_ID;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/19/19
 */
@Component
public class ViewNoteDao implements UpdatableDao<ViewNote> {

    private static final Logger logger = LoggerFactory.getLogger(ViewNoteDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ViewNoteDao(JdbcTemplate jdbcTemplate,
                       NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<ViewNote> get(long id) {
        return Optional.empty();
    }

    /**
     * @param args keys: viewId chapterId
     * @return
     */
    @Override
    public Optional<List<ViewNote>> get(Map<String, Object> args) {
        String sql = "SELECT * FROM view_note " +
                "JOIN notes ON view_note.note_id = notes.note_id WHERE " +
                "view_id = :viewId AND chapter_id = :chapterId";
        SqlParameterSource params = new MapSqlParameterSource(args);
        List<ViewNote> result = null;
        try {
            result = namedParameterJdbcTemplate.query(sql, params, ViewNoteDao::mapRow);
        } catch (DataAccessException e) {
            String errMsg = "Could not get view_note for view_id: " + args.get("viewId") +
                    " note_id: " + args.get("noteId") + "\n" + e.getMessage();
            logger.info(errMsg);
        }
        return Optional.ofNullable(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long save(ViewNote viewNote) {
        String sql = "INSERT INTO view_note (view_id, note_id) " +
                "VALUES (:viewId, :noteId)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(VIEW_ID, viewNote.getViewId())
                .addValue(NOTE_ID, viewNote.getNoteId());

        try {
            return namedParameterJdbcTemplate.update(sql, params);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean update(ViewNote viewNote) {
        return false;
    }

    @Override
    public boolean delete(ViewNote viewNote) {
        String sql = "DELETE FROM view_note WHERE view_id = :viewId AND " +
                "note_id = :noteId";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("viewId", viewNote.getViewId())
                .addValue("noteId", viewNote.getNoteId());
        int rows = namedParameterJdbcTemplate.update(sql, params);
        return rows > 0;
    }

    @Override
    public List<ViewNote> getAll() {
        String sql = "SELECT * FROM view_note";
        return jdbcTemplate.query(sql, ViewNoteDao::mapRow);
    }

    static ViewNote mapRow(ResultSet rs, int rowNum) throws SQLException {
        ViewNote viewNote = new ViewNote();
        viewNote.setViewNoteId(rs.getInt("view_note_id"));
        viewNote.setViewId(rs.getInt("view_id"));
        viewNote.setNoteId(rs.getInt("note_id"));
        return viewNote;
    }
}
