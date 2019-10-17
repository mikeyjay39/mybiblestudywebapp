package com.mybiblestudywebapp.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/17/19
 */
public class ChapterDao implements Dao<Chapter> {

    private JdbcTemplate jdbcTemplate;

    public ChapterDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Chapter> get(long id) {
        String sql = "SELECT * FROM chapters WHERE chapter_id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{id}, ChapterDao::mapRow));
    }

    @Override
    public Optional<Chapter> getUnique(String uniqueKey) {
        return Optional.empty();
    }

    @Override
    public List<Chapter> getAll() {
        return null;
    }

    private static Chapter mapRow(ResultSet rs, int rowNum) throws SQLException {
        Chapter chapter = new Chapter();
        chapter.setChapterId(rs.getInt("chapter_id"));
        chapter.setBookId(rs.getInt("book_id"));
        chapter.setChapter_no(rs.getInt("chapter_no"));
        return chapter;
    }
}
