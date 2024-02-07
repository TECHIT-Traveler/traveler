package com.ll.traveler.domain.post.postComment.entity;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity
@SuperBuilder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PUBLIC)
@Getter
@Setter
@ToString(callSuper = true)
public class PostComment extends BaseEntity {
    @ManyToOne
    private Member author;
    @ManyToOne
    private Post post;
    @Column(columnDefinition = "TEXT")
    private String body;
}