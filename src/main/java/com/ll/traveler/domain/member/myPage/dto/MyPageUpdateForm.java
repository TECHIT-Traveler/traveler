package com.ll.traveler.domain.member.myPage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageUpdateForm {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String nickName;
}
