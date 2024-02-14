package com.ll.traveler.domain.member.member.controller;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.member.service.MemberService;
import com.ll.traveler.global.rq.Rq;
import com.ll.traveler.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

    @Setter
    @Getter
    public static class JoinForm {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
        @NotBlank
        private String email;
        @NotBlank
        private String nickname;
        private MultipartFile profileImg;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        RsData<Member> joinRs = memberService.join(joinForm.getUsername(), joinForm.getPassword(), joinForm.getEmail(), joinForm.getNickname(), joinForm.getProfileImg());

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