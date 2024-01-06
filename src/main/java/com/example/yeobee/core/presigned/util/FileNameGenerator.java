package com.example.yeobee.core.presigned.util;

import java.util.UUID;

public class FileNameGenerator {

    public static String generateFileName(String originalFileName) {
        boolean hasExtension = originalFileName.contains(".");
        String newFileKey = UUID.randomUUID().toString();

        if (hasExtension) {
            return newFileKey + originalFileName.substring(originalFileName.lastIndexOf("."));
        } else {
            return newFileKey;
        }
    }
}
