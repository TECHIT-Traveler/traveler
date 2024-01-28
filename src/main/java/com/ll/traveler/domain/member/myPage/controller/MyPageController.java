package com.ll.traveler.domain.member.myPage.controller;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.myPage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/my-page")
@RequiredArgsConstructor // 생성자 주입
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/{id}")
    public String myPageMain(@PathVariable("id") Long id, Model model) {
        Member member = myPageService.getMemberInfo(id);

        // 회원정보
        model.addAttribute("member", member);

        return "domain/member/myPage/main";
    }
}
