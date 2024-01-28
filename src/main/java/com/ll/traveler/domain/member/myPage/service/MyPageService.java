package com.ll.traveler.domain.member.myPage.service;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;

    public Member getMemberInfo(String id) {
        return memberRepository.findByUsername(id).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));
    }

}
