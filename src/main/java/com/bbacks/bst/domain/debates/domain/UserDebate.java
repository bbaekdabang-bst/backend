package com.bbacks.bst.domain.debates.domain;

import com.bbacks.bst.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_debate")
@NoArgsConstructor
public class UserDebate {

    @Id @GeneratedValue
    @Column(name = "user_debate_id")
    private Long userDebateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deb_id")
    private Debate debate;

    @Builder
    public UserDebate(User user, Debate debate) {
        this.user = user;
        this.debate = debate;
    }
}
