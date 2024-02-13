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
            // 유효성 검사에서 오류가 발생한 경우, 바로 실패 처리하고 리다이렉트합니다.
            // 회원가입은 수행되지 않으므로 joinRs가 필요하지 않습니다.
            model.addAttribute("MemberDto", memberDto);
            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "redirect:/member/join";
        } else {
            // 유효성 검사를 통과한 경우에만 회원가입을 시도합니다.
            RsData<Member> joinRs = memberService.join(memberDto);
            return rq.redirectOrBack(joinRs, "/member/login");
        }
    }

    @GetMapping("/login")
    public String showLogin() {
        return "domain/member/member/login";
    }

}

