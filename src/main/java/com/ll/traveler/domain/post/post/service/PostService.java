package com.ll.traveler.domain.post.post.service;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.domain.post.post.repository.PostRepository;
import com.ll.traveler.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

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
    public void like(Member member, Post post) {
        post.addLike(member);
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId).get();
    }
}
