package com.mybiblestudywebapp.bibletextservice.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/29/20
 */
@Entity
@Data
@Table(name = "verse",
uniqueConstraints = @UniqueConstraint(columnNames = {"chapter_id", "verse_no"}))
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Accessors(chain = true)
@Slf4j
public class Verse {

    @Id
    @SequenceGenerator(name = "MY_VERSE_SEQ", sequenceName = "MY_VERSE_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_VERSE_SEQ" )
    @Column(name = "verse_id")
    private long id;

    @OneToMany(mappedBy = "verse", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapKeyColumn
    private Map<Translation, VerseText> text = new HashMap<>();

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "verse_no")
    private int verseNo;

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    public Verse addVerseText(VerseText verseText) {
        verseText.setVerse(this);
        text.put(verseText.getTranslation(), verseText);
        return this;
    }
}
