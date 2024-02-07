package com.ll.traveler.domain.post.post.entity;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.post.PostCategory.entity.PostCategory;
import com.ll.traveler.domain.post.postComment.entity.PostComment;
import com.ll.traveler.domain.post.postLike.entity.PostLike;
import com.ll.traveler.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;

@Entity
@SuperBuilder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Post extends BaseEntity {
    private String title;
    @Column(columnDefinition = "TEXT")
    private String body;
    private String area;

    @ManyToOne
    private Member author;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostCategory> categories = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    @OrderBy("id DESC")
    private List<PostComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostLike> likes = new ArrayList<>();

    public void addLike(Member member) {
        if(hasLike(member)) {
            return;
        }
        likes.add(PostLike.builder()
                .post(this)
                .member(member)
                .build());
    }

    public boolean hasLike(Member member) {
        return likes.stream()
                .anyMatch(postLike -> postLike.getMember().equals(member));
    }

    public PostComment writeComment(Member actor, String body) {
        PostComment postComment = PostComment.builder()
                .post(this)
                .author(actor)
                .body(body)
                .build();

        comments.add(postComment);

        return postComment;
    }
}
