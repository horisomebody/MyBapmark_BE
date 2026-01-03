package com.example.demo.responseDto;

import com.example.demo.domain.Bookmark;
import com.example.demo.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkResponseDto {
    private Long postId;
    private String title;
    private String address;
    private double latitude;
    private double longitude;
    private String placeName;
    private boolean visited;


    public BookmarkResponseDto(Bookmark bookmark) {
        Post post = bookmark.getPost();
        this.postId = post.getId();
        this.title = post.getTitle();
        this.address = post.getAddress();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.placeName = post.getPlaceName();
        this.visited = bookmark.getVisited();
    }
}

