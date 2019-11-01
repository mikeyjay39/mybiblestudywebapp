package com.mybiblestudywebapp.persistence;

import com.mybiblestudywebapp.persistence.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.mybiblestudywebapp.main.Constants.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/17/19
 */

/**
 * Used to determine which case to get used in get() with Map argument
 */
enum GetNotesCase {
    GET_CHAPTER_NOTES_FROM_VIEW, GET_ALL_NOTES_ABOVE_RANK
}

@Component
public class NoteDao implements UpdatableDao<Note> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public NoteDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Save Note to DB
     * @param note
     * @return note_id or -1 on failure
     */
    @Override
    @Transactional
    public long save(Note note) {
        String sql = "INSERT INTO notes (note, user_id, book_id, chapter_id, verse, priv, lang) " +
                "VALUES (:note, :userId, :bookId, :chapterId, :verse, :priv, :lang) " +
                "RETURNING note_id";
        Long noteId = null;

        SqlParameterSource namedParams = new MapSqlParameterSource()
                .addValue("note", note.getNoteText())
                .addValue("userId", note.getUserId())
                .addValue("bookId", note.getBookId())
                .addValue(CHAPTER_ID, note.getChapterId())
                .addValue(VERSE, note.getVerse())
                .addValue("priv", note.isPriv())
                .addValue("lang", note.getLang());

        try {
            noteId = namedParameterJdbcTemplate.queryForObject(sql, namedParams, Long.class);
        } catch (DataAccessException e) {
            String errMsg = "Could note add note for user_id: " + note.getUserId() +
                    " book_id: " + note.getBookId() + " chapter_id: " + note.getChapterId() +
                    " verse: " + note.getVerse() + "\n" + e.getMessage();
            LOGGER.error(errMsg);
        }

        return noteId == null ? -1 : noteId;
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
                .addValue("note", note.getNoteText())
                .addValue("bookId", note.getBookId())
                .addValue(CHAPTER_ID, note.getChapterId())
                .addValue(VERSE, note.getVerse())
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
            String errMsg = "No result for note_id = " + id + "\n" + e.getMessage();
            LOGGER.error(errMsg);
        }
        return Optional.ofNullable(result);
    }

    /**
     * Get all notes in a chapter based on a key
     * @param args keys can be: viewId + chapterId or ranking
     * @return
     */
    @Override
    public Optional<List<Note>> get(final Map<String, Object> args) {
        GetNotesCase getNotesCase = getNotesCase(args);

        if (getNotesCase == null) {
            return Optional.empty();
        }

        switch (getNotesCase) {
            case GET_CHAPTER_NOTES_FROM_VIEW:
                return getChapterNotesFromView(args);
            case GET_ALL_NOTES_ABOVE_RANK:
                return getAllNotesAboveRank(args);
            default:
                break;
        }

        return Optional.empty();
    }

    @Override
    public List<Note> getAll() {
        return new ArrayList<>();
    }

    static Note mapRow(ResultSet rs, int rowNum) throws SQLException {
        Note note = new Note();

        note.setNoteId(rs.getInt("note_id"));
        note.setUserId(rs.getInt("user_id"));
        note.setBookId(rs.getInt("book_id"));
        note.setChapterId(rs.getInt("chapter_id"));
        note.setVerse(rs.getInt(VERSE));
        note.setRanking(rs.getInt("ranking"));
        note.setPriv(rs.getBoolean("priv"));
        note.setNoteText(rs.getString("note"));
        note.setLang(rs.getString("lang"));
        note.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        var lastModified = rs.getTimestamp("last_modified");
        if (null != lastModified) {
            note.setLastModified(lastModified.toLocalDateTime());
        }

        return note;
    }

    /**
     * Use this to determine from args which case of get() we want to call, such
     * as getting all notes from a specific view or getting all notes above a specific rank
     * value.
     * @param args
     * @return
     */
    private GetNotesCase getNotesCase(final Map<String, Object> args) {
        GetNotesCase result = null;
        Set<String> keys = args.keySet();

        // Build get notes from view key check
        List<String> notesViewKeys = new ArrayList<>();
        notesViewKeys.add(VIEW_ID);
        notesViewKeys.add(CHAPTER_ID);

        // Build get notes above ranking check
        List<String> notesRankingKeys = new ArrayList<>();
        notesRankingKeys.add(RANKING);

        if (keys.containsAll(notesViewKeys)) {
            result = GetNotesCase.GET_CHAPTER_NOTES_FROM_VIEW;
        } else if (keys.containsAll(notesRankingKeys)) {
            result = GetNotesCase.GET_ALL_NOTES_ABOVE_RANK;
        }

        return result;
    }

    /**
     * Get all the notes in a chapter based on a view ID
     * @param args
     * @return
     */
    private Optional<List<Note>> getChapterNotesFromView(final Map<String, Object> args) {

        // Get chapter notes from view
        List<Note> result = null;
        String sql = "SELECT * FROM notes " +
                "JOIN view_note ON view_note.note_id = notes.note_id " +
                "WHERE view_note.view_id = view_id " +
                "AND notes.chapter_id = :chapterId";
        SqlParameterSource params = new MapSqlParameterSource(args);

        try {
            result = namedParameterJdbcTemplate.query(sql, params, NoteDao::mapRow);
        } catch (DataAccessException e) {
            String errMsg = "Could note retrieve notes for view_id: " + args.get("viewId") + " chapter_id: " +
                    args.get(CHAPTER_ID) + "\n" + e.getMessage();
            LOGGER.error(errMsg);
        }

        return Optional.ofNullable(result);
    }

    /**
     * Get all notes in a chapter above a rank.
     * Args needs to contain a key 'ranking'.
     * @param args
     * @return
     */
    private Optional<List<Note>> getAllNotesAboveRank(final Map<String, Object> args) {
        List<Note> result = null;
        String sql = "SELECT * FROM notes WHERE ranking > :ranking AND priv = false";
        SqlParameterSource params = new MapSqlParameterSource(args);

        try {
            result = namedParameterJdbcTemplate.query(sql, params, NoteDao::mapRow);
        } catch (DataAccessException e) {
            String errMsg = "Could note retrieve notes above ranking: " + args.get(RANKING) + "\n" + e.getMessage();
            LOGGER.error(errMsg);
        }

        return Optional.ofNullable(result);
    }
}