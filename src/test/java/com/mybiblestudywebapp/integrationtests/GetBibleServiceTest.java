package com.mybiblestudywebapp.integrationtests;

import com.mybiblestudywebapp.getbible.GetBibleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GetBibleServiceTest {

    @Autowired
    private GetBibleService getBibleService;

    @Test
    public void getVersesForChapter() throws Exception {
        var result = getBibleService.getVersesForChapter("Genesis",  1).get();
        Assert.assertNotNull(result);
    }
}