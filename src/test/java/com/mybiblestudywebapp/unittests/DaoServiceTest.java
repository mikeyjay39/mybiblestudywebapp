package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.*;
import com.mybiblestudywebapp.persistence.model.Note;
import com.mybiblestudywebapp.persistence.model.View;
import com.mybiblestudywebapp.persistence.model.ViewNote;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/20/19
 */
public class DaoServiceTest {

    private JdbcTemplate jdbcTemplate;
    private DaoService daoService;

    public DaoServiceTest() {
        jdbcTemplate = new JdbcTemplate(DbConnectionTest.getEmbeddedPostgres());
        daoService = new DaoServiceJdbcImpl(jdbcTemplate);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void addUserNotesToView() throws Exception {
        List<Long> noteIds = createNotes();
        long viewId = createView();
        var completedResult = daoService.addUserNotesToView(1, 1);
        long result = completedResult.get();
        Assert.assertTrue(result > 0);

        // test that viewNote was added
        Dao viewNoteDao = new ViewNoteDao(jdbcTemplate);
        var allViewNotes = viewNoteDao.getAll();
        Map<String, Object> args = new HashMap<>();
        args.put("viewId", 1l);
        args.put("chapterId", 1);
        Optional<List<ViewNote>> viewNoteResult = viewNoteDao.get(args);
        List<ViewNote> viewNotes = viewNoteResult.get();
        Assert.assertTrue(viewNotes.size() > 0);
    }

    @Test
    public void testGetStudyNotes() throws Exception {
        createNotes();
        var completedResult = daoService.addUserNotesToView(1, 1);
        long result = completedResult.get();
        Assert.assertTrue(result > 0);

        Dao viewDao = new ViewDao(jdbcTemplate);
        View view = (View)viewDao.get(1).get();

        List<Note> notes = daoService.getStudyNotesForChapter(view.getViewCode(), "Genesis", 1).get();
        Assert.assertTrue(notes.size() > 0);
    }

    private List<Long> createNotes() {
        UpdatableDao noteDao = new NoteDao(jdbcTemplate);
        List<Long> result = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Note note = new Note();
            note.setUserId(1);
            note.setBookId(1);
            note.setChapterId(1);
            //note.setVerse(1);
            note.setPriv(false);
            note.setLang("en");
            note.setNote("this is note #" + i);
            result.add(noteDao.save(note));
            Optional<Note> opt = noteDao.get(i + 2);
            Note testNote = opt.get();
            Assert.assertEquals(i + 2, testNote.getNoteId());
        }
        return result;
    }

    private long createView() {
        UpdatableDao viewDao = new ViewDao(jdbcTemplate);
        View view = new View();
        view.setUserId(1);
        view.setPriv(false);
        return viewDao.save(view);
    }
}