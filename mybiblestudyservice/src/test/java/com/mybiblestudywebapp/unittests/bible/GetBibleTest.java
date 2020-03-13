package com.mybiblestudywebapp.unittests.bible;

import com.mybiblestudywebapp.getbible.GetBible;
import com.mybiblestudywebapp.getbible.GetBibleImpl;
import com.mybiblestudywebapp.getbible.GetBibleResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
public class GetBibleTest {

    private GetBibleResponse getBibleResponse;
    @Autowired
    private GetBible getBible = new GetBibleImpl();


    @Before
    public void setUp() throws Exception {
        getBibleResponse = null;
    }

    @After
    public void tearDown() throws Exception {
        getBibleResponse = null;
    }

    @Test
    public void getVersesForChapter() {
        getBibleResponse = getBible.getVersesForChapter("Exodus", 1);
        List<Map<String, String>> verses = getBibleResponse.getVerses();
        Assert.assertNotNull(verses);
    }
}