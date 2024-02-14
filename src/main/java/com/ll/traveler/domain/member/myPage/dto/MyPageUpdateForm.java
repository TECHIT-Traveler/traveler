package com.ll.traveler.domain.member.myPage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageUpdateForm {
    @NotBlank(message = "닉네임은 필수입니다.")
    @Email
    private String email;
    @NotBlank(message = "이메일은 필수입니다.")
    private String nickName;
}
