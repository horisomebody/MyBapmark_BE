package com.example.demo.responseDto;

import com.example.demo.domain.User;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private Long id;
    private String email;
    private String nickname; // ✅ 닉네임 추가

    public UserInfoResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname(); // ✅ 매핑
    }
}
