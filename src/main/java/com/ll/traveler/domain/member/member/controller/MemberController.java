package com.ll.traveler.domain.member.member.controller;

import com.ll.traveler.domain.member.mail.service.MailService;
import com.ll.traveler.domain.member.member.MemberDto;
import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.member.service.MemberService;
import com.ll.traveler.global.rq.Rq;
import com.ll.traveler.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
    public String join(@Valid MemberDto memberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/member/join";

        }
        if (!memberDto.getPassword().equals(memberDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "redirect:/member/join";
        }
        RsData<Member> joinRs = memberService.join(memberDto);
        return rq.redirectOrBack(joinRs, "/member/login");

    }


    @GetMapping("/login")
    public String showLogin() {
        return "domain/member/member/login";
    }


    @RequestMapping( "/findLoginId")
    String showFindLoginId() {
        return "domain/member/member/findLoginId";
    }
    @RequestMapping( "/dofindLoginId")
    @ResponseBody
    String doFindLoginId(@RequestParam Map<String, Object> param) {
        Map<String, Object> findLoginIdRs = memberService.findLoginId(param);

        return (String)findLoginIdRs.get("msg");
    }
}


