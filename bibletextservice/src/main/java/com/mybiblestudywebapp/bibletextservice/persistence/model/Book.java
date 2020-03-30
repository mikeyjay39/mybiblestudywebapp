package com.mybiblestudywebapp.bibletextservice.persistence.model;

import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
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
@Table(name = "books")
@Data
@Slf4j
@Accessors(chain = true)
public class Book {

    @Id
    @SequenceGenerator(name = "MY_ENTITY_SEQ", sequenceName = "MY_ENTITY_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_ENTITY_SEQ" )
    @Column(name = "book_id")
    private long id;

    private String title;

    @ManyToOne
    private Testament testament;

    @OneToMany
    private Set<Chapter> chapters;


}
