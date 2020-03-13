package com.mybiblestudywebapp;

import com.mybiblestudywebapp.integrationtests.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 11/4/19
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        BibleStudyControllerTest.class,
        GetBibleServiceTest.class,
        LoginControllerTest.class,
        MainServiceTest.class,
        NotesControllerTest.class,
        UsersControllerTest.class,
        ViewsControllerTest.class
})
public class IntegrationTestSuite {
}
