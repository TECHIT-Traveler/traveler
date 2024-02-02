package com.ll.traveler.global.security;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.member.entity.Role;
import com.ll.traveler.domain.member.member.entity.SocialProvider;
import com.ll.traveler.domain.member.member.repository.MemberRepository;
import com.ll.traveler.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        // 카카오 로그인 처리
        if ("KAKAO".equals(providerTypeCode)) {
            return processKakaoUser(oAuth2User);
        }

        // 다른 OAuth2 제공자에 대한 처리
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        String oauthId = oAuth2User.getName();
        String username = providerTypeCode + "__%s".formatted(oauthId);

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(),SocialProvider.fromString(attributes.getProvider())))
                .orElse(attributes.toEntity());

        return memberRepository.save(member);
    }

    private OAuth2User processKakaoUser(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        String nickname = (String) properties.get("nickname");
        String profileImgUrl = (String) properties.get("profile_image");
        String oauthId = oAuth2User.getName();
        String username = "KAKAO__%s".formatted(oauthId);
        Role role = (Role) attributes.get("ROLE_MEMBER");

        Member member = memberService.whenSocialLogin("KAKAO", username, nickname,role ,profileImgUrl).getData();

        return new SecurityUser(member.getId(), member.getUsername(), member.getPassword(), member.getAuthorities());
    }
}
