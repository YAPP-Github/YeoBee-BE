package com.example.yeobee.core.presigned.dto.response;

import com.example.yeobee.core.presigned.dto.PresignedUrl;
import java.util.List;

public record PresignedUrlResponseDto(List<PresignedUrl> presignedUrlList) {

}