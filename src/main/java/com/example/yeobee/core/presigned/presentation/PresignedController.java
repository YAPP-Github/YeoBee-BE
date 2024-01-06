package com.example.yeobee.core.presigned.presentation;

import com.example.yeobee.core.presigned.application.PresignedService;
import com.example.yeobee.core.presigned.presentation.dto.response.PresignedUrlResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/presigned-url")
@RestController
@RequiredArgsConstructor
public class PresignedController {

    private final PresignedService presignedService;

    @GetMapping
    public List<PresignedUrlResponseDto> getPresignedUrl(@RequestParam List<String> fileNames) {
        return fileNames.stream()
            .map(presignedService::getPresignedUrl)
            .toList();
    }
}