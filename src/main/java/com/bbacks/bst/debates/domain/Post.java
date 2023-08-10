package com.bbacks.bst.debates.domain;

import com.bbacks.bst.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor
public class Post {
    @Id @GeneratedValue
    @Column(name = "p_id")
    private Long postId;

    @ManyToOne
    @JoinColumn(name="deb_id")
    private Debate debate;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private User user;

    @Column(name = "p_content")
    private String postContent;

    @Column(name = "p_photo")
    private String postPhoto;

    @Column(name = "quotation_id")
    private Long postQuotationId;

    @Column(name = "is_pro")
    private Integer postIsPro;

    @CreationTimestamp
    @Column(name = "created_date")
    private Date postCreatedAt;

    @Builder
    public Post(Debate debate, User user, String postContent, String postPhoto, Long postQuotationId, Integer postIsPro) {
        this.debate = debate;
        this.user = user;
        this.postContent = postContent;
        this.postPhoto = postPhoto;
        this.postQuotationId = postQuotationId;
        this.postIsPro = postIsPro;
    }
}
