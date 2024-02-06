package com.ll.traveler.domain.post.post.service;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.domain.post.post.repository.PostRepository;
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
            case "subarea" :
                return postRepository.findByDistrictContaining(kw, pageable);
            case "category":
                return postRepository.findByCategoriesContentContaining(kw, pageable);
            case "author":
                return postRepository.findByAuthorNicknameContaining(kw, pageable);
            default:
                return postRepository.findByTitleContaining(kw, pageable);
        }
    }

    @Transactional
    public Post write(Member author, String title, String body, String area, String district) {
        Post post = Post.builder()
                .modifyDate(LocalDateTime.now())
                .author(author)
                .title(title)
                .body(body)
                .area(area)
                .district(district)
                .build();

        return postRepository.save(post);
    }


    @Transactional
    public void like(Member member, Post post) {
        post.addLike(member);
    }

    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

    public boolean canModify(Member actor, Post post) {
        return  actor.equals(post.getAuthor());
    }

    @Transactional
    public void modify(Post post, String title, String body, String area, String district) {
        post.setTitle(title);
        post.setBody(body);
        post.setArea(area);
        post.setDistrict(district);
    }

    public boolean canDelete(Member actor, Post post) {
        if ( actor.isAdmin() ) return true;

        return actor.equals(post.getAuthor());
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }
}
