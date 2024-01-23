package com.ll.traveler.domain.member.member.service;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.member.repository.MemberRepository;
import com.ll.traveler.global.rsData.RsData;
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
    public RsData<Member> join(String email, String password, String nickname) {
        if (findByEmail(email).isPresent()){
            return RsData.of("400-2", "이미 존재하는 회원입니다.");
        }
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .build();

        memberRepository.save(member);

        return RsData.of("200", "%s님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.".formatted(member.getEmail()), member);
    }

    public Optional<Member> findByEmail(String email){
        return memberRepository.findByEmail(email);
    }
}
