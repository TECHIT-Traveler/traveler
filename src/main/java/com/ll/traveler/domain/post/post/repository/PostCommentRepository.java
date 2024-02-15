package com.ll.traveler.domain.post.post.repository;


import com.ll.traveler.domain.post.postComment.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    Optional<PostComment> findCommentById(long id);
    List<PostComment> findByAuthorId(Long memberId);
}
