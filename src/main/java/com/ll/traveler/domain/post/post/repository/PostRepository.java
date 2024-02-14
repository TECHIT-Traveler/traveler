package com.ll.traveler.domain.post.post.repository;

import com.ll.traveler.domain.post.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContaining(String kw, Pageable pageable);
    Page<Post> findByAreaContaining(String kw, Pageable pageable);
    Page<Post> findByCategoriesContentContaining(String kw, Pageable pageable);
    Page<Post> findByAuthorNicknameContaining(String kw, Pageable pageable);
    List<Post> findByAuthorId(Long id);
}
