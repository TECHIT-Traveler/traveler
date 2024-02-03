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
import java.util.Optional;

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
    public RsData<Post> write(Member author, String title, String body, String area) {
        Post post = Post.builder()
                .modifyDate(LocalDateTime.now())
                .author(author)
                .title(title)
                .body(body)
                .area(area)
                .build();

        postRepository.save(post);

        return RsData.of("200", "%d번 게시글이 작성되었습니다.".formatted(post.getId()), post);
    }

    @Transactional
    public void like(Member member, Post post) {
        post.addLike(member);
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }
}
