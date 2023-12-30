package com.example.yeobee.common.util;

import java.util.Base64;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JwtParser {

    @SneakyThrows
    public static String getSocialIdFromJwt(String idToken) {
        String[] jwt = idToken.split("[.]");
        byte[] decodedBytes = Base64.getDecoder().decode(jwt[1]);
        String payloadString = new String(decodedBytes);
        JSONObject payload = (JSONObject) (new JSONParser().parse(payloadString));
        return (String) payload.get("sub");
    }
}
