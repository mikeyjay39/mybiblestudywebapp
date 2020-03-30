package com.mybiblestudywebapp.bibletextservice;

import com.mybiblestudywebapp.bibletextservice.persistence.model.*;
import com.mybiblestudywebapp.bibletextservice.persistence.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

import java.nio.file.Files;
import java.nio.file.Path;

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


            Path path = Path.of("/home/michael/Projects/mybiblestudywebapp/bibletextservice/src/main/resources/Kjvbookscsv.csv");
            Files.lines(path).skip(1).forEach(s -> {
                String[] values = s.split(",");
                Book book = new Book()
                        .setTitle(values[1])
                        .setTestament("O".equals(values[2]) ? OT : NT);
                        bookRepository.save(book);
               // System.out.println(s);
                    }
                    );
            path = Path.of("/home/michael/Projects/mybiblestudywebapp/bibletextservice/src/main/resources/Kjvtextcsv.csv");
            Files.lines(path).skip(1).forEach(s -> {
                String[] values = s.split(",");
                Book book = bookRepository.findById(Long.valueOf(values[1])).get();
                Chapter chapter = new Chapter().setChapterNo(Integer.valueOf(values[2])).setBook(book);
                saveChapter(chapterRepository, chapter);
                chapter = chapterRepository.findByBookAndAndChapterNo(book, Integer.valueOf(values[2]));
                Verse verse = new Verse()
                        .setVerseNo(Integer.valueOf(values[3]))
                        .setChapter(chapter)
                        .setText(values[4])
                        .setVersion(version);
                verseRepository.save(verse);



            });

        };
    }

    private void saveChapter(ChapterRepository repository, Chapter chapter) {
        try {
            repository.save(chapter);
        } catch (Exception e){}
    }
}
