package com.ll.traveler.domain.post.post.controller;

import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

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
}
