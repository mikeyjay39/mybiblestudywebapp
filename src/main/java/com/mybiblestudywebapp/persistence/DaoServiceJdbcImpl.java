package com.mybiblestudywebapp.persistence;

import com.mybiblestudywebapp.dashboard.notes.RankNoteRequest;
import com.mybiblestudywebapp.dashboard.notes.RankNoteResponse;
import com.mybiblestudywebapp.dashboard.users.LoginResponse;
import com.mybiblestudywebapp.dashboard.users.UserSession;
import com.mybiblestudywebapp.main.Response;
import com.mybiblestudywebapp.persistence.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.BatchUpdateException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static com.mybiblestudywebapp.main.Constants.*;

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
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserSession userSession;

    public DaoServiceJdbcImpl() {
    }

    /**
     * Used for unit tests. Pass the JdbcTemplate with the embedded postgres datasource as the arg.
     * @param jdbcTemplate
     * @param namedParameterJdbcTemplate
     */
    public DaoServiceJdbcImpl(JdbcTemplate jdbcTemplate,
                              NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        bookDao = new BookDao(jdbcTemplate, namedParameterJdbcTemplate);
        chapterDao = new ChapterDao(jdbcTemplate, namedParameterJdbcTemplate);
        commentDao = new CommentDao(namedParameterJdbcTemplate);
        noteDao = new NoteDao(jdbcTemplate, namedParameterJdbcTemplate);
        userDao = new UserDao(jdbcTemplate, namedParameterJdbcTemplate);
        viewDao = new ViewDao(jdbcTemplate, namedParameterJdbcTemplate);
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
        String sql = "SELECT * FROM notes WHERE user_id = :userId AND priv = false " +
                "AND NOT EXISTS (" +
                " SELECT * FROM view_note AS vn " +
                "WHERE vn.view_id = :viewId AND vn.note_id = notes.note_id)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("viewId", viewId);
        List<Note> results = null;
        try {
            results = namedParameterJdbcTemplate.query(sql, params, NoteDao::mapRow);
            if (results.isEmpty()) {
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
        int[] updateCounts = null;

        try {
            updateCounts = namedParameterJdbcTemplate.batchUpdate(sqlInsert, batch);
        } catch (DuplicateKeyException e) {
            logger.error(e.getMessage());
        }

        int total = IntStream.of(updateCounts == null ? new int[]{0} : updateCounts)
                .reduce(0, (a, b) -> a + b);

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
    public CompletableFuture<List<Note>> getStudyNotesForChapter(String viewCode, String book, long chapterNo)
    throws DaoServiceException {
        Map<String, Object> viewArgs = new HashMap<>();
        Map<String, Object> bookArgs = new HashMap<>();
        Map<String, Object> chapterArgs = new HashMap<>();

        viewArgs.put("viewCode", viewCode);
        bookArgs.put("title", book);

        // get view_id
        Optional<List<View>> viewOpt = viewDao.get(viewArgs);

        List<View> viewList = viewOpt.orElseThrow(
                () -> new DaoServiceException("No views returned for view: " + viewCode)
        );

        View view = viewList.get(0);
        long viewId = view.getViewId();

        // get book_id
        Optional<List<Book>> bookIdOpt = bookDao.get(bookArgs);

        List<Book> bookResults = bookIdOpt.orElseThrow(
                () -> new DaoServiceException("No books returned for: " + book)
        );

        Book bookResult = bookResults.get(0);

        // get chapter_id
        chapterArgs.put("bookId", bookResult.getBookId());
        chapterArgs.put("chapterNo", chapterNo);
        Optional<List<Chapter>> chaptersOpt = chapterDao.get(chapterArgs);

        List<Chapter> chapterIds = chaptersOpt.orElseThrow(
                () -> new DaoServiceException("No chapters returned for: " + book + " " + chapterNo)
        );

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

    /**
     * {@inheritDoc}
     * @param user
     * @return
     */
    @Async
    @Override
    public CompletableFuture<User> createUserAccount(User user)
            throws DaoServiceException {
        user.setPassword(encoder.encode(user.getPassword()));

        Long userId = userDao.save(user);

        if (userId == null) {
            throw new DaoServiceException("Could not create user: " + user.getEmail());
        }
        user.setUserId(userId);
        return CompletableFuture.completedFuture(user);
    }

    /**
     * {@inheritDoc}
     * @param request
     * @return
     * @throws DaoServiceException
     */
    @Override
    @Async
    public CompletableFuture<Long> addNote(Note request) throws DaoServiceException {
        long result = noteDao.save(request);

        if (result < 0) {
            throw new DaoServiceException("Could not add new note: " + request.getNoteText());
        }

        return CompletableFuture.completedFuture(result);
    }

    /**
     * {@inheritDoc}
     * @param request
     * @return
     * @throws DaoServiceException
     */
    @Override
    @Async
    @Transactional
    public CompletableFuture<RankNoteResponse> rankNote(RankNoteRequest request) throws DaoServiceException {

        RankNoteResponse response = new RankNoteResponse();
        String checkRankingSql = "SELECT ranking_value FROM rank_note " +
                "WHERE note_id = :noteId AND user_id = :userId";
        String insertRankNote = "INSERT INTO rank_note (note_id, user_id, ranking_value) " +
                "VALUES (:noteId, :userId, :rankingValue)";
        String updateRankNote = "UPDATE rank_note SET ranking_value = ranking_value + :rankingValue " +
                "WHERE note_id = :noteId AND user_id = :userId";
        String updateNote = "UPDATE notes SET ranking = ranking + :rankingValue " +
                "WHERE note_id = :noteId";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(NOTE_ID, request.getNoteId())
                .addValue(USER_ID, request.getUserId())
                .addValue("rankingValue", request.isIncreaseRanking() ? 1 : -1);

        // check if ranking already exists
        boolean exists = false;
        Integer rankingValueResult = null;

        try {
        rankingValueResult = namedParameterJdbcTemplate.queryForObject(checkRankingSql, params, Integer.class);
        } catch (EmptyResultDataAccessException e) {
        // no results
        } catch (DataAccessException e) {
            throw new DaoServiceException("No note retrieved for note_id: " + request.getNoteId() + " user_id: " +
                    request.getUserId() + "\n" + e.getMessage());
        }

        int currentRankingValue = 0;

        if (rankingValueResult != null) {
            currentRankingValue = rankingValueResult;
            exists = true;
        }

        // check if already ranked positive or negative
        if (request.isIncreaseRanking()) {
            // increase ranking case
            if (currentRankingValue > 0) {
                // already ranked positive so return
                response.setResult("Already ranked positive");
                return CompletableFuture.completedFuture(response);
            }
        } else {
            // decrease ranking case
            if (currentRankingValue < 0) {
                // already ranked negative so return
                response.setResult("Already ranked negative");
                return CompletableFuture.completedFuture(response);
            }
        }

        if (exists) {
            // already exists so update note and note_ranking
            int rows = namedParameterJdbcTemplate.update(updateRankNote, params);
            if (rows < 1) {
                String errMsg = "Could not update rank_note table for noteId " + request.getNoteId() + "\n";
                logger.error(errMsg);
                throw new DaoServiceException(errMsg);
            }
        } else {
            // does not exist so insert into note_ranking and update note table
            int rows = namedParameterJdbcTemplate.update(insertRankNote, params);
            if (rows < 1) {
                String errMsg = "Could not insert into rank_note table for noteId " + request.getNoteId() +
                        "\n";
                logger.error(errMsg);
                throw new DaoServiceException(errMsg);
            }
        }

        // update note table
        int rows = namedParameterJdbcTemplate.update(updateNote, params);
        if (rows < 1) {
            String errMsg = "Could not update note table for noteId " + request.getNoteId() + "\n";
            logger.error(errMsg);
            throw new DaoServiceException(errMsg);
        }

        response.setResult("success");
        return CompletableFuture.completedFuture(response);
    }

    /**
     * {@inheritDoc}
     * @param username
     * @return
     */
    @Override
    public Response login(String username) {
        LoginResponse loginResponse = new LoginResponse();
        String sql = "SELECT user_id FROM users WHERE email = :username";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", username);
        long userId = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        userSession.userId = userId;
        loginResponse.setUserId(userId);
        return loginResponse;
    }
}
