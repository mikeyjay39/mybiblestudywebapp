package com.mybiblestudywebapp.bibletextservice.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/29/20
 */
@Entity
@Data
@Table(name = "verse",
uniqueConstraints = @UniqueConstraint(columnNames = {"version_id", "chapter_id", "verse_no"}))
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Accessors(chain = true)
@Slf4j
public class Verse {

    @Id
    @ToString.Exclude
    @SequenceGenerator(name = "MY_VERSE_SEQ", sequenceName = "MY_VERSE_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_VERSE_SEQ" )
    @Column(name = "verse_id")
    private long id;

    @ToString.Exclude
    @Column(columnDefinition="TEXT")
    private String text;

    @EqualsAndHashCode.Include
    @Column(name = "verse_no")
    private int verseNo;

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "version_id")
    private Version version;
}
