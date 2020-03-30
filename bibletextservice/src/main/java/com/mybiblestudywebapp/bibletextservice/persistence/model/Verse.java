package com.mybiblestudywebapp.bibletextservice.persistence.model;

import lombok.Data;
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
@Table(name = "verse")
@Accessors(chain = true)
@Slf4j
public class Verse {

    @Id
    @SequenceGenerator(name = "MY_VERSE_SEQ", sequenceName = "MY_VERSE_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_VERSE_SEQ" )
    @Column(name = "verse_id")
    private long id;

    @Column(columnDefinition="TEXT")
    private String text;

    @Column(name = "verse_no")
    private int verseNo;

    @ManyToOne
    private Chapter chapter;


    @ManyToOne
    private Version version;
}
