package com.mybiblestudywebapp.bibletextservice;

import com.mybiblestudywebapp.bibletextservice.persistence.model.*;
import com.mybiblestudywebapp.bibletextservice.persistence.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailParseException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
public class BibleTextServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibleTextServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(
            VerseRepository verseRepository,
            TestamentRepository testamentRepository,
            VersionRepository versionRepository,
            BookRepository bookRepository,
            ChapterRepository chapterRepository) {
        return (args) -> {
            Testament ot = new Testament().setTestament(Testament.Testaments.OLD_TESTAMENT);
            Testament nt = new Testament().setTestament(Testament.Testaments.NEW_TESTAMENT);
            testamentRepository.save(ot);
            testamentRepository.save(nt);
            final Testament OT = testamentRepository.findTestamentByTestament(Testament.Testaments.OLD_TESTAMENT);
            final Testament NT = testamentRepository.findTestamentByTestament(Testament.Testaments.NEW_TESTAMENT);

            Version version = new Version().setTitle("King James").setLanguage(Version.Languages.EN);
            versionRepository.save(version);

            List<Book> books = new ArrayList<>();
            Path path = Path.of("/home/michael/Projects/mybiblestudywebapp/bibletextservice/src/main/resources/Kjvbookscsv.csv");
            List<Book> finalBooks = books;
            Files.lines(path).skip(1).forEach(s -> {
                String[] values = s.split(",");
                Book book = new Book()
                        .setTitle(values[1])
                        .setTestament("O".equals(values[2]) ? OT : NT);
                finalBooks.add(book);

                    }
                    );
            books = (List<Book>)bookRepository.saveAll(finalBooks);
            //books = (List<Book>) bookRepository.findAll();
            Map<Long, Book> bookMap = new HashMap<>();

            for (Book b : books) {
                bookMap.put(b.getId(), b);
            }

            LinkedHashMap<Chapter, Chapter> chapters = new LinkedHashMap<>();
            LinkedHashSet<Verse> verses = new LinkedHashSet<>();

            path = Path.of("/home/michael/Projects/mybiblestudywebapp/bibletextservice/src/main/resources/Kjvtextcsv.csv");
            Files.lines(path).skip(1).forEach(s -> {
                String[] values = s.split(",");
                //Book book = bookRepository.findById(Long.valueOf(values[1])).get();
                Book book = bookMap.get(Long.valueOf(values[1]));
                Chapter chapterKey = new Chapter().setChapterNo(Integer.valueOf(values[2])).setBook(book);
                Chapter chapter = chapters.getOrDefault(chapterKey, chapterKey);
                //book.addChapter(chapter);
                //saveChapter(chapterRepository, chapter);
                //chapter = chapterRepository.findByBookAndAndChapterNo(book, Integer.valueOf(values[2]));
                Verse verse = new Verse()
                        .setVerseNo(Integer.valueOf(values[3]))
                        .setChapter(chapter)
                        .setText(values[4])
                        .setVersion(version);
                //verses.add(verse);
                chapter.addVerse(verse);
                chapters.put(chapter, chapter);
                book.addChapter(chapter);
                //verseRepository.save(verse);
            });

            //List<Book> savedBooks = (List<Book>)bookRepository.saveAll(bookMap.values());
            //List<Chapter> savedChapters = (List<Chapter>)chapterRepository.saveAll(chapters.values());

            /*for (Book b : savedBooks) {
                bookMap.put(b.getId(), b);
            }

            Map<Long, Chapter> chapterMap = new HashMap<>();

            for (Chapter c : savedChapters) {

                for (Verse v : c.getVerses().values()) {
                    v.setChapter(c);
                    verses.add(v);
                }

                chapterMap.put(c.getId(), c);
                bookMap.get(c.getBook().getId()).addChapter(c);
            }*/


            books = (List<Book>)bookRepository.saveAll(bookMap.values());
            //chapterRepository.saveAll(chapterMap.values());

            //books = (List<Book>)bookRepository.findAll();
            Book genesis = books.get(0);
            Chapter g1 = genesis.getChapters().get(0);

            //chapterRepository.saveAll(chapters);
        };
    }

    private void saveChapter(ChapterRepository repository, Chapter chapter) {
        try {
            repository.save(chapter);
        } catch (Exception e){}
    }
}
