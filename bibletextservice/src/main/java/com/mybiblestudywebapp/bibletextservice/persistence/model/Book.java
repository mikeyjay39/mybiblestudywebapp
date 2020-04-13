package com.mybiblestudywebapp.bibletextservice.persistence.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/29/20
 */
@Entity
@Table(name = "books", schema = "public",
uniqueConstraints = @UniqueConstraint(columnNames = "title"))
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true, includeFieldNames = false)
@Slf4j
@Accessors(chain = true)
public class Book {

    @Id
    @SequenceGenerator(name = "MY_ENTITY_SEQ", sequenceName = "MY_ENTITY_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "MY_ENTITY_SEQ" )
    @Column(name = "book_id")
    private long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    private String title;

    @ManyToOne
    @JoinColumn(name = "testament_id")
    private Testament testament;

    @OneToMany(mappedBy = "book", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @MapKeyColumn(name = "chapter_no")
    private Map<Integer, Chapter> chapters = new TreeMap<>();

    public Map<Integer, Chapter> addChapter(Chapter c) {
        chapters.put(c.getChapterNo(), c);
        return Map.copyOf(chapters);
    }


}
