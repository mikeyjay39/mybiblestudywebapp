package com.mybiblestudywebapp.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/18/19
 */
public class ViewDao implements UpdatableDao<View> {

    private static final Logger logger = LoggerFactory.getLogger(ViewDao.class);
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ViewDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }


    /**
     * Creates a View in the DB
     * @param view
     * @return
     */
    @Override
    public boolean save(View view) {
        String sql = "INSERT INTO views (user_id, priv) " +
                "VALUES (:userId, :priv)";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", view.getUserId())
                .addValue("priv", view.isPriv());
        int rows = 0;
        rows = namedParameterJdbcTemplate.update(sql, params, holder);
        return rows > 0;
    }

    @Override
    public boolean update(View view) {
        return false;
    }

    @Override
    public boolean delete(View view) {
        return false;
    }

    /**
     * Query DB for a single view
     * @param id
     * @return
     */
    @Override
    public Optional<View> get(long id) {
        String sql = "SELECT * FROM views WHERE view_id = :viewId";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("viewId", id);
        View result = null;
        try {
            result = namedParameterJdbcTemplate.queryForObject(sql, params, ViewDao::mapRow);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Could not get view_id = " + id + "\n" + e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<View> getUnique(String uniqueKey) {
        return Optional.empty();
    }

    @Override
    public List<View> getAll() {
        return null;
    }

    private static View mapRow(ResultSet rs, int rowNum) throws SQLException {
        View view = new View();
        view.setViewId(rs.getInt("view_id"));
        view.setUserId(rs.getInt("user_id"));
        view.setViewCode(rs.getString("view_code"));
        view.setPriv(rs.getBoolean("priv"));
        return view;
    }
}
