package com.example.demo.service;

import com.example.demo.repository.BookmarkRepository;
import com.example.demo.repository.StampBoardRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.domain.Bookmark;
import com.example.demo.domain.StampBoard;
import com.example.demo.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StampBoardService {

    private final StampBoardRepository stampBoardRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;

    // 스탬프보드 생성
    @Transactional
    public StampBoard createStampBoard(Long userId, String title, String color) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
        StampBoard board = new StampBoard(user, title, color);
        return stampBoardRepository.save(board);
    }


    @Transactional
    public void updateBoardTitle(Long boardId, String newTitle, Long userId) throws AccessDeniedException {
        StampBoard board = stampBoardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("보드를 찾을 수 없습니다."));

        if (!board.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("해당 보드를 수정할 권한이 없습니다.");
        }

        board.setTitle(newTitle);
    }


    @Transactional
    public void updateBoardColor(Long boardId, String color, Long userId) throws AccessDeniedException {
        StampBoard board = stampBoardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        if (!board.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("해당 보드를 수정할 권한이 없습니다.");
        }

        board.setColor(color);
        stampBoardRepository.save(board);
    }


    // 특정 유저의 스탬프보드 목록 조회
    public List<StampBoard> getStampBoardsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
        return stampBoardRepository.findByUser(user);
    }

    // 특정 스탬프보드 하나 조회
    public StampBoard getStampBoard(Long boardId) {
        return stampBoardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("스탬프보드를 찾을 수 없습니다."));
    }

    // 스탬프보드 삭제
    @Transactional
    public void deleteStampBoard(Long boardId, Long userId) throws AccessDeniedException {
        // 1. 보드 조회
        StampBoard board = stampBoardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("보드를 찾을 수 없습니다."));

        // 2. 로그인한 사용자가 이 보드의 주인인지 검증
        if (!board.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("이 보드를 삭제할 권한이 없습니다.");
        }

        // 3. 삭제
        stampBoardRepository.delete(board);
    }

    // 북마크 추가
    @Transactional
    public void addBookmarkToBoard(Long stampBoardId, Long bookmarkId, Long userId) throws AccessDeniedException {
        StampBoard board = stampBoardRepository.findById(stampBoardId)
                .orElseThrow(() -> new RuntimeException("StampBoard not found"));

        // 🔐 소유자 검증
        if (!board.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("해당 보드를 수정할 권한이 없습니다.");
        }

        if (board.getBookmarks().size() >= 10) {
            throw new IllegalStateException("최대 10개의 장소만 추가할 수 있습니다.");
        }

        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        board.getBookmarks().add(bookmark);
    }



    @Transactional
    public void removeBookmarkFromBoard(Long boardId, Long bookmarkId, Long userId) throws AccessDeniedException {

        StampBoard board = stampBoardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("스탬프 보드를 찾을 수 없습니다."));

        if (!board.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("해당 보드에 접근할 권한이 없습니다.");
        }

        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new RuntimeException("북마크를 찾을 수 없습니다."));

        if (!board.getBookmarks().contains(bookmark)) {
            throw new RuntimeException("해당 북마크는 이 스탬프 보드에 포함되어 있지 않습니다.");
        }

        board.getBookmarks().remove(bookmark);
        stampBoardRepository.save(board); // 변경 감지로 생략해도 되긴 함
    }


}

