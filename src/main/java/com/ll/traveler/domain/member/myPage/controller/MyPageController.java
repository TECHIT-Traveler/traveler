package com.ll.traveler.domain.member.myPage.controller;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.myPage.dto.MyPageUpdateForm;
import com.ll.traveler.domain.member.myPage.service.MyPageService;
import com.ll.traveler.global.rq.Rq;
import com.ll.traveler.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/my-page")
@RequiredArgsConstructor // 생성자 주입
public class MyPageController {
    private final MyPageService myPageService;
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
}
