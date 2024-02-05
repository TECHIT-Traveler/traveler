package com.ll.traveler.global.security;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.member.entity.SocialProvider;
import com.ll.traveler.domain.member.member.service.MemberService;
import com.nimbusds.jwt.JWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Member member = null;
        // Factory pattern

        // 카카오 로그인 처리
        if (providerTypeCode.equals("KAKAO")) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map attributesProperties = (Map) attributes.get("properties");
            String nickname = (String) attributesProperties.get("nickname");
            String profileImgUrl = (String) attributesProperties.get("profile_image");

            String oauthId = oAuth2User.getName();
            String username = "KAKAO" + "__%s".formatted(oauthId);

            member = memberService.whenSocialLogin("KAKAO",username, nickname, "",profileImgUrl).getData();

        } else if ( providerTypeCode.equals("NAVER")) {

            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> loginValue = (Map<String, Object>) attributes.get("response");

            member = memberService.whenSocialLogin("NAVER", loginValue.get("id").toString(),
                    loginValue.get("name").toString(), loginValue.get("email").toString(),"").getData();


        } else if (providerTypeCode.equals("GOOGLE")){

            Map<String, Object> attributes = oAuth2User.getAttributes();

            for (String s : attributes.keySet()) {
                System.out.println("s = " + s);
            }
                    member = memberService.whenSocialLogin("GOOGLE", attributes.get("sub").toString(),
                    attributes.get("name").toString(), "",attributes.get("picture").toString()).getData();

        }

        return new SecurityUser(member.getId(), member.getUsername(), "", member.getAuthorities());
    }
}
