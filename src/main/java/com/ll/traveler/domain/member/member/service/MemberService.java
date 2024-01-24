package com.ll.traveler.domain.member.member.service;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.member.repository.MemberRepository;

import com.ll.traveler.global.app.AppConfig;
import com.ll.traveler.global.rsData.RsData;
import com.ll.traveler.standard.utill.Ut;
import jakarta.validation.constraints.NotBlank;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<Member> join(String username, String password,String email, String nickname) {
        if (findByUsername(username).isPresent()){
            return RsData.of("400-2", "이미 존재하는 회원입니다.");
        }
        Member member = Member.builder()
                .username(username)

                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .build();

        memberRepository.save(member);

        return RsData.of("200", "%s님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.".formatted(member.getUsername()), member);
    }

    public Optional<Member> findByUsername(String username){
        return memberRepository.findByUsername(username);
    }
    @Transactional
    public RsData<Member> join(String username, String password, String nickname) {
        if (findByUsername(username).isPresent()){
            return RsData.of("400-2", "이미 존재하는 회원입니다.");
        }
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .build();

        memberRepository.save(member);

        return RsData.of("200", "%s님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.".formatted(member.getUsername()), member);
    }
    @Transactional
    public RsData<Member> whenSocialLogin(String providerTypeCode, String username,String nickname, String profileImgUrl) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) return RsData.of("200", "이미 존재합니다.", opMember.get());

        String filePath = Ut.str.hasLength(profileImgUrl) ?
                Ut.file.downloadFileByHttp(profileImgUrl, AppConfig.getTempDirPath()) : "";

        return join(username,"", nickname);
    }
}
