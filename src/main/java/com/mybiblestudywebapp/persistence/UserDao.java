package com.mybiblestudywebapp.persistence;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        var optional = Optional.ofNullable(users.get((int) id));
        // validId is needed in case the user id (long) is larger than the max size of the array (int)
        boolean validId = id < Integer.MAX_VALUE && id >= 0;

        if (validId && optional.isPresent()) {
            return optional;
        } else {
            String sql = "SELECT * FROM users WHERE user_id = ?";
            var result = jdbcTemplate.queryForList(sql, id);
            User user = null;
            if (result.size() > 0) {
                user = buildUser(result.get(0));
            }
            if (validId) {
                users.add((int)user.getUserId(), user);
            }
            return Optional.ofNullable(user);
        }
    }

    /**
     * Gets user from database based on email value
     * @param email
     * @return User from database
     */
    public Optional<User> get(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        var result = jdbcTemplate.queryForList(sql, email);
        User user = null;
        if (result.size() > 0) {
            user = buildUser(result.get(0));
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    /**
     * Writes User into database
     * @param user
     * @return true on success
     */
    @Override
    public boolean save(User user) {
        boolean result = false;
        String sql = "INSERT INTO users (email, firstname, lastname, password) VALUES (?, ?, ?, ?);";
        int rows = jdbcTemplate.update(
                sql, user.getEmail(), user.getFirstname(), user.getLastname(), user.getPassword()
        );
        if (rows > 0) {
            result = true;
        }
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

    /**
     * Builds a User object after SQL query
     * @param sqlrow The result row
     * @return User DTO
     */
    private User buildUser(Map<String, Object> sqlrow) {
        User user = new User();
        user.setUserId((long)sqlrow.get("user_id"));
        user.setEmail((String)sqlrow.get("email"));
        user.setFirstname((String)sqlrow.get("firstname"));
        user.setLastname((String)sqlrow.get("lastname"));
        user.setPassword((String)sqlrow.get("password"));
        user.setRanking((int)sqlrow.get("ranking"));
        user.setCreatedAt(((Timestamp)sqlrow.get("created_at")).toLocalDateTime());
        return user;
    }
}
