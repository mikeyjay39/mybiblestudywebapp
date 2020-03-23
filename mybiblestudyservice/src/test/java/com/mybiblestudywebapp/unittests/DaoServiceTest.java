package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistenceservice.persistence.*;
import com.mybiblestudywebapp.utils.persistence.model.Note;
import com.mybiblestudywebapp.utils.persistence.model.View;
import com.mybiblestudywebapp.utils.persistence.model.ViewNote;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

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
        jdbcTemplate = DbConnectionTest.getJdbcTemplate();
        daoService = new DaoServiceJdbcImpl(jdbcTemplate,
                new NamedParameterJdbcTemplate(jdbcTemplate));
    }

    @Test
    public void addUserNotesToView() throws Exception {
        List<Long> noteIds = createNotes();
        long viewId = createView();
        var completedResult = daoService.addUserNotesToView(1, 1);
        long result = completedResult.get();
        Assert.assertTrue(result > 0);

        // test that viewNote was added
        Dao viewNoteDao = new ViewNoteDao(jdbcTemplate, new NamedParameterJdbcTemplate(jdbcTemplate));
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

        Dao viewDao = new ViewDao(jdbcTemplate, new NamedParameterJdbcTemplate(jdbcTemplate));
        View view = (View) viewDao.get(1).get();

        List<Note> notes = daoService.getStudyNotesForChapter(view.getViewCode(), "Genesis", 2).get();
        Assert.assertTrue(notes.size() > 0);
    }

    private List<Long> createNotes() {
        UpdatableDao noteDao = new NoteDao(jdbcTemplate, new NamedParameterJdbcTemplate(jdbcTemplate));
        List<Long> result = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Note note = new Note();
            note.setUserId(1);
            note.setBookId(1);
            note.setChapterId(2);
            note.setVerseStart(i);
            note.setPriv(false);
            note.setLang("en");
            note.setNoteText("this is note #" + i);
            result.add(noteDao.save(note));
            Optional<Note> opt = noteDao.get(i + 2);
            Note testNote = opt.get();
            Assert.assertEquals(i + 2, testNote.getNoteId());
        }
        return result;
    }

    private long createView() {
        UpdatableDao viewDao = new ViewDao(jdbcTemplate, new NamedParameterJdbcTemplate(jdbcTemplate));
        View view = new View();
        view.setUserId(1);
        view.setPriv(false);
        return viewDao.save(view);
    }
}