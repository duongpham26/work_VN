package com.duongpham26.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String getMethodName() {
        if(true) {
            // throw IdInvalidException("error");
        }
        return "hello";
    }
}
