package com.example.yeobee.core.trip.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// TODO: S3 이미지 경로 매핑 구현
@Getter
@RequiredArgsConstructor
public enum TripUserDefaultProfileImageType {
    IMAGE0("test0"),
    IMAGE1("test1"),
    IMAGE2("test2"),
    IMAGE3("test3"),
    IMAGE4("test4"),
    IMAGE5("test5"),
    IMAGE6("test6"),
    IMAGE7("test7"),
    IMAGE8("test8"),
    IMAGE9("test9");
    
    private final String imageUrl;
}
