package com.bbacks.bst.reviews.domain;

import com.bbacks.bst.books.domain.Book;
import com.bbacks.bst.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "review")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_id")
    private Book book;

    @Column(name = "r_title")
    private String reviewTitle;

    @Column(name = "r_picture")
    private String reviewImg;

    @Column(name = "r_content")
    private String reviewContent;

    @Column(name = "r_spoiler", columnDefinition = "TINYINT(1)")
    private Boolean reviewSpoiler = false;

    @Column(name = "r_private", columnDefinition = "TINYINT(1)")
    private Boolean reviewPrivate = false;

    @CreatedDate
    @Column(name = "r_created_date", updatable = false)
    private Date reviewCreatedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewComment> reviewComment = new ArrayList<>();

    public void setReviewImg(String filePath) {
        this.reviewImg = filePath;
    }

}
