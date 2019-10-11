package com.mybiblestudywebapp.unittests;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.w3c.dom.CDATASection;

import javax.sql.DataSource;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 11.
 */
public class DbConnectionTest {

    private static DataSource dataSource;

    @BeforeClass
    public static void beforeClass() throws Exception {
        dataSource = buildEmbeddedDataBase();
    }


    @Test
    public void testEmbeddedPg() throws Exception
    {
        try (EmbeddedPostgres pg = EmbeddedPostgres.start();
             Connection connection = pg.getPostgresDatabase().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT 1");
            Assert.assertTrue(rs.next());
            Assert.assertEquals(1, rs.getInt(1));
            Assert.assertFalse(rs.next());
        } catch (Exception e) {
            System.out.print(e);
            Assert.fail();
        }
    }

    /**
     * See if we were able to import the schema into the embedded DB
     */
    @Test
    public void testEmbeddedPgBuilt() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM book;");
            Assert.assertTrue(rs.next());
            Assert.assertEquals("Genesis", rs.getString("title"));
        } catch (Exception e) {
            System.out.print(e);
            Assert.fail();
        }
    }

    private static DataSource buildEmbeddedDataBase() throws Exception {
        EmbeddedPostgres pg = EmbeddedPostgres.start();
        DataSource dataSource = pg.getPostgresDatabase();

        try (
                Connection connection = dataSource.getConnection()
        ) {

            ScriptUtils.executeSqlScript(connection,
                    new EncodedResource(
                            new FileSystemResource(
                                    Paths.get(
                                            "/home/developer/my-bible-study-web-app/sql/schema.sql"))));
        }

        try (
                Connection connection = dataSource.getConnection()
                ) {

            ScriptUtils.executeSqlScript(connection,
                    new EncodedResource(
                            new FileSystemResource(
                                    Paths.get(
                                            "/home/developer/my-bible-study-web-app/sql/backups/mybiblestudydb.sql"))));
        }
        return dataSource;
    }
}


