package com.bbacks.bst.domain.reviews.domain;

import com.bbacks.bst.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "review_comment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewComment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "r_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private User user;

    @Column(name = "comment_text")
    private String commentText;

}
