package com.ll.traveler.domain.member.myPage.service;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.member.repository.MemberRepository;
import com.ll.traveler.global.rq.Rq;
import com.ll.traveler.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;
    private final Rq rq;
    private final PasswordEncoder passwordEncoder;

    public Member getMemberInfo(Long id) {
        return rq.getMemberOrThrow(id);
    }

    public RsData<Void> updateMemberInfo(Long id, String email, String nickName) {
        Member member = rq.getMemberOrThrow(id);

        if(member == null) {
            return RsData.of("500", "수정중 문제가 발생하였습니다.");
        }

        member.setEmail(email);
        member.setNickname(nickName);
        member.setModifyDate(LocalDateTime.now());

        memberRepository.save(member);
        return RsData.of("200", "수정이 완료되었습니다");
    }

    public RsData<Void> changePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
        Member member = rq.getMemberOrThrow(id);

        if (member == null) {
            return RsData.of("500", "비밀번호 변경 중 문제가 발생하였습니다.");
        }

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            return RsData.of("403", "현재 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호 확인
        if (!newPassword.equals(confirmPassword)) {
            return RsData.of("400", "새로운 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 변경
        member.setPassword(passwordEncoder.encode(newPassword));
        member.setModifyDate(LocalDateTime.now());

        memberRepository.save(member);
        return RsData.of("200", "비밀번호가 성공적으로 변경되었습니다.");
    }
}