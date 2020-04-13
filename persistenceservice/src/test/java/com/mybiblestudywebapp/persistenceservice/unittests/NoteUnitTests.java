package com.mybiblestudywebapp.persistenceservice.unittests;

import com.mybiblestudywebapp.utils.persistence.model.Note;
import com.mybiblestudywebapp.persistenceservice.persistence.NoteDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/17/19
 */
@Transactional
public class NoteUnitTests {

    private Note note;
    private NoteDao noteDao;

    @After
    public void tearDown() {
        note = null;
    }

    public NoteUnitTests() {
        noteDao = new NoteDao(DbConnectionTest.getJdbcTemplate(),
                new NamedParameterJdbcTemplate(DbConnectionTest.getJdbcTemplate()));
    }

    @Test
    public void testSave() throws Exception {
        saveNote();
    }

    @Test
    public void testUpdate() {
        note = saveAndGet();
        note.setNoteText("The note has been modified!");
        boolean result = noteDao.update(note);
        Note updatedNote = noteDao.get(note.getNoteId()).get();
        Assert.assertTrue(result);
        Assert.assertEquals("The note has been modified!", updatedNote.getNoteText());
    }

    @Rollback
    @Test
    public void testDelete() {
        note = saveAndGet();
        long noteId = note.getNoteId();
        boolean result = noteDao.delete(note);
        Assert.assertTrue(result);
        Optional<Note> retrievedNote = noteDao.get(noteId);
        Assert.assertTrue(retrievedNote.isEmpty());
    }

    @Test
    public void getNotesAboveRanking() {
        note = saveAndGet();
        Map<String, Object> args = new HashMap<>();
        args.put("ranking", -1);
        List<Note> retrievedNotes = noteDao.get(args).get();
        Assert.assertNotNull(retrievedNotes);
        Assert.assertTrue(!retrievedNotes.isEmpty());
    }

    private Note saveAndGet() {
        long noteId = saveNote();
        Note retrievedNote = noteDao.get(noteId).get();
        Assert.assertEquals("A new note!", retrievedNote.getNoteText());
        return retrievedNote;
    }


    private long saveNote() {
        note = new Note();
        note.setUserId(1);
        note.setBookId(1);
        note.setChapterId(1);
        note.setNoteText("A new note!");
        note.setPriv(false);
        note.setLang("en");
        long result = noteDao.save(note);
        Assert.assertTrue(result > -1);
        return result;
    }
}
