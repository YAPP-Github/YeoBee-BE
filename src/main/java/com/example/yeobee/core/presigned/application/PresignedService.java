package com.example.yeobee.core.presigned.application;

import com.example.yeobee.common.util.UrlUtil;
import com.example.yeobee.core.presigned.presentation.dto.response.PresignedUrlResponseDto;
import com.example.yeobee.core.presigned.util.FileNameGenerator;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class PresignedService {

    private static final String METADATA_KEY_ORIGINAL_NAME = "original-name";
    private static final Duration SIGNATURE_DURATION = Duration.ofMinutes(1);

    private final S3Presigner s3Presigner;
    private final String bucketName;
    private final String cdnUrl;

    public PresignedService(
        S3Presigner s3Presigner,
        @Value("${spring.cloud.aws.s3.bucket}") String bucketName,
        @Value("${spring.cloud.aws.cdn.url}") String cdnUrl
    ) {
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
        this.cdnUrl = cdnUrl;
    }

    public PresignedUrlResponseDto getPresignedUrl(String fileName) {
        final String newFileName = FileNameGenerator.generateFileName(fileName);
        final String filePath = "images/" + newFileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(filePath)
            .metadata(Map.of(METADATA_KEY_ORIGINAL_NAME, fileName))
            .build();

        PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(SIGNATURE_DURATION)
            .putObjectRequest(putObjectRequest)
            .build();

        String presignedUrl = s3Presigner.presignPutObject(preSignRequest).url().toString();
        String fileUrl = UrlUtil.join(cdnUrl, filePath);

        return new PresignedUrlResponseDto(presignedUrl, fileUrl);
    }
}