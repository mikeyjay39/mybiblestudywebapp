package com.mybiblestudywebapp.persistence;

import com.sun.source.tree.Tree;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

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

    /**
     * Use this method to insert all the chapters needed into the data base.
     * @param args Key = book_id. Value = number of chapters in the book.
     * @return
     */
    public long insertAllChapters(TreeMap<Integer, Integer> args) {
        long totalRows = 0;
        for (int i = 0; i < args.size(); i++) {
            int book_id = i + 1;
            int chapters = args.get(book_id);
            for (int j = 0; j < chapters; j ++) {
                int chapter_no = j + 1;
                String sql = "INSERT INTO chapters (book_id, chapter_no) " +
                        "VALUES (?, ?)";
                jdbcTemplate.update(sql, book_id, chapter_no);
                totalRows++;
            }
        }
        return totalRows;
    }

    private static Chapter mapRow(ResultSet rs, int rowNum) throws SQLException {
        Chapter chapter = new Chapter();
        chapter.setChapterId(rs.getInt("chapter_id"));
        chapter.setBookId(rs.getInt("book_id"));
        chapter.setChapter_no(rs.getInt("chapter_no"));
        return chapter;
    }
}
