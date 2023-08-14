package com.bbacks.bst.debates.domain;

import com.bbacks.bst.books.domain.Book;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Table(name = "debate")
@NoArgsConstructor
public class Debate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "deb_participants")
    private Integer debateParticipants;

    @CreationTimestamp
    @Column(name = "deb_created_date")
    private Date debateCreatedAt;

    @OneToMany(mappedBy = "debate", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Debate(Book bookId, String debateTopic, Integer debateType, String debatePassword, String debateDescription, Integer debateParticipants) {
        this.bookId = bookId;
        this.debateTopic = debateTopic;
        this.debateType = debateType;
        this.debatePassword = debatePassword;
        this.debateDescription = debateDescription;
        this.debateParticipants = debateParticipants;
    }

}
