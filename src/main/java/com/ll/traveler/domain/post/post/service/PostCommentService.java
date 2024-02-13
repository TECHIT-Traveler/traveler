package com.ll.traveler.domain.post.post.service;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.domain.post.post.repository.PostCommentRepository;
import com.ll.traveler.domain.post.postComment.entity.PostComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Transactional
    public PostComment writeComment(Member actor, Post post, String body) {
        return post.writeComment(actor, body);
    }

    public boolean canModifyComment(Member actor, PostComment comment) {
        if (actor == null) return false;

        return actor.equals(comment.getAuthor());
    }

    public boolean canDeleteComment(Member actor, PostComment comment) {
        if (actor == null) return false;

        if (actor.isAdmin()) return true;

        return actor.equals(comment.getAuthor());
    }

    public Optional<PostComment> findCommentById(long id) {
        return postCommentRepository.findCommentById(id);
    }

    @Transactional
    public void modifyComment(PostComment postComment, String body) {
        postComment.setBody(body);
    }

    @Transactional
    public void deleteComment(PostComment postComment) {
        postCommentRepository.delete(postComment);
    }
}
