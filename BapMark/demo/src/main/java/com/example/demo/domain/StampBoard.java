package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="StampBoard")
@Getter
@Setter
public class StampBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 스탬프판의 주인 (로그인한 사용자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 스탬프판 이름 (예: 한식, 양식, 카페 등)
    @Column(nullable = false)
    private String title;

    // 스탬프 색깔
    @Column(nullable = false)
    private String color;

    // 생성 시각
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 연결된 장소들 (ManyToMany 또는 중간 테이블 필요)
    @ManyToMany
    @JoinTable(
            name = "stampboard_place",
            joinColumns = @JoinColumn(name = "stampboard_id"),
            inverseJoinColumns = @JoinColumn(name = "bookmark_id")
    )
    private List<Bookmark> bookmarks = new ArrayList<>();

    // 기본 생성자
    public StampBoard() {}

    public StampBoard(User user, String title, String color) {
        this.user = user;
        this.title = title;
    }
}

