package com.mybiblestudywebapp.persistenceservice;

import com.mybiblestudywebapp.persistenceservice.unittests.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/23/19
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        BookUnitTests.class,
        ChapterUnitTests.class,
        CommentDaoTest.class,
        DaoServiceTest.class,
        DbConnectionTest.class,
        NoteUnitTests.class,
        UserUnitTests.class,
        ViewNoteDaoTest.class,
        ViewUnitTests.class
})
public class UnitTestSuite {
}
