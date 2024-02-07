package com.ll.traveler.domain.member.myPage.service;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.member.repository.MemberRepository;
import com.ll.traveler.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;

    public Member getMemberInfo(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));
    }

    public RsData<Void> updateMemberInfo(Long id, String email, String nickName) {
        Member member = memberRepository.findById(id).orElse(null);

        if(member == null) {
            return RsData.of("500", "수정중 문제가 발생하였습니다.");
        }

        member.setEmail(email);
        member.setNickname(nickName);
        member.setModifyDate(LocalDateTime.now());

        memberRepository.save(member);
        return RsData.of("200", "수정이 완료되었습니다");
    }

}
