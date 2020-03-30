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
@Data
@Table(name = "version")
@Slf4j
@Accessors(chain = true)
public class Version {

    @Id
    @SequenceGenerator(name = "MY_VERSION_SEQ", sequenceName = "MY_VERSION_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_VERSION_SEQ" )
    @Column(name = "version_id")
    private long id;

    private String title;

    @OneToMany
    private Set<Verse> verses;

    @Enumerated(EnumType.STRING)
    private Languages language;

    public enum Languages {
        EN, HU
    }
}
