package com.example.demo.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {
    private String title;
    private String content;
    private String address;
    private double latitude;
    private double longitude;
    private String placeName;
}
