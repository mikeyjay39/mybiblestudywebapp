package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.model.Note;
import com.mybiblestudywebapp.persistence.NoteDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Optional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/17/19
 */
public class NoteUnitTests {

    private Note note;
    private NoteDao noteDao;

    @Before
    public void setUp() throws Exception {
        note = null;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DbConnectionTest.rebuildEmbeddedDataBase());
        noteDao = new NoteDao(jdbcTemplate,
                new NamedParameterJdbcTemplate(jdbcTemplate));
    }

    @After
    public void tearDown() {
        note = null;
    }

    public NoteUnitTests() {
        //JdbcTemplate jdbcTemplate = new JdbcTemplate(DbConnectionTest.getEmbeddedPostgres());
        noteDao = new NoteDao(DbConnectionTest.getJdbcTemplate(),
                new NamedParameterJdbcTemplate(DbConnectionTest.getJdbcTemplate()));
    }

    @Test
    public void testSave() throws Exception {
        saveNote();
    }

    @Test
    public void testUpdate() {
        Note retrievedNote = saveAndGet();
        note.setNoteText("The note has been modified!");
        boolean result = noteDao.update(note);
        Note updatedNote = noteDao.get(note.getNoteId()).get();
        Assert.assertTrue(result);
        Assert.assertEquals("The note has been modified!", updatedNote.getNoteText());
    }

    @Test
    public void testDelete() {
        note = saveAndGet();
        boolean result = noteDao.delete(note);
        Assert.assertTrue(result);
        Optional<Note> retrievedNote = noteDao.get(1);
        Assert.assertTrue(retrievedNote.isEmpty());
    }

    private Note saveAndGet() {
        note = saveNote();
        note.setNoteId(1);
        note = noteDao.get(1l).get();
        Assert.assertEquals("This is the first note", note.getNoteText());
        return note;
    }


    private Note saveNote() {
        note = new Note();
        note.setUserId(1);
        note.setBookId(1);
        note.setChapterId(1);
        note.setNoteText("A new note!");
        note.setPriv(false);
        note.setLang("en");
        long result = noteDao.save(note);
        Assert.assertTrue(result > -1);
        return note;
    }
}
