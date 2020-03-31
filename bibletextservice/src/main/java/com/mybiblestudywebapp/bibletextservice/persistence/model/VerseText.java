package com.mybiblestudywebapp.bibletextservice.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/31/20
 */
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "verse_text", uniqueConstraints = @UniqueConstraint(columnNames = {"verse_id", "version"}))
@ToString(onlyExplicitlyIncluded = true, includeFieldNames = true)
@Accessors(chain = true)
public class VerseText {

    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "VERSE_TEXT_SEQ", sequenceName = "VERSE_TEXT_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "VERSE_TEXT_SEQ" )
    @Column(name = "verse_text_id")
    private long id;

    @ToString.Include
    @ManyToOne
    @JoinColumn(name = "verse_id")
    private Verse verse;

    @Column(columnDefinition="TEXT")
    @ToString.Include
    private String text;

    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "version", referencedColumnName = "title")
    private Version version;

}
