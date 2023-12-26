package com.example.yeobee.common.util.parser;

import java.util.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JwtParser {

    public static String getSocialIdFromJwt(String idToken) throws ParseException {
        String[] jwt = idToken.split("[.]");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(jwt[1]);
        String payloadString = new String(decodedBytes);
        JSONParser jsonParser = new JSONParser();
        JSONObject payload = (JSONObject) (jsonParser.parse(payloadString));
        return (String) payload.get("sub");
    }
}
