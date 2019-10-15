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
    private User user;


    public UserUnitTests() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(DbConnectionTest.getEmbeddedPostgres());
        userDao = new UserDao(jdbcTemplate);
        user = createTestUser();
    }

    @Test
    public void testAddUser() throws Exception {
        addUser(user);
    }

    @Test
    public void testGetUser() throws Exception {
        addUser(user);
        User resultUser = getUserByEmail(user.getEmail());
        Assert.assertNotNull(resultUser);

    }

    private User getUserByEmail(String email) {
        long id = -1;
        User user = userDao.get(email).orElse(null);
        return user;
    }

    private User createTestUser() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setFirstname("Han");
        user.setLastname("Solo");
        user.setPassword("12345");
        return user;
    }

    private void addUser(User user) throws Exception {
        boolean result = userDao.save(user);
        Assert.assertTrue(result);
    }
}
