package com.ll.traveler.domain.member.myPage.controller;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.myPage.dto.MyPageUpdateForm;
import com.ll.traveler.domain.member.myPage.service.MyPageService;
import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.domain.post.post.service.PostService;
import com.ll.traveler.domain.travel.travelRoute.entity.TravelRoute;
import com.ll.traveler.domain.travel.travelRoute.service.TravelRouteService;
import com.ll.traveler.global.rq.Rq;
import com.ll.traveler.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/my-page")
@RequiredArgsConstructor // 생성자 주입
public class MyPageController {
    private final MyPageService myPageService;
    private final TravelRouteService travelRouteService;
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/{id}")
    public String myPageMain(@PathVariable("id") Long id, Model model) {
        Member member = myPageService.getMemberInfo(id);

        // 회원정보
        model.addAttribute("member", member);

        return "domain/member/myPage/main";
    }

    @GetMapping("/{id}/update")
    public String memberInfoUpdateMain(@PathVariable("id") Long id, Model model) {
        Member member = myPageService.getMemberInfo(id);

        // 회원정보
        model.addAttribute("member", member);

        return "domain/member/myPage/update";
    }

    @PostMapping("/{id}/update")
    public String memberInfoUpdate(@PathVariable("id") Long id
            , MyPageUpdateForm form
            , Model model) {
        RsData<Void> memberRs = myPageService.updateMemberInfo(id, form.getEmail(), form.getNickName());

        Member member = myPageService.getMemberInfo(id);
        model.addAttribute("member", member);

        return rq.redirectOrBack(memberRs, "/my-page/" + id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/my-travel")
    public String myTravel(@PathVariable("id") long id, Model model) {
        List<TravelRoute> travels = travelRouteService.findByAuthorId(id);
        model.addAttribute("travels", travels);

        return "domain/member/myPage/myTravel";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/my-post")
    public String myPost(@PathVariable("id") long id, Model model) {
        List<Post> posts = postService.findByAuthorId(id);
        model.addAttribute("posts", posts);

        return "domain/member/myPage/myPost";
    }
}
