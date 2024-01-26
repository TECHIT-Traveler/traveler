package com.ll.traveler.domain.post.post.controller;

import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.domain.post.post.service.PostService;
import com.ll.traveler.global.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/list")
    public String list(
            Model model,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "title") String criteria,
            @RequestParam(defaultValue = "1") int page
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, 8, Sort.by(sorts));

        Page<Post> postPage = postService.search(kw, criteria, pageable);
        model.addAttribute("postPage", postPage);
        model.addAttribute("page", page);
        model.addAttribute("kw", kw);
        model.addAttribute("criteria", criteria);

        return "domain/post/post/list";
    }

    @Getter
    @Setter
    public static class WriteForm {
        @NotBlank
        private String title;
        @NotBlank
        private String body;
        @NotBlank
        private String area;
    }

    @GetMapping("/detail/{postId}")
    public String goDetail(@PathVariable Long postId, Model model){
        Post post = postService.findById(postId);
        model.addAttribute("post", post);

        return "domain/post/post/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite() {
        return "domain/post/post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@Valid WriteForm form) {
        Post post = postService.write(rq.getMember(), form.getTitle(), form.getBody(), form.getArea());

        return rq.redirect("/post/detail/" + post.getId(), post.getId() + "번 글이 작성되었습니다.");
    }
}
