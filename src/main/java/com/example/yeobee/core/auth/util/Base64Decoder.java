package com.example.yeobee.core.auth.util;

import java.util.Base64;

public class Base64Decoder {

    public static String decode(String encodedString) {
        return new String(Base64.getDecoder().decode(encodedString));
    }
}
