package com.example.demo.controller;

import com.example.demo.jwt.UserDetailsImpl;
import com.example.demo.service.StampBoardService;
import com.example.demo.domain.StampBoard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Tag(name = "스탬프보드 API", description = "스탬프보드 관련 API입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stampboards")
public class StampBoardController {

    private final StampBoardService stampBoardService;

    // 보드 생성
    @Operation(summary = "스탬프보드 생성")
    @PostMapping
    public ResponseEntity<StampBoard> createBoard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @RequestParam String title,
                                                  @RequestParam String color) {
        Long userId = userDetails.getUser().getId(); // 또는 userDetails.getId() 형태
        StampBoard board = stampBoardService.createStampBoard(userId, title, color);
        return ResponseEntity.ok(board);
    }

    // 보드 이름 수정
    @Operation(summary = "스탬프보드 제목 수정")
    @PatchMapping("/{boardId}/title")
    public ResponseEntity<?> updateBoardTitle(@PathVariable Long boardId,
                                              @RequestParam String title,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) throws AccessDeniedException {
        Long userId = userDetails.getUser().getId(); // 로그인한 사용자 ID 가져오기

        stampBoardService.updateBoardTitle(boardId, title, userId);
        return ResponseEntity.ok("보드 이름이 수정되었습니다.");
    }

    // 보드 컬러 수정
    @Operation(summary = "스탬프보드 컬러 수정")
    @PatchMapping("/{boardId}/color")
    public ResponseEntity<?> updateBoardColor(@PathVariable Long boardId,
                                              @RequestParam String color,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) throws AccessDeniedException {
        Long userId = userDetails.getUser().getId(); // 로그인한 사용자 ID 추출
        stampBoardService.updateBoardColor(boardId, color, userId);
        return ResponseEntity.ok("보드 컬러가 수정되었습니다.");
    }

    // 유저의 보드 목록 조회
    @Operation(summary = "사용자의 스탬프보드 목록 조회")
    @GetMapping("/me/boards")
    public ResponseEntity<List<StampBoard>> getMyBoards(@AuthenticationPrincipal UserDetailsImpl user) {
        Long userId = user.getId();
        return ResponseEntity.ok(stampBoardService.getStampBoardsByUser(userId));
    }

    // 특정 보드 상세 조회
    @Operation(summary = "스탬프보드 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<StampBoard> getBoard(@PathVariable Long id,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) throws AccessDeniedException {
        Long userId = userDetails.getUser().getId();
        StampBoard board = stampBoardService.getStampBoard(id);

        // 🔒 사용자 소유 검증
        if (!board.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("이 보드에 접근할 권한이 없습니다.");
        }

        return ResponseEntity.ok(board);
    }

    // 보드 삭제
    @Operation(summary = "스탬프보드 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) throws AccessDeniedException {
        Long userId = userDetails.getUser().getId();
        stampBoardService.deleteStampBoard(id, userId);
        return ResponseEntity.ok("삭제 완료");
    }

    // 스탬프보드에 북마크 추가
    @Operation(summary = "스탬프보드에 북마크 추가")
    @PostMapping("/{boardId}/bookmark")
    public ResponseEntity<?> addBookmark(@PathVariable Long boardId,
                                         @RequestBody Long bookmarkId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId(); // 로그인한 사용자 ID

        try {
            stampBoardService.addBookmarkToBoard(boardId, bookmarkId, userId); // userId 전달
            return ResponseEntity.ok("북마크 추가 완료");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }

    // 스탬프보드에서 북마크 삭제
    @Operation(summary = "스탬프보드에서 북마크 삭제")
    @DeleteMapping("/{boardId}/bookmark")
    public ResponseEntity<?> removeBookmark(@PathVariable Long boardId,
                                            @RequestBody Long bookmarkId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();

        try {
            stampBoardService.removeBookmarkFromBoard(boardId, bookmarkId, userId);
            return ResponseEntity.ok("북마크 삭제 완료");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }



}

