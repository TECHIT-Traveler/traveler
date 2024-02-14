package com.ll.traveler.domain.post.post.controller;

import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.domain.post.post.service.PostService;
import com.ll.traveler.global.exceptions.GlobalException;
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

    //글작성 폼
    @Getter
    @Setter
    public static class WriteForm {
        @NotBlank
        private String title;
        @NotBlank
        private String body;
        @NotBlank
        private String area;
        @NotBlank
        private String district;
    }

    //디테일 페이지
    @GetMapping("detail/{id}")
    public String showPost(@PathVariable long id) {
        rq.setAttribute("post", postService.findById(id).get());

        return "domain/post/post/detail";
    }

    //글작성 GET
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite() {
        return "domain/post/post/write";
    }

    //글작성 POST
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@Valid WriteForm form) {
        Post post = postService.write(rq.getMember(), form.getTitle(), form.getBody(), form.getArea(), form.getDistrict());

        return rq.redirect("/post/detail/" + post.getId(), post.getId() + "번 글이 작성되었습니다.");
    }

    //글수정 GET
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable long id, Model model) {
        Post post = postService.findById(id).orElseThrow(() -> new GlobalException("404-1", "해당 글이 존재하지 않습니다."));

        if (!postService.canModify(rq.getMember(), post)) throw new GlobalException("403-1", "권한이 없습니다.");

        model.addAttribute("post", post);

        return "domain/post/post/modify";
    }

    //수정폼
    @Getter
    @Setter
    public static class ModifyForm {
        @NotBlank
        private String title;
        @NotBlank
        private String body;
        @NotBlank
        private String area;
        @NotBlank
        private String district;
    }

    //글수정 PUT
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/modify")
    public String modify(@PathVariable long id ,@Valid ModifyForm form) {
        Post post = postService.findById(id).orElseThrow(() -> new GlobalException("404-1", "해당 글이 존재하지 않습니다."));

        if (!postService.canModify(rq.getMember(), post)) throw new GlobalException("403-1", "권한이 없습니다.");

        postService.modify(post, form.getTitle(), form.getBody(), form.getArea(), form.getDistrict());

        return rq.redirect("/post/detail/" + post.getId(), post.getId() + "번 글이 수정되었습니다.");
    }

    //글삭제
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable long id) {
        Post post = postService.findById(id).orElseThrow(() -> new GlobalException("404-1", "해당 글이 존재하지 않습니다."));

        if (!postService.canDelete(rq.getMember(), post)) throw new GlobalException("403-1", "권한이 없습니다.");

        postService.delete(post);

        return "redirect:/post/list";
    }
}
