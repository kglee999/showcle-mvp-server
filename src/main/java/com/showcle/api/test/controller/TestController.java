package com.showcle.api.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/test")
public class TestController {

    // SNS 로그인 테스트
    @GetMapping("/login")
    public String loginPage() {
        return "test/login";
    }
}
