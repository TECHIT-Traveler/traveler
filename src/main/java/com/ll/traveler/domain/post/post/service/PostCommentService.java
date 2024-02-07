package com.ll.traveler.domain.post.post.service;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.domain.post.post.repository.PostCommentRepository;
import com.ll.traveler.domain.post.postComment.entity.PostComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;

    public void create(Post post, String body, Member author) {
        PostComment postComment = new PostComment();
        postComment.setBody(body);
        postComment.setModifyDate(LocalDateTime.now());
        postComment.setPost(post);
        postComment.setAuthor(author);
        this.postCommentRepository.save(postComment);
    }
}
