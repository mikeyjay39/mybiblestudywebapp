package com.mybiblestudywebapp.bibletextservice.persistence.model;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/29/20
 */
@Entity
@Table(name = "chapters", uniqueConstraints={@UniqueConstraint(columnNames={"chapter_no", "book_book_id"})})
@Data
@Accessors(chain = true)
@Slf4j
public class Chapter {

    @Id
    @SequenceGenerator(name = "MY_CHAPTER_SEQ", sequenceName = "MY_CHAPTER_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_CHAPTER_SEQ" )
    @Column(name = "chapter_id")
    private long id;

    @Column(name = "chapter_no")
    private int chapterNo;

    @ManyToOne
    private Book book;

    @OneToMany
    private Set<Verse> verses;
}
