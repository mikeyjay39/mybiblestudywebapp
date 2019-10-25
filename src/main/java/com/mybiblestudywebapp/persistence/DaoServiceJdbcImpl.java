package com.mybiblestudywebapp.persistence;

import com.mybiblestudywebapp.persistence.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/19/19
 */
@Service
public class DaoServiceJdbcImpl implements DaoService {

    private static final Logger logger = LoggerFactory.getLogger(DaoServiceJdbcImpl.class);

    @Autowired
    private Dao bookDao;

    @Autowired
    private Dao chapterDao;

    @Autowired
    private UpdatableDao commentDao;

    @Autowired
    private UpdatableDao noteDao;

    @Autowired
    private UpdatableDao userDao;

    @Autowired
    private UpdatableDao viewDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DaoServiceJdbcImpl(){}

    /**
     * Used for unit tests. Pass the JdbcTemplate with the embedded postgres datasource as the arg.
     * @param jdbcTemplate
     */
    public DaoServiceJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        bookDao = new BookDao(jdbcTemplate);
        chapterDao = new ChapterDao(jdbcTemplate);
        commentDao = new CommentDao(jdbcTemplate);
        noteDao = new NoteDao(jdbcTemplate);
        userDao = new UserDao(jdbcTemplate);
        viewDao = new ViewDao(jdbcTemplate);
    }


    /**
     * {@inheritDoc}
     *
     * @param userId the user who's notes will be added to the view
     * @param viewId
     * @return
     */
    @Override
    @Transactional
    @Async
    public CompletableFuture<Long> addUserNotesToView(long userId, long viewId) {
        String sql = "SELECT * FROM notes WHERE user_id = :userId AND priv = false";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("viewId", viewId);
        List<Note> results = null;
        try {
            results = namedParameterJdbcTemplate.query(sql, params, NoteDao::mapRow);
            if (results.size() <= 0) {
                return CompletableFuture.completedFuture((long)results.size());
            }
        } catch (DataAccessException e) {
            String errMsg = "Could not find notes for user_id: " + userId + "\n" + e.getMessage();
            logger.info(errMsg);
            return CompletableFuture.completedFuture(0l);
        }
        List<ViewNote> viewNotes = new ArrayList<>();

        for (Note note : results) {
            ViewNote viewNote = new ViewNote();
            viewNote.setViewId(viewId);
            viewNote.setNoteId(note.getNoteId());
            viewNotes.add(viewNote);
        }

        String sqlInsert = "INSERT INTO view_note (view_id, note_id) " +
                "VALUES (:viewId, :noteId)";

        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(viewNotes.toArray());
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sqlInsert, batch);

        int total = IntStream.of(updateCounts).reduce(0, (a, b) -> a + b);

        return CompletableFuture.completedFuture((long)total);
    }

    /**
     * {@inheritDoc}
     * @param viewCode
     * @param book
     * @param chapterNo
     * @return
     */
    @Override
    public CompletableFuture<List<Note>> getStudyNotesForChapter(String viewCode, String book, long chapterNo) {
        Map<String, Object> viewArgs = new HashMap<String, Object>();
        Map<String, Object> bookArgs = new HashMap<String, Object>();
        Map<String, Object> chapterArgs = new HashMap<String, Object>();

        viewArgs.put("viewCode", viewCode);
        bookArgs.put("title", book);

        // get view_id
        List<View> viewList = (List<View>) viewDao.get(viewArgs).get();
        View view = viewList.get(0);
        long viewId = view.getViewId();

        // get book_id
        List<Book> bookResults = (List<Book>)bookDao.get(bookArgs).get();
        Book bookResult = bookResults.get(0);

        // get chapter_id
        chapterArgs.put("bookId", bookResult.getBookId());
        chapterArgs.put("chapterNo", chapterNo);
        List<Chapter> chapterIds = (List<Chapter>)chapterDao.get(chapterArgs).get();
        Chapter chapter = chapterIds.get(0);
        long chapterId = chapter.getChapterId();

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("viewId", viewId)
                .addValue("chapterId", chapterId);

        String sql = "SELECT * FROM notes " +
                "JOIN view_note ON view_note.note_id = notes.note_id " +
                "JOIN chapters ON notes.chapter_id = chapters.chapter_id " +
                "WHERE chapters.chapter_id = :chapterId " +
                "AND view_note.view_id = :viewId";

        List<Note> result = namedParameterJdbcTemplate.query(sql, params, NoteDao::mapRow);

        return CompletableFuture.completedFuture(result);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
}