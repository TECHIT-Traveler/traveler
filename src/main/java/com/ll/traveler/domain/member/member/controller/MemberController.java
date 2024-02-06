package com.ll.traveler.domain.member.member.controller;

import com.ll.traveler.domain.member.member.JoinForm;
import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.member.service.MemberService;
import com.ll.traveler.global.rq.Rq;
import com.ll.traveler.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "domain/member/member/join";
    }


    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm, Errors errors, Model model) {
        RsData<Member> joinRs = memberService.join(joinForm.getUsername(), joinForm.getPassword(), joinForm.getEmail(), joinForm.getNickname());
        if (errors.hasErrors()) {
            // 회원가입 실패시, 입력 데이터를 유지
            model.addAttribute("JoinForm", joinForm);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return rq.redirectOrBack(joinRs, "/member/login");
        }
        return rq.redirectOrBack(joinRs, "/member/login");

    }

    @GetMapping("/login")
    public String showLogin() {
        return "domain/member/member/login";
    }

}

