package com.bbacks.bst.debates.domain;

import com.bbacks.bst.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Table(name = "post")
public class Post {
    @Id @GeneratedValue
    @Column(name = "p_id")
    private Long postId;

    @ManyToOne
    @JoinColumn(name="deb_id")
    private Debate debateId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "p_content")
    private String postContent;

    @Column(name = "p_photo")
    private String postPhoto;

    @Column(name = "quotation_id")
    private Long postQuotationId;

    @Column(name = "p_like")
    private Integer postLike;

    @Column(name = "p_dislike")
    private Integer postDislike;

    @Column(name = "is_pro")
    private Boolean postIsPro;

    @CreationTimestamp
    @Column(name = "created_date")
    private Date postCreatedAt;

}
