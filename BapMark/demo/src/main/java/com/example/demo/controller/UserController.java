package com.example.demo.controller;

import com.example.demo.jwt.UserDetailsImpl;
import com.example.demo.requestDto.UpdateNicknameRequestDto;
import com.example.demo.responseDto.UserInfoResponseDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "유저 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    @Operation(summary = "내 정보 조회", description = "JWT로 인증된 내 유저 정보를 조회합니다.")@GetMapping("/me")
    public ResponseEntity<UserInfoResponseDto> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser(); // ✔️ 바로 principal에서 유저 정보 추출
        return ResponseEntity.ok(new UserInfoResponseDto(user)); // ✔️ 응답 반환
    }

    private final UserService userService;

    //사용자 정보 조회
    @Operation(summary = "유저 단일 조회", description = "ID로 유저 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    //사용자 탈퇴
    @Operation(summary = "유저 탈퇴", description = "ID로 유저를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("사용자가 성공적으로 삭제되었습니다.");
    }

    //닉네임 변경
    @Operation(summary = "닉네임 변경", description = "JWT로 인증된 유저의 닉네임을 수정합니다.")
    @PatchMapping("/me")
    public ResponseEntity<String> updateNickname(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UpdateNicknameRequestDto request) {

        User user = userDetails.getUser(); // 인증된 사용자 정보 (null 아님 보장)

        userService.updateNickname(user.getId(), request.getNickname());

        return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
    }
}
