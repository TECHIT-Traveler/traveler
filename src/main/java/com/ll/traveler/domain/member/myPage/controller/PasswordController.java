package com.ll.traveler.domain.member.myPage.controller;

import com.ll.traveler.domain.member.myPage.service.MyPageService;
import com.ll.traveler.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PasswordController {
    private final MyPageService myPageService;

    @GetMapping("/password")
    public String showPasswordForm(Model model) {
        // 비밀번호 변경 폼을 보여주는 페이지로 이동
        model.addAttribute("passwordForm", new PasswordForm());
        return "domain/member/myPage/password"; // 비밀번호 변경 페이지 템플릿 이름
    }

    @PostMapping("/password/change")
    public String changePassword(@ModelAttribute PasswordForm passwordForm, Model model) {
        RsData<Void> result = myPageService.changePassword(passwordForm.getMemberId(),
                passwordForm.getCurrentPassword(),
                passwordForm.getNewPassword(),
                passwordForm.getConfirmPassword());

        if (result.isSuccess()) {
            // 성공적으로 변경되면 성공 메시지를 모델에 추가하여 비밀번호 변경 페이지로 리다이렉트
            model.addAttribute("successMessage", result.getMessage());
            return "redirect:/password";
        } else {
            // 변경에 실패하면 실패 메시지를 모델에 추가하여 다시 비밀번호 변경 페이지로 이동
            model.addAttribute("errorMessage", result.getMessage());
            return "redirect:/password";
        }
    }
}