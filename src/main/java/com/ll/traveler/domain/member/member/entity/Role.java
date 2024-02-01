package com.ll.traveler.domain.member.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role{
    MEMBER("ROLE_MEMBER");

    private final String key;

}
