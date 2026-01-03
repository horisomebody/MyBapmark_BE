package com.example.demo.responseDto;

import com.example.demo.domain.StampBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StampBoardDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;


    // 📌 정적 팩토리 메서드
    public static StampBoardDto fromEntity(StampBoard board) {
        return new StampBoardDto(
                board.getId(),
                board.getTitle(),
                board.getCreatedAt()
        );
    }
}



