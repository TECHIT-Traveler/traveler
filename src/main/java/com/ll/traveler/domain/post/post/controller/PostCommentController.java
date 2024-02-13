package com.ll.traveler.domain.post.post.controller;

import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.domain.post.post.service.PostCommentService;
import com.ll.traveler.domain.post.post.service.PostService;
import com.ll.traveler.domain.post.postComment.entity.PostComment;
import com.ll.traveler.global.exceptions.GlobalException;
import com.ll.traveler.global.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post/{id}/comment")
public class PostCommentController {
    private final PostService postService;
    private final PostCommentService postCommentService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{commentId}/modify")
    public String showModify(
            @PathVariable long id,
            @PathVariable long commentId
    ) {
        Post post = postService.findById(id).orElseThrow(() -> new GlobalException("404-1", "해당 글이 존재하지 않습니다."));
        PostComment postComment = postCommentService.findCommentById(commentId).orElseThrow(() -> new GlobalException("404-1", "해당 댓글이 존재하지 않습니다."));

        if (!postCommentService.canModifyComment(rq.getMember(), postComment))
            throw new GlobalException("403-1", "해당 댓글을 수정할 권한이 없습니다.");

        rq.setAttribute("post", post);
        rq.setAttribute("postComment", postComment);

        return "domain/post/postComment/modify";
    }

    @Getter
    @Setter
    public static class ModifyForm {
        @NotBlank
        private String body;
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{commentId}/modify")
    public String modify(
            @PathVariable long id,
            @PathVariable long commentId,
            @Valid ModifyForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return rq.redirect("/post/" + id, "댓글을 입력해주세요.");
        }
        PostComment postComment = postCommentService.findCommentById(commentId).orElseThrow(() -> new GlobalException("404-1", "해당 댓글이 존재하지 않습니다."));

        if (!postCommentService.canModifyComment(rq.getMember(), postComment))
            throw new GlobalException("403-1", "해당 댓글을 수정할 권한이 없습니다.");

        postCommentService.modifyComment(postComment, form.getBody());

        return rq.redirect("/post/detail/" + id + "#postComment-" + postComment.getId(), "댓글이 수정되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{commentId}/delete")
    public String delete(
            @PathVariable long id,
            @PathVariable long commentId
    ) {
        PostComment postComment = postCommentService.findCommentById(commentId).orElseThrow(() -> new GlobalException("404-1", "해당 댓글이 존재하지 않습니다."));

        if (!postCommentService.canDeleteComment(rq.getMember(), postComment))
            throw new GlobalException("403-1", "해당 댓글을 수정할 권한이 없습니다.");

        postCommentService.deleteComment(postComment);

        return rq.redirect("/post/detail/" + id, commentId + "번 댓글이 삭제되었습니다.");
    }

    @PostMapping("/create")
    public String createComment(@PathVariable("id") long id, @Valid CreateCommentForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return rq.redirect("/post/detail/" + id, "댓글을 입력해주세요.");
        }
        Post post = postService.findById(id).orElseThrow(() -> new GlobalException("404-1", "해당 글이 존재하지 않습니다."));
        this.postCommentService.create(post, form.body, rq.getMember());
        return String.format("redirect:/post/detail/%s", id);
    }

    @Getter
    @Setter
    public static class CreateCommentForm {

        @NotBlank
        private String body;
    }
}