package com.mybiblestudywebapp.persistenceservice.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybiblestudywebapp.persistenceservice.cache.CacheService;
import com.mybiblestudywebapp.utils.persistence.DaoServiceException;
import com.mybiblestudywebapp.utils.persistence.model.*;
import com.mybiblestudywebapp.utils.http.*;
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
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.*;
import java.util.stream.IntStream;

import static com.mybiblestudywebapp.utils.main.Constants.*;

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
    private UpdatableDao viewNoteDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private UserSession userSession;

    public DaoServiceJdbcImpl() {
    }

    /**
     * Used for unit tests. Pass the JdbcTemplate with the embedded postgres datasource as the arg.
     *
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
    public Long addUserNotesToView(long userId, long viewId) {
        String sql = "SELECT * FROM notes WHERE user_id = :userId AND priv = false " +
                "AND NOT EXISTS (" +
                " SELECT * FROM view_note AS vn " +
                "WHERE vn.view_id = :viewId AND vn.note_id = notes.note_id)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(USER_ID, userId)
                .addValue(VIEW_ID, viewId);
        List<Note> results = null;

        try {
            results = namedParameterJdbcTemplate.query(sql, params, NoteDao::mapRow);
            if (results.isEmpty()) {
                return (long) results.size();
            }
        } catch (DataAccessException e) {
            String errMsg = String.format("Could not find notes for user_id: %s %n%s", userId, e.getMessage());
            logger.info(errMsg);
            return 0L;
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

        cacheService.clearDashboardCache();

        try {
            updateCounts = namedParameterJdbcTemplate.batchUpdate(sqlInsert, batch);
        } catch (DuplicateKeyException e) {
            logger.error(e.getMessage());
        }

        int total = IntStream.of(updateCounts == null ? new int[]{0} : updateCounts)
                .reduce(0, (a, b) -> a + b);

        return (long) total;
    }

    /**
     * {@inheritDoc}
     *
     * @param viewCode  if viewCode is 0 then get ALL public notes from every author
     * @param book
     * @param chapterNo
     * @return
     */
    @Override
    public List<Note> getStudyNotesForChapter(String viewCode, String book, long chapterNo) {
        Map<String, Object> viewArgs = new HashMap<>();
        Map<String, Object> bookArgs = new HashMap<>();
        Map<String, Object> chapterArgs = new HashMap<>();

        viewArgs.put("viewCode", viewCode);
        bookArgs.put("title", book);

        // get view_id
        Optional<List<View>> viewOpt = viewDao.get(viewArgs);

        List<View> viewList = null;

        View view = null;
        long viewId = 0;

        if (!"0".equals(viewCode)) {
            // not the generic view case to get viewId
            viewOpt = viewDao.get(viewArgs);

            if (viewOpt.isEmpty()) {
                logger.error("No views found for viewId: {}", viewCode);
                return List.of();
            }

            viewList = viewOpt.get();
            view = viewList.get(0);
            viewId = view.getViewId();
        }

        // get book_id
        Optional<List<Book>> bookIdOpt = bookDao.get(bookArgs);

        if (bookIdOpt.isEmpty()){
            logger.error("No books returned for: {}", book);
            return List.of();
        }

        List<Book> bookResults = bookIdOpt.get();
        Book bookResult = bookResults.get(0);

        // get chapter_id
        chapterArgs.put("bookId", bookResult.getBookId());
        chapterArgs.put("chapterNo", chapterNo);
        Optional<List<Chapter>> chaptersOpt = chapterDao.get(chapterArgs);

        if (chaptersOpt.isEmpty()) {
            logger.error("No chapters returned for: {} {}", book, chapterNo);
            return List.of();
        }

        List<Chapter> chapterIds = chaptersOpt.get();
        Chapter chapter = chapterIds.get(0);
        long chapterId = chapter.getChapterId();

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("viewId", viewId)
                .addValue("chapterId", chapterId);

        String sql = "";

        // check for all notes or view specific notes
        if ("0".equals(viewCode)) {
            // get all public notes
            sql = "SELECT * FROM notes " +
                    "JOIN chapters ON notes.chapter_id = chapters.chapter_id " +
                    "WHERE chapters.chapter_id = :chapterId AND notes.priv = false " +
                    "ORDER BY notes.verse_start, notes.verse_end, notes.note_id ASC";
        } else {
            // view code was set so get view specific notes
            sql = "SELECT * FROM notes " +
                    "JOIN view_note ON view_note.note_id = notes.note_id " +
                    "JOIN chapters ON notes.chapter_id = chapters.chapter_id " +
                    "WHERE chapters.chapter_id = :chapterId " +
                    "AND view_note.view_id = :viewId " +
                    "ORDER BY notes.verse_start, notes.verse_end, notes.note_id ASC";
        }

        List<Note> result = namedParameterJdbcTemplate.query(sql, params, NoteDao::mapRow);

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @param user
     * @return
     */
    @Override
    public User createUserAccount(User user)
            throws DaoServiceException {

        Long userId = userDao.save(user);

        if (userId == null) {
            throw new DaoServiceException("Could not create user: " + user.getEmail());
        }

        user.setUserId(userId);
        return user;
    }

    /**
     * {@inheritDoc}
     *
     * @param request
     * @return
     * @throws DaoServiceException
     */
    @Override
    public long addNote(Note request) {
        request.setUserId(userSession.userId);
        long result = noteDao.save(request);

        if (result < 0) {
            logger.error("Could not add new note: {}", request.getNoteText());
        }

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @param request
     * @return
     * @throws DaoServiceException
     */
    @Override
    @Transactional
    public RankNoteResponse rankNote(RankNoteRequest request) {

        RankNoteResponse response = new RankNoteResponse();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(400);

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
                .addValue(USER_ID, userSession.userId)
                .addValue("rankingValue", request.isIncreaseRanking() ? 1 : -1);

        // check if ranking already exists
        boolean exists = false;
        Integer rankingValueResult = null;

        try {
            rankingValueResult = namedParameterJdbcTemplate.queryForObject(checkRankingSql, params, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            // no results
        } catch (DataAccessException e) {
            String errMsg = String.format("No note retrieved for note_id: %s user_id: %s%n%s",
                    request.getNoteId(),
                    request.getUserId(),
                    e.getMessage());
            logger.error(errMsg);
            errorResponse.setDetail(errMsg);
            response.setErrorResponse(errorResponse);
            return response;
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
                return response;
            }
        } else {
            // decrease ranking case
            if (currentRankingValue < 0) {
                // already ranked negative so return
                response.setResult("Already ranked negative");
                return response;
            }
        }

        if (exists) {
            // already exists so update note and note_ranking
            int rows = namedParameterJdbcTemplate.update(updateRankNote, params);
            if (rows < 1) {
                String errMsg = "Could not update rank_note table for noteId " + request.getNoteId() + "\n";
                logger.error(errMsg);
                errorResponse.setDetail(errMsg);
                response.setErrorResponse(errorResponse);
                return response;
            }
        } else {
            // does not exist so insert into note_ranking and update note table
            int rows = namedParameterJdbcTemplate.update(insertRankNote, params);
            if (rows < 1) {
                String errMsg = "Could not insert into rank_note table for noteId " + request.getNoteId() +
                        "\n";
                logger.error(errMsg);
                errorResponse.setDetail(errMsg);
                response.setErrorResponse(errorResponse);
                return response;            }
        }

        // update note table
        int rows = namedParameterJdbcTemplate.update(updateNote, params);
        if (rows < 1) {
            String errMsg = "Could not update note table for noteId " + request.getNoteId() + "\n";
            logger.error(errMsg);
            errorResponse.setDetail(errMsg);
            response.setErrorResponse(errorResponse);
            return response;        }

        response.setResult("success");
        return response;
    }

    /**
     * {@inheritDoc}
     *
     * @param username
     * @return
     */
    @Override
    public LoginResponse login(String username) {
        LoginResponse loginResponse = new LoginResponse();
        String sql = "SELECT user_id FROM users WHERE email = :username";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", username);
        long userId = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        userSession.userId = userId;
        loginResponse.setUserId(userId);
        return loginResponse;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @Transactional
    public long addView() {
        View view = new View();
        view.setUserId(userSession.userId);
        long viewId = viewDao.save(view);

        if (viewId < 0) {
            String errMsg = String.format("Could not add new view for user_id: %s", userSession.userId);
            logger.error(errMsg);
        }

        return viewId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Integer> getChapter(String book, int chapterNo) {
        String sql = "SELECT b.book_id, c.chapter_id FROM books AS b " +
                "JOIN chapters AS c ON b.book_id = c.book_id " +
                "WHERE b.title = :title AND c.chapter_no = :chapterNo";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", book)
                .addValue("chapterNo", chapterNo);

        Map<String, Integer> queryResult = new HashMap<>();

        try {
            SqlRowSet rs = namedParameterJdbcTemplate.queryForRowSet(sql, params);

            if (rs.next()) {
                queryResult.put("bookId", rs.getInt("book_id"));
                queryResult.put("chapterId", rs.getInt("chapter_id"));
            }
        } catch (DataAccessException e) {
            logger.error("Could not find IDs for book: {}  and chapter: {} \n{}", book, chapterNo, e.getMessage());
            return Map.of();
        }

        return queryResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getViews() {
        return ((ViewDao) viewDao).getAllCodesForUser(userSession.userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeNoteFromView(String viewcode, long noteId) {
        Map<String, Object> args = new HashMap<>();
        args.put("viewCode", viewcode);
        List<View> viewList = (List<View>) viewDao.get(args).get();
        ViewNote viewNote = new ViewNote();
        viewNote.setViewId(viewList.get(0).getViewId());
        viewNote.setNoteId(noteId);
        cacheService.clearDashboardCache();

        if (viewNoteDao.delete(viewNote)) {
            cacheService.clearDashboardCache();
            return "success";
        } else {
            return "failure";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String deleteView(String viewcode) {
        String sql = "SELECT v.view_id FROM views AS v JOIN " +
                "users AS u on u.user_id = v.user_id " +
                "WHERE u.user_id = :userId AND v.view_code = :viewCode";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("viewCode", viewcode, Types.OTHER)
                .addValue("userId", userSession.userId);

        Long viewId = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);

        if (viewId == null) {
            String errMsg = String.format("Could not match viewcode: %s to user_id: %s",viewcode, userSession.userId);
            logger.error(errMsg);
            return errMsg;
        }

        View view = new View();
        view.setViewId(viewId);
        cacheService.clearDashboardCache();

        if (viewDao.delete(view)) {
            return "success";
        } else {
            String errMsg = String.format("Could not delete viewCode: %s",viewcode);
            logger.error(errMsg);
            return errMsg;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUsers() {
        var result = userDao.getAll();

        if (result.isEmpty()) {
            logger.error("No users returned by query");
            return List.of();
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String addNotesToView(String viewcode, long authorId, int ranking) {
        Map<String, Object> args = new HashMap<>();
        args.put("viewCode", viewcode);
        List<View> viewList = (List<View>) viewDao.get(args).get();
        View view = viewList.get(0);

        String sql = "SELECT * FROM notes WHERE user_id = :authorId " +
                "AND priv = false AND ranking >= :ranking " +
                "AND NOT EXISTS (" +
                " SELECT * FROM view_note AS vn " +
                "WHERE vn.view_id = :viewId AND vn.note_id = notes.note_id)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("authorId", authorId)
                .addValue("viewId", view.getViewId())
                .addValue("ranking", ranking);
        List<Note> results;
        cacheService.clearDashboardCache();

        try {
            results = namedParameterJdbcTemplate.query(sql, params, NoteDao::mapRow);
            if (results.isEmpty()) {
                String errMsg = String.format("No notes found to be added to view");
                logger.error(errMsg);
                return errMsg;
            }
        } catch (DataAccessException e) {
            String errMsg = String.format("Could not find notes from author: %s%n%s", authorId, e.getMessage());
            logger.info(errMsg);
            return errMsg;
        }

        List<ViewNote> viewNotes = new ArrayList<>();

        for (Note note : results) {
            ViewNote viewNote = new ViewNote();
            viewNote.setViewId(view.getViewId());
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

        return total > 0 ? "success" : "no notes added";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> getAllChapterNotesForUser(String book, long chapterNo, long userId) {
        Map<String, Object> args = new HashMap<>();
        args.put("userId", userId);
        args.put("book", book);
        args.put("chapterNo", chapterNo);

        // make sure only private notes are selected if this comes from the logged in user
        args.put("priv", userId != userSession.userId);

        var opt = noteDao.get(args);

        return (List<Note>) opt.orElseGet(List::of);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String updateNote(Note note) {

        // set user_id here to make sure the user isn't trying to update a note they don't own
        note.setUserId(userSession.userId);

        // set default language to en if none is specified
        if (note.getLang() == null) {
            note.setLang("en");
        }

        cacheService.clearDashboardCache();

        if (noteDao.update(note)) {
            return "success";
        } else {
            String errMsg = String.format("Could note update note_id: %s", note.getNoteId());
            logger.error(errMsg);
            return errMsg;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public String deleteNote(long noteId) {

        Note note = new Note();
        note.setNoteId(noteId);
        // TODO add userID to all HTTP requests and use that here instead
        note.setUserId(userSession.userId);
        cacheService.clearDashboardCache();

        if (noteDao.delete(note)) {
            return "success";
        } else {
            String errMsg = String.format("Could note delete noteId: " + noteId);
            logger.error(errMsg);
            return errMsg;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> getComments(long noteId) {

        // Check if user is requesting their own notes. We don't want them viewing comments on private notes.
        boolean userOwnsNote = true;
        Optional<Note> opt = noteDao.get(noteId);

        if (opt.isPresent()) {
            userOwnsNote = userSession.userId == opt.get().getUserId();
        }

        // Build args
        Map<String, Object> args = new HashMap<>();
        args.put(NOTE_ID, noteId);
        args.put("userOwnsNote", userOwnsNote);

        Optional<List<Comment>> optComments = commentDao.get(args);

        if (optComments.isEmpty()) {
            logger.error("Couldn't retrieve comments for note_id: " + noteId);
            return List.of();
        }

        return optComments.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Response addComent(Comment comment) {
        comment.setUserId(userSession.userId);
        GenericResponse response = new GenericResponse();

        long result = commentDao.save(comment);

        if (result <= 0) {
            String errMsg = String.format("Could not add comment to note_id: " + comment.getNoteId());
            logger.error(errMsg);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setDetail(errMsg);
            errorResponse.setStatus(400);
            response.setErrorResponse(errorResponse);
            response.setStatus("failure");
            return response;
        }

        response.setUserId(userSession.userId);
        response.setStatus("success");
        response.setEntityId(result);
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Response addNoteToView(String viewcode, long noteId) {
        Map<String, Object> args = new HashMap<>();
        args.put("viewCode", viewcode);
        var opt = viewDao.get(args);
        long viewId;
        GenericResponse response = new GenericResponse();

        if (opt.isEmpty()) {
            String errMsg = String.format("Could not get id for viewcode: %s");
            logger.error(errMsg);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setDetail(errMsg);
            errorResponse.setStatus(400);
            response.setErrorResponse(errorResponse);
            response.setStatus("failure");
            return response;
        } else {
            viewId = ((List<View>) opt.get()).get(0).getViewId();
        }

        ViewNote viewNote = new ViewNote();
        viewNote.setViewId(viewId);
        viewNote.setNoteId(noteId);
        cacheService.clearDashboardCache();

        if (viewNoteDao.save(viewNote) < 1) {
            String errMsg =
                    String.format("Could note add note_id %s to view_id %s%nNote already exists", noteId, viewId);
            logger.error(errMsg);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setDetail(errMsg);
            errorResponse.setStatus(400);
            response.setErrorResponse(errorResponse);
            response.setStatus("failure");
            return response;
        }

        response.setStatus("success");
        return response;
    }
}
