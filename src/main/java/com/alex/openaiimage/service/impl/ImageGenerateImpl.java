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

    @Override
    public List<String> getImageUrl(String prompt, int number, ImageSize imageSize, String srcLanguage) throws Exception {
        List<String> resList = new ArrayList<>(number);
        String size = imageSize.getSizeNum();
        String accessKey = environment.getProperty("openAI.key");
        String openAIImageUrl = environment.getProperty("openAI.url");

        // 输入内容为中文
        if (srcLanguage.equals("zh")) {
            String iFlyTekUrl = environment.getProperty("iflytek.url");

            String bodyStr = IFlyRequestBuilder.buildHttpBody(prompt);
            Map<String, String> iFlyHeader = IFlyRequestBuilder.buildHttpHeader(bodyStr);

            Map<String, Object> resultMap = HttpUtil.doPost(iFlyTekUrl, iFlyHeader, bodyStr);
            if (resultMap == null) {
                log.info("请求失败，doPost方法未能成功获取响应");
                throw new Exception("IFlyTek翻译接口请求失败");
            }

            String dstBodyStr = resultMap.get("body").toString();

            Gson gson = new Gson();
            IFlyResponse iFlyResponse = gson.fromJson(dstBodyStr, IFlyResponse.class);
            if (iFlyResponse.getCode() != 0) {
                log.warning("请前往https://www.xfyun.cn/document/error-code?code=" + iFlyResponse.getCode() + "查询解决办法");
            }

            prompt = getDstResult(gson.fromJson(dstBodyStr, JsonElement.class));
        }


        ImageResponse imageResponse = OpenAIRequestBuilder.buildHTTPBody(accessKey, openAIImageUrl, prompt, number, size);
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
}
