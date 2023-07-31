package com.bbacks.bst.debates.domain;

import com.bbacks.bst.books.domain.Book;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
@Table(name = "debate")
public class Debate {
    @Id @GeneratedValue
    @Column(name = "deb_id")
    private Long debateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_id")
    private Book book;

    @Column(name = "deb_topic")
    private String debateTopic;

    @Column(name = "deb_type")
    @Enumerated(EnumType.ORDINAL)
    private DebateType debateType;

    @Column(name = "deb_pw")
    private String debatePassword;

    @Column(name = "deb_description")
    private String debateDescription;

    @Column(name = "deb_posts")
    private Long debatePostCount;

    @Column(name = "deb_created_date")
    private Date debateCreatedDate;

}
