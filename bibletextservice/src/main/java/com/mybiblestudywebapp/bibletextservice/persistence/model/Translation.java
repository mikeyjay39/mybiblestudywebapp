package com.mybiblestudywebapp.bibletextservice.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/29/20
 */
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true, includeFieldNames = false)
@Table(name = "translation",
uniqueConstraints = @UniqueConstraint(columnNames = "title"))
@Slf4j
@Accessors(chain = true)
public class Translation implements Serializable {

    @Id
    @SequenceGenerator(name = "MY_VERSION_SEQ", sequenceName = "MY_VERSION_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "MY_VERSION_SEQ" )
    private long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    private String title;

    @OneToMany(mappedBy = "translation", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<VerseText> verses;

    @Enumerated(EnumType.STRING)
    private Languages language;

    public enum Languages {
        EN, HU
    }
}
