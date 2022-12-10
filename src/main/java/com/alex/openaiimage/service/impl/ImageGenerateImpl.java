package com.alex.openaiimage.service.impl;

import com.alex.openaiimage.entity.IFlyResponse;
import com.alex.openaiimage.entity.ImageResponse;
import com.alex.openaiimage.service.ImageGenerate;
import com.alex.openaiimage.entity.ImageSize;
import com.alex.openaiimage.util.HttpUtil;
import com.alex.openaiimage.util.IFlyRequestBuilder;
import com.alex.openaiimage.util.OpenAIRequestBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import jakarta.annotation.Resource;
import lombok.extern.java.Log;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Log
public class ImageGenerateImpl implements ImageGenerate {
    @Resource
    Environment environment;

    private String prompt;

    @Override
    public List<String> getImageUrl(String prompt, int number, ImageSize imageSize, String srcLanguage) throws Exception {
        this.prompt = prompt;
        List<String> resList = new ArrayList<>(number);
        String size = imageSize.getSizeNum();

        // 输入内容为中文
        if (srcLanguage.equals("zh")) {
            IFlyResponse iFlyResponse = getFlyResponse(prompt);

            if (iFlyResponse.getCode() != 0) {
                log.warning("请前往https://www.xfyun.cn/document/error-code?code=" + iFlyResponse.getCode() + "查询解决办法");
            }
        }

        ImageResponse imageResponse = getImageResponse(this.prompt, number, size);
        if (imageResponse == null) {
            throw new NullPointerException("Image response from \"buildeHttpBody\" method is null");
        }

        for (ImageResponse.Data curData : imageResponse.getData()) {
            resList.add(curData.getUrl());
        }

        return resList;
    }

    private String getDstResult(JsonElement jsonElement) {
        return jsonElement
                .getAsJsonObject().get("data")
                .getAsJsonObject().get("result")
                .getAsJsonObject().get("trans_result")
                .getAsJsonObject().get("dst").getAsString();
    }

    private IFlyResponse getFlyResponse(String prompt) throws Exception {
        String iFlyTekUrl = environment.getProperty("iflytek.url");
        String appId = environment.getProperty("iflytek.appId");
        String secret = environment.getProperty("iflytek.secret");
        String key = environment.getProperty("iflytek.key");

        String bodyStr = IFlyRequestBuilder.buildHttpBody(prompt, appId);
        Map<String, String> iFlyHeaderMap = IFlyRequestBuilder.buildHttpHeader(bodyStr, iFlyTekUrl, secret, key);

        Map<String, Object> resultMap = HttpUtil.doPost(iFlyTekUrl, iFlyHeaderMap, bodyStr, "iFly");

        String dstBodyStr = resultMap.get("body").toString();
        Gson gson = new Gson();

        this.prompt = getDstResult(gson.fromJson(dstBodyStr, JsonElement.class));

        return gson.fromJson(dstBodyStr, IFlyResponse.class);
    }

    private ImageResponse getImageResponse(String prompt, int number, String size) {
        String openAIUrl = environment.getProperty("openAI.url");
        String accessKey = environment.getProperty("openAI.key");
        Map<String, String> headerMap = OpenAIRequestBuilder.buildHttpHeader(accessKey);
        String bodyStr = OpenAIRequestBuilder.buildBody(prompt, number, size);

        Map<String, Object> resultMap = HttpUtil.doPost(openAIUrl, headerMap, bodyStr, "OpenAI");

        String data = (String) resultMap.get("data");
        return new Gson().fromJson(data, ImageResponse.class);
    }
}
