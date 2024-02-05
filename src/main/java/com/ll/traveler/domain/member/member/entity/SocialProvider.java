package com.ll.traveler.domain.member.member.entity;

public enum SocialProvider {
    KAKAO,
    NAVER,
    GOOGLE,
    ;

    public static SocialProvider fromString(String providerString) {
        return switch (providerString.toUpperCase()) {
            case "KAKAO" -> KAKAO;
            case "NAVER" -> NAVER;
            case "GOOGLE" -> GOOGLE;
            default -> throw new IllegalStateException("지원되지 않는 소셜 로그인입니다.");
        };
    }
}
