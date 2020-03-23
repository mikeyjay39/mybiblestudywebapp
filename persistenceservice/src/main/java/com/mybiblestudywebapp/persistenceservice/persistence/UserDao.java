package com.mybiblestudywebapp.persistenceservice.persistence;

import com.mybiblestudywebapp.utils.persistence.DaoServiceException;
import com.mybiblestudywebapp.utils.persistence.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import static com.mybiblestudywebapp.utils.main.Constants.*;


/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 14.
 */
@Component
public class UserDao implements UpdatableDao<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate,
                   NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<User> get(long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        var result = jdbcTemplate.queryForList(sql, id);
        User user = null;
        if (!result.isEmpty()) {
            user = buildUser(result.get(0));
        }
        return Optional.ofNullable(user);
    }

    /**
     * Gets user from database based on email value
     *
     * @param args key is email
     * @return User from database
     */
    @Override
    public Optional<List<User>> get(Map<String, Object> args) {
        String sql = "SELECT * FROM users WHERE email = :email";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(EMAIL, args.get(EMAIL));
        List<User> result = null;
        try {
            result = namedParameterJdbcTemplate.query(sql, params, UserDao::mapRow);
        } catch (DataAccessException e) {
            String errMsg = "Error getting users with email = " + args.get(EMAIL) +
                    "\n" + e.getMessage();
            LOGGER.error(errMsg);
        }

        return Optional.ofNullable(result);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users ORDER BY lastname, firstname";
        var result = jdbcTemplate.queryForList(sql);
        if (result.isEmpty()) {
            return new ArrayList<>();
        }
        List<User> users = new ArrayList<>();
        for (Map<String, Object> row : result) {
            users.add(buildUser(row));
        }
        return users;
    }

    /**
     * Writes User into database
     *
     * @param user
     * @return user_id or -1 on failure
     */
    @Transactional
    @Override
    public long save(User user) {
        String sql = "INSERT INTO users (email, firstname, lastname, password) " +
                "VALUES (:email, :firstname, :lastname, :password) " +
                "RETURNING user_id";

        String userAuthoritiesSql = "INSERT INTO user_authorities (email, authority) "
                + "VALUES (:email, :authority)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(EMAIL, user.getEmail())
                .addValue(FIRSTNAME, user.getFirstname())
                .addValue(LASTNAME, user.getLastname())
                .addValue(PASSWORD, user.getPassword())
                .addValue("authority", "USER");

        Long userId = null;
        try {
            userId = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
            int rows = namedParameterJdbcTemplate.update(userAuthoritiesSql, params);
            if (userId == null || rows < 1) {
                throw new DaoServiceException("Could not insert into user_authorities table");
            }
        } catch (DaoServiceException e) {
            userId = null;
            String errMsg = "Could not add user for email: " + user.getEmail() +
                    "\n" + e.getMessage();
            LOGGER.info(errMsg);
        }

        return userId;
    }

    /**
     * Updates the entry in the users table
     *
     * @param user
     * @return
     */
    @Transactional
    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET email = :email, " +
                "firstname = :firstname, lastname = :lastname, password = :password, " +
                "ranking = :ranking, created_at = :createdAt WHERE user_id = :userId";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(EMAIL, user.getEmail())
                .addValue("userId", user.getUserId())
                .addValue(FIRSTNAME, user.getFirstname())
                .addValue(LASTNAME, user.getLastname())
                .addValue(PASSWORD, user.getPassword())
                .addValue(RANKING, user.getRanking())
                .addValue("createdAt", Timestamp.valueOf(user.getCreatedAt()));
        int rows = 0;
        rows = namedParameterJdbcTemplate.update(sql, params, holder);
        return rows > 0;
    }

    /**
     * @param user
     * @param params
     * @return
     * @deprecated
     */
    @Deprecated(since = "Another method exists that accepts User object")
    public boolean update(User user, Map<String, Object> params) {
        Set<String> columns = params.keySet();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE users SET ");
        int columnsSize = columns.size();
        int i = 0;

        for (String column : columns) {
            stringBuilder.append(column + " = '" + params.get(column) + "'");
            i++;
            if (i < columnsSize) {
                stringBuilder.append(",");
            }
        }

        stringBuilder.append(" WHERE user_id = " + user.getUserId());
        String sql = stringBuilder.toString();
        int rows = jdbcTemplate.update(sql);
        return rows > 0;
    }

    @Transactional
    @Override
    public boolean delete(User user) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        int rows = jdbcTemplate.update(sql, user.getUserId());
        return rows > 0;
    }

    private static User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setEmail(rs.getString(EMAIL));
        user.setFirstname(rs.getString(FIRSTNAME));
        user.setLastname(rs.getString(LASTNAME));
        user.setPassword(rs.getString(PASSWORD));
        user.setRanking(rs.getInt(RANKING));
        user.setCreatedAt((rs.getTimestamp("created_at")).toLocalDateTime());
        return user;
    }

    /**
     * Builds a User object after SQL query
     *
     * @param sqlrow The result row
     * @return User DTO
     */
    private User buildUser(Map<String, Object> sqlrow) {
        User user = new User();
        user.setUserId((long) sqlrow.get("user_id"));
        user.setEmail((String) sqlrow.get("email"));
        user.setFirstname((String) sqlrow.get("firstname"));
        user.setLastname((String) sqlrow.get("lastname"));
        user.setPassword((String) sqlrow.get("password"));
        user.setRanking((int) sqlrow.get("ranking"));
        user.setCreatedAt(((Timestamp) sqlrow.get("created_at")).toLocalDateTime());
        return user;
    }
}
