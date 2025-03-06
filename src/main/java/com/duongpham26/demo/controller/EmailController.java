package com.duongpham26.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    @GetMapping("/email")
    @ApiMessage("Send email")
    public String get() {
        return "hello";
    }
}
