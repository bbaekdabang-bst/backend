package com.bbacks.bst.debates.domain;

import com.bbacks.bst.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name="bookmark_post")
public class PostBookmark {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bm_p_id")
    private Long bookmarkId;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "p_id")
    private Post post;

    @Builder
    public PostBookmark(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
