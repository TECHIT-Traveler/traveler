package com.ll.traveler.domain.post.post.service;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.domain.post.post.repository.PostCommentRepository;
import com.ll.traveler.domain.post.post.repository.PostRepository;
import com.ll.traveler.domain.post.postComment.entity.PostComment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;

    public Page<Post> search(String kw, String criteria, Pageable pageable) {
        switch (criteria) {
            case "area":
                return postRepository.findByAreaContaining(kw, pageable);
            case "category":
                return postRepository.findByCategoriesContentContaining(kw, pageable);
            case "author":
                return postRepository.findByAuthorNicknameContaining(kw, pageable);
            default:
                return postRepository.findByTitleContaining(kw, pageable);
        }
    }

    @Transactional
    public Post write(Member author, String title, String body, String area) {
        Post post = Post.builder()
                .modifyDate(LocalDateTime.now())
                .author(author)
                .title(title)
                .body(body)
                .area(area)
                .build();

        return postRepository.save(post);
    }


    @Transactional
    public void like(Member actor, Post post) {
        post.addLike(actor);
    }

    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
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

    public boolean canModify(Member actor, Post post) {
        return actor.equals(post.getAuthor());
    }

    @Transactional
    public void modify(Post post, String title, String body, String area) {
        post.setTitle(title);
        post.setBody(body);
        post.setArea(area);
    }

}
