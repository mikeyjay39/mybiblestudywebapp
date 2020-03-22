package com.mybiblestudywebapp.integrationtests;

import com.mybiblestudywebapp.utils.http.RankNoteRequest;
import com.mybiblestudywebapp.dashboard.users.CreateUserRequest;
import com.mybiblestudywebapp.dashboard.users.CreateUserResponse;
import com.mybiblestudywebapp.main.MainService;
import com.mybiblestudywebapp.persistenceservice.persistence.NoteDao;
import com.mybiblestudywebapp.persistenceservice.persistence.UpdatableDao;
import com.mybiblestudywebapp.utils.persistence.model.Note;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/27/19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MainServiceTest {

    @Autowired
    private MainService mainService;

    @Autowired
    private NoteDao noteDao;

    @Rollback
    @Test
    public void createUserAccount() throws Exception {
        CreateUserRequest user = new CreateUserRequest();
        String password = "testpass";
        user.setPassword(password);
        user.setFirstname("Main Service");
        user.setLastname("Integration Test");
        user.setEmail("mainservice@intergation.test");
        var result = mainService.createUserAccount(user);
        CreateUserResponse body = (CreateUserResponse) result.getBody();
        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assert.assertNotNull(body.getUserId());
    }

    @Rollback
    @Test
    public void rankNote() {
        RankNoteRequest request = new RankNoteRequest();
        request.setNoteId(1);
        request.setUserId(2);
        request.setIncreaseRanking(false);
        var result = mainService.rankNote(request);
        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
        Note note = noteDao.get(1).get();
        Assert.assertTrue(note.getRanking() < 1);
    }
}
