package com.alex.openaiimage.util;

import com.google.gson.JsonObject;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.Map;

@Log
public class OpenAIRequestBuilder {
    private OpenAIRequestBuilder() {
    }

    public static String buildBody(String prompt, int number, String size) {
        JsonObject body = new JsonObject();

        body.addProperty("prompt", prompt);
        body.addProperty("n", number);
        body.addProperty("size", size);

        return body.toString();
    }

    public static Map<String, String> buildHttpHeader(String accessKey) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", accessKey);
        header.put("Content-Type", "application/json");

        return header;
    }
}
