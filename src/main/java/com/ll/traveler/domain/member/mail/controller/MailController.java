package com.ll.traveler.domain.member.mail.controller;

import com.ll.traveler.domain.member.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @GetMapping("/mail")
    public String MailPage() {
        return "Mail";
    }

    @ResponseBody
    @PostMapping("/mail")
    public String MailSend(String email) {

        int number = mailService.sendMail(email);
        String num = "" + number;
        return num;
    }
}
