package com.mybiblestudywebapp.bibletextservice.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/29/20
 */
@Entity
@Table(name = "chapters", schema = "public", uniqueConstraints={@UniqueConstraint(columnNames={"chapter_no", "book_id"})})
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Accessors(chain = true)
@Slf4j
public class Chapter {

    @Id
    @SequenceGenerator(name = "MY_CHAPTER_SEQ", sequenceName = "MY_CHAPTER_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_CHAPTER_SEQ" )
    @Column(name = "chapter_id")
    private long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "chapter_no")
    private int chapterNo;

    @OneToMany(mappedBy = "chapter", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @MapKeyColumn(name = "verse_no")
    private Map<Integer, Verse> verses = new TreeMap<>();

    public Map<Integer, Verse> addVerse(Verse verse) {
        verses.put(verse.getVerseNo(), verse);
        return Map.copyOf(verses);
    }
}
