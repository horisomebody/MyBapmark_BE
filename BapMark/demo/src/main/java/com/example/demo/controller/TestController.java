package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "JWT 인증 테스트 API", description = "JWT 인증이 필요한 보호된 API 테스트용")
@RestController
@RequestMapping("/api/secure")
public class TestController {

    @Operation(summary = "JWT 인증 테스트용 API")
    @GetMapping("/test")
    public String secureEndpoint() {
        return "✅ JWT 인증 완료! 보호된 API 접근 성공";
    }
}
