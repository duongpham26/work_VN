package com.duongpham26.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.service.EmailService;
import com.duongpham26.demo.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    @ApiMessage("Send email")
    public String get() {
        // this.emailService.sendEmailSync("phamthanhduong8a1@gmail.com", "test Spring
        // Boot", "<h1>Hello<h1>", false,
        // true);
        this.emailService.sendEmailFromTemplateSync("phamthanhduong8a1@gmail.com", "TEST", "test");
        return "hello";
    }
}
