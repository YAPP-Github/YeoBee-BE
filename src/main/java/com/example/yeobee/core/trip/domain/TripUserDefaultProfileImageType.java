package com.example.yeobee.core.trip.domain;

import com.example.yeobee.common.util.UrlUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TripUserDefaultProfileImageType {
    IMAGE0("image0"),
    IMAGE1("image1"),
    IMAGE2("image2"),
    IMAGE3("image3"),
    IMAGE4("image4"),
    IMAGE5("image5"),
    IMAGE6("image6"),
    IMAGE7("image7"),
    IMAGE8("image8"),
    IMAGE9("image9");

    private static final String S3_PROFILE_IMAGE_KEY_PATH = "static/user/profile-image/";

    private final String key;

    // TODO: 동적으로 CDN 주소 설정하도록
    public String getImageUrl() {
        return UrlUtil.join("https://cdn.yeobee.me", S3_PROFILE_IMAGE_KEY_PATH, key);
    }
}
