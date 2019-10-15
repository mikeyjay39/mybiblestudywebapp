package com.mybiblestudywebapp.persistence;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 14.
 */
public class UserDao implements Dao<User> {

    private JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // TODO replace this with JDBC methods
    private List<User> users = new ArrayList<>();

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(users.get((int) id));
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public boolean save(User user) {
        boolean result = false;
        users.add(user);
        return result;
    }

    @Override
    public void update(User user, String[] params) {
        // TODO finish this

        users.add(user);
    }

    @Override
    public void delete(User user) {
        users.remove(user);
    }
}
