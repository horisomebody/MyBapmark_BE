package com.example.demo.service;

import com.example.demo.repository.ShareLinkRepository;
import com.example.demo.repository.StampBoardRepository;
import com.example.demo.domain.ShareLink;
import com.example.demo.domain.StampBoard;
import com.example.demo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.nio.file.AccessDeniedException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShareLinkService {

    private final ShareLinkRepository shareLinkRepository;
    private final StampBoardRepository stampBoardRepository;

    public String createShareLink(Long boardId, Long userId) throws AccessDeniedException {
        StampBoard board = stampBoardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // 🔐 소유자 검증 추가
        if (!board.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("공유 링크를 생성할 권한이 없습니다.");
        }

        String uuid = UUID.randomUUID().toString();

        ShareLink link = new ShareLink();
        link.setUuid(uuid);
        link.setOriginalBoard(board);
        shareLinkRepository.save(link);

        return "https://yourdomain.com/share/" + uuid;
    }


    public void importSharedBoard(String uuid, User user) {
        ShareLink link = shareLinkRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Link not found"));
        StampBoard original = link.getOriginalBoard();

        StampBoard copy = new StampBoard();
        copy.setTitle(original.getTitle());
        copy.setUser(user);
        copy.getBookmarks().addAll(original.getBookmarks());
        stampBoardRepository.save(copy);
    }
}

