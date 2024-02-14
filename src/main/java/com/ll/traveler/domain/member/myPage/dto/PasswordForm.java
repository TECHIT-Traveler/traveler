package com.ll.traveler.domain.member.myPage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordForm {
    private Long memberId;

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    private String newPassword;

    @NotBlank(message = "새로운 비밀번호 확인을 입력해주세요.")
    private String confirmPassword;
}
