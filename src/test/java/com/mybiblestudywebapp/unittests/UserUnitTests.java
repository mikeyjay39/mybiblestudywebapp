package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.User;
import com.mybiblestudywebapp.persistence.UserDao;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 14.
 */
public class UserUnitTests {

    private UserDao userDao;

    public UserUnitTests() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(DbConnectionTest.getEmbeddedPostgres());
    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User();
        boolean result = userDao.save(user);
        Assert.assertTrue(result);
    }
}
