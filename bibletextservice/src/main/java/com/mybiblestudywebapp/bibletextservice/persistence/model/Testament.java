package com.mybiblestudywebapp.bibletextservice.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true, includeFieldNames = false)
@Table(name = "testament",
uniqueConstraints = @UniqueConstraint(columnNames = "testament"))
@Slf4j
@Accessors(chain = true)
public class Testament {

    @Id
    @SequenceGenerator(name = "MY_TESTAMENT_SEQ", sequenceName = "MY_TESTAMENT_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "MY_TESTAMENT_SEQ" )
    @Column(name = "testament_id")
    private long id;

    @OneToMany(mappedBy = "testament", fetch = FetchType.LAZY)
    private Set<Book> books;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Enumerated(EnumType.STRING)
    private Testaments testament;

    public enum Testaments {
        OLD_TESTAMENT, NEW_TESTAMENT;
    }
}
