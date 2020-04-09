package com.mybiblestudywebapp.bibletextservice.service;

import com.mybiblestudywebapp.bibletextservice.persistence.model.*;
import com.mybiblestudywebapp.bibletextservice.persistence.repository.BookRepository;
import com.mybiblestudywebapp.bibletextservice.persistence.repository.TestamentRepository;
import com.mybiblestudywebapp.bibletextservice.persistence.repository.TranslationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 4/8/20
 */
@Service
@Slf4j
public class AddBible {

    @Autowired
    private TestamentRepository testamentRepository;

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private BookRepository bookRepository;

    private Resource kjvBooksR = new ClassPathResource("Kjvbookscsv.csv");

    private String kjvBooks = "Kjvbookscsv.csv";

    private Resource kjvTextR = new ClassPathResource("Kjvtextcsv.csv");

    private String kjvText = "Kjvtextcsv.csv";

    /**
     * Use this method to parse Bible input files and persist them
     */
    public void init() {
        log.debug("init() trying to open Bible resource files");
        try {
            kjvBooks = kjvBooksR.getFile().getAbsolutePath();
            kjvText = kjvTextR.getFile().getAbsolutePath();
            log.debug("Opening bible csv files:\n{}\n{}",
                    kjvBooks,
                    kjvText);

        } catch (IOException e) {
            log.error("Could not open {} or {}\n{}",
                    kjvBooksR.toString(), kjvTextR.toString(),e.getMessage());
        }


        Translation translation = initVersionAndTestament();
        Map<Long, Book> bookMap = initBooks();
        initVerses(bookMap, translation);
    }

    private Translation initVersionAndTestament() {

        Testament ot = new Testament().setTestament(Testament.Testaments.OLD_TESTAMENT);
        Testament nt = new Testament().setTestament(Testament.Testaments.NEW_TESTAMENT);
        testamentRepository.save(ot);
        testamentRepository.save(nt);
        Translation translation = new Translation().setTitle("King James").setLanguage(Translation.Languages.EN);
        translation = translationRepository.save(translation);
        return translation;
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
            log.debug("{} found and parsing", kjvBooks);
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

    private void initVerses(Map<Long, Book> bookMap, Translation translation) {

        LinkedHashMap<Chapter, Chapter> chapters = new LinkedHashMap<>();
        Path path = Path.of(kjvText);

        try (Stream<String> stream = Files.lines(path)) {
            log.debug("{} found and parsing", kjvText);

            stream.skip(1).forEach(s -> {

                String[] values = s.split(",");
                Book book = bookMap.get(Long.valueOf(values[1]));
                Chapter chapterKey = new Chapter().setChapterNo(Integer.valueOf(values[2])).setBook(book);
                Chapter chapter = chapters.getOrDefault(chapterKey, chapterKey);
                String text = parseVerse(Arrays.copyOfRange(values, 4, values.length));
                VerseText verseText = new VerseText().setText(text).setTranslation(translation);
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
        log.debug("done persisting {}", translation.toString());
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
