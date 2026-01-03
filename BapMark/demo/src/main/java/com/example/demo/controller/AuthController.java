package com.example.demo.controller;

import com.example.demo.responseDto.LoginResponseDto;
import com.example.demo.requestDto.TokenRequestDto;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "구글 로그인",
            description = "Google ID 토큰을 받아 사용자를 로그인 처리하고 JWT를 반환한다."
    )
    @PostMapping("/google")
    public ResponseEntity<LoginResponseDto> googleLogin(@RequestBody TokenRequestDto tokenRequest) {
        String token = authService.loginWithGoogle(tokenRequest.getIdToken());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}
