package com.ll.traveler.domain.post.PostCategory.entity;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.global.jpa.IdEntity;
import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
public class PostCategory extends IdEntity {
    private String content;

    @ManyToOne
    private Member author;

    @ManyToOne
    private Post post;
}
