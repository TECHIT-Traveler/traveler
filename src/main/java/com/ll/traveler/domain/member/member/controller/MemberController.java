package com.ll.traveler.domain.member.member.controller;

import com.ll.traveler.domain.member.mail.service.MailService;
import com.ll.traveler.domain.member.member.MemberDto;
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
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MailService mailService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin(MemberDto memberDto) {
        return "domain/member/member/join";
    }


    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid MemberDto memberDto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("memberDto", memberDto);

            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "domain/member/member/login";
        }
        RsData<Member> joinRs = memberService.join(memberDto);
        return rq.redirectOrBack(joinRs, "/member/login");
    }

    @GetMapping("/login")
    public String showLogin() {
        return "domain/member/member/login";
    }
}



