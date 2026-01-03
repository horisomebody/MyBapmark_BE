package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="Bookmark")
@Getter
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 북마크한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 게시글이 없을 수도 있으므로 nullable = true
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "visited", nullable = false)
    private Boolean visited = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 북마크가 어떤 스탬프판에 속하는지 확인용
    @ManyToMany(mappedBy = "bookmarks")
    private List<StampBoard> stampBoard = new ArrayList<>();


    public Bookmark() {}

    // 검색으로 저장하는 경우
    public Bookmark(User user, String placeName, String address, Double lat, Double lng) {
        this.user = user;
        this.placeName = placeName;
        this.address = address;
        this.latitude = lat;
        this.longitude = lng;
        this.visited = false;
        this.createdAt = LocalDateTime.now();
    }

    // 게시글로 저장하는 경우
    public Bookmark(User user, Post post) {
        this.user = user;
        this.post = post;
        this.address = post.getAddress();
        this.placeName = post.getPlaceName();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.visited = false;
        this.createdAt = LocalDateTime.now();
    }
}

