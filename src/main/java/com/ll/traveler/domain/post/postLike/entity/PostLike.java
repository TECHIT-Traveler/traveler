package com.ll.traveler.domain.post.postLike.entity;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.global.jpa.IdEntity;
import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Getter
public class PostLike extends IdEntity {
    @ManyToOne
    private Member member;

    @ManyToOne
    private Post post;
}
