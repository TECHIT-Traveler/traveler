package com.ll.traveler.domain.member.member.entity;

import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.domain.post.postComment.entity.PostComment;
import com.ll.traveler.domain.post.postLike.entity.PostLike;
import com.ll.traveler.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@SuperBuilder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Member extends BaseEntity {
    private String email;
    private String password;
    private String nickname;
}
