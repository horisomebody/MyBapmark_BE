package com.example.demo.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkRequestDto {
    private String placeName;
    private String address;
    private Double latitude;
    private Double longitude;
}
