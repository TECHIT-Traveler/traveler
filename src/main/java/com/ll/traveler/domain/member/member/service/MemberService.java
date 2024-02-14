package com.ll.traveler.domain.member.member.service;

import com.ll.traveler.domain.base.genFile.entity.GenFile;
import com.ll.traveler.domain.base.genFile.service.GenFileService;
import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.member.repository.MemberRepository;

import com.ll.traveler.global.app.AppConfig;
import com.ll.traveler.global.rsData.RsData;
import com.ll.traveler.standard.utill.Ut;

import lombok.RequiredArgsConstructor;
import org.apache.groovy.util.Maps;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final GenFileService genFileService;

    public Optional<Member> findByUsername(String username){
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public RsData<Member> join(String providerTypeCode,String username, String nickname, String email,String filePath) {
        return join(providerTypeCode,username,"",email, nickname,filePath);
    }

    @Transactional
    public RsData<Member> join(String username, String password, String email, String nickname, MultipartFile profileImg) {
        String profileImgFilePath = Ut.file.toFile(profileImg, AppConfig.getTempDirPath());

        return join("MEMBER",username, password, email, nickname, profileImgFilePath);
    }

    @Transactional
    public RsData<Member> join(String providerTypeCode, String username,String password,String email ,String nickname, String profileImgFilePath) {
        if (findByUsername(username).isPresent()){
            return RsData.of("400-2", "이미 존재하는 회원입니다.");
        }
        // 소셜 로그인을 통한 회원가입에서는 비번이 없다.
        if (StringUtils.hasText(password)) password = passwordEncoder.encode(password);

        Member member = Member
                .builder()
                .providerTypeCode(providerTypeCode)
                .username(username)
                .password(password)
                .email(email)
                .nickname(nickname)
                .build();

        memberRepository.save(member);

        if (Ut.str.hasLength(profileImgFilePath)) {
            saveProfileImg(member, profileImgFilePath);
        }

        return RsData.of("200", "%s님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.".formatted(member.getUsername()), member);
    }
    private void saveProfileImg(Member member, String profileImgFilePath) {
        genFileService.save(member.getModelName(), member.getId(), "common", "profileImg", 1, profileImgFilePath);
    }

    @Transactional
    public RsData<Member> whenSocialLogin(String providerTypeCode , String username, String nickname,String email,String profileImgUrl) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) return RsData.of("200", "이미 존재합니다.", opMember.get());

        String filePath = Ut.str.hasLength(profileImgUrl) ? Ut.file.downloadFileByHttp(profileImgUrl, AppConfig.getTempDirPath()) : "";

        return join(providerTypeCode,username,nickname,email,filePath);
    }

    public String getProfileImgUrl(Member member) {
        return Optional.ofNullable(member)
                .flatMap(this::findProfileImgUrl)
                .orElse("https://placehold.co/30x30?text=UU");
    }

    private Optional<String> findProfileImgUrl(Member member) {
        return genFileService.findBy(
                        member.getModelName(), member.getId(), "common", "profileImg", 1
                )
                .map(GenFile::getUrl);
    }

    public Map<String, Object> findLoginId(Map<String, Object> param) {
        String username = (String) param.get("username");
        String email = (String) param.get("email");

        Member member = memberRepository.findByUsernameAndEmail(username, email);
        if (member == null) {
           return Maps.of("resultCode", "F-1", "msg", "일지하는 회원이 없습니다.");
        }
        return Maps.of("resultCode", "S-1", "msg", "당신의 로그인 아이디는 " +member.getUsername() +"입니다");
    }
}