package com.example.demo.util;

import com.example.demo.responseDto.GoogleUserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Component;


import java.util.Collections;

@Component
public class OAuth2Util {

    private final GoogleIdTokenVerifier verifier;

    // 📌 생성자에서 verifier 설정 (CLIENT_ID는 반드시 수정해야 함!)
    public OAuth2Util() throws Exception {
        this.verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance()
        )
                .setAudience(Collections.singletonList("259615437161-8u2923f0uqb79qtgnodukisb9bncl6gi.apps.googleusercontent.com")) // 여기에 진짜 CLIENT_ID 넣어야 함
                .build();
    }

    // 📌 id_token 검증하고 사용자 정보 추출
    public GoogleUserInfo verifyIdToken(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new IllegalArgumentException("ID 토큰이 유효하지 않습니다.");
            }

            Payload payload = idToken.getPayload();
            //로그 출력: payload에 담긴 정보를 콘솔에서 확인 가능하다.
            System.out.println("[Google OAUth] payload = \n" + payload.toPrettyString());

            String sub = payload.getSubject(); // Google의 고유 ID
            String email = payload.getEmail();

            return new GoogleUserInfo(sub, email);

        } catch (Exception e) {
            throw new RuntimeException("Google ID 토큰 검증에 실패했습니다.", e);
        }
    }
}
