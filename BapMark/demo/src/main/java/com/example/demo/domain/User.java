package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // ✅ JPA 기본 생성자
@AllArgsConstructor // (선택) 테스트나 필요 시 사용
@Builder // ✅ 빌더 자동 생성
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //DB에서 자동 증가하는 기본키 (PK)

    @Column(nullable = false)
    private String oauthId; // 구글에서 내려준 유저 식별자 (변경 불가)

    @Column(nullable = false)
    private String email;

    @Column(nullable = true,unique = true)
    private String nickname;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

}
