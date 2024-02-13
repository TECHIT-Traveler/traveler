package com.ll.traveler.domain.member.myPage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordForm {
    private Long memberId;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}