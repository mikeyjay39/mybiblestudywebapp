package com.mybiblestudywebapp.bibletextservice.persistence;

import com.mybiblestudywebapp.bibletextservice.persistence.model.*;
import com.mybiblestudywebapp.bibletextservice.persistence.repository.BookRepository;
import com.mybiblestudywebapp.bibletextservice.persistence.repository.TestamentRepository;
import com.mybiblestudywebapp.bibletextservice.persistence.repository.VersionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * This class is used to initialize and populate the database with Bibles
 *
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/31/20
 */
@Configuration
@Slf4j
public class PersistenceConfig {

    @Autowired
    private TestamentRepository testamentRepository;

    @Autowired
    private VersionRepository versionRepository;

    @Autowired
    private BookRepository bookRepository;

    private String kjvBooks =
            "/home/michael/Projects/mybiblestudywebapp/bibletextservice/src/main/resources/Kjvbookscsv.csv";

    private String kjvText =
            "/home/michael/Projects/mybiblestudywebapp/bibletextservice/src/main/resources/Kjvtextcsv.csv";

    @PostConstruct
    public void init() {

        Version version = initVersionAndTestament();
        Map<Long, Book> bookMap = initBooks();
        initVerses(bookMap, version);
    }

    private Version initVersionAndTestament() {

        Testament ot = new Testament().setTestament(Testament.Testaments.OLD_TESTAMENT);
        Testament nt = new Testament().setTestament(Testament.Testaments.NEW_TESTAMENT);
        testamentRepository.save(ot);
        testamentRepository.save(nt);
        Version version = new Version().setTitle("King James").setLanguage(Version.Languages.EN);
        version = versionRepository.save(version);
        return version;
    }

    private Map<Long, Book> initBooks() {

        final Testament OT = testamentRepository.findTestamentByTestament(Testament.Testaments.OLD_TESTAMENT);
        final Testament NT = testamentRepository.findTestamentByTestament(Testament.Testaments.NEW_TESTAMENT);
        List<Book> books = new ArrayList<>();
        Path path = Path.of(kjvBooks);
        List<Book> finalBooks = books;

        try (
            Stream<String> stream = Files.lines(path))
        {
            stream.skip(1).forEach(s -> {
                        String[] values = s.split(",");
                        Book book = new Book()
                                .setTitle(values[1])
                                .setTestament("O".equals(values[2]) ? OT : NT);
                        finalBooks.add(book);
                    }
            );
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        books = (List<Book>)bookRepository.saveAll(finalBooks);
        Map<Long, Book> bookMap = new HashMap<>();

        for (Book b : books) {
            bookMap.put(b.getId(), b);
        }

        return bookMap;
    }

    private void initVerses(Map<Long, Book> bookMap, Version version) {

        LinkedHashMap<Chapter, Chapter> chapters = new LinkedHashMap<>();
        Path path = Path.of(kjvText);

        try (Stream<String> stream = Files.lines(path)) {
            stream.skip(1).forEach(s -> {

                String[] values = s.split(",");
                Book book = bookMap.get(Long.valueOf(values[1]));
                Chapter chapterKey = new Chapter().setChapterNo(Integer.valueOf(values[2])).setBook(book);
                Chapter chapter = chapters.getOrDefault(chapterKey, chapterKey);
                String text = parseVerse(Arrays.copyOfRange(values, 4, values.length));
                VerseText verseText = new VerseText().setText(text).setVersion(version);
                Verse verse = new Verse()
                        .setVerseNo(Integer.valueOf(values[3]))
                        .setChapter(chapter)
                        .addVerseText(verseText);
                chapter.addVerse(verse);
                chapters.put(chapter, chapter);
                book.addChapter(chapter);
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        List<Book> books = (List<Book>)bookRepository.saveAll(bookMap.values());
        Book genesis = books.get(0);
        Chapter g1 = genesis.getChapters().get(0);
    }

    private String parseVerse(String[] input) {

        StringBuilder builder = new StringBuilder();

        for (String s : input) {
            builder.append(s);
            builder.append(",");
        }

        builder.deleteCharAt(builder.length() -1);
        return builder.toString();
    }
}
