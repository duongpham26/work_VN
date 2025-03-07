package com.duongpham26.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.service.EmailService;
import com.duongpham26.demo.service.SubScriberService;
import com.duongpham26.demo.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;

    private final SubScriberService subScriberService;

    public EmailController(EmailService emailService, SubScriberService subScriberService) {
        this.emailService = emailService;
        this.subScriberService = subScriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send email")
    public String get() {
        // this.emailService.sendEmailSync("phamthanhduong8a1@gmail.com", "test Spring
        // Boot", "<h1>Hello<h1>", false,
        // true);
        this.subScriberService.sendSubscribersEmail();
        return "hello";
    }
}
