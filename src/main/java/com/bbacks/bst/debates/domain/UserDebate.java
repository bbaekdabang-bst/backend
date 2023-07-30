package com.bbacks.bst.debates.domain;

import com.bbacks.bst.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "user_debate")
public class UserDebate {

    @Id @GeneratedValue
    @Column(name = "user_debate_id")
    private Long userDebateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deb_id")
    private Debate debateId;
}
