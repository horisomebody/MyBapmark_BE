package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import com.example.demo.responseDto.GoogleUserInfo;
import com.example.demo.domain.User;
import com.example.demo.jwt.JwtProvider;
import com.example.demo.util.OAuth2Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OAuth2Util oAuth2Util;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public String loginWithGoogle(String idToken) {
        // 1. idToken 검증
        GoogleUserInfo googleUser = oAuth2Util.verifyIdToken(idToken);

        // 2. 기존 유저 조회
        User user = userRepository.findByOauthId(googleUser.getSub())
                .orElseGet(() -> {
                    // 3. 없으면 새로 저장 (회원가입)
                    User newUser = User.builder()
                            .oauthId(googleUser.getSub())
                            .email(googleUser.getEmail())
                            .build();
                    return userRepository.save(newUser);
                });

        // 4. JWT 생성
        return jwtProvider.generateToken(user.getOauthId(), user.getEmail());
    }
}

