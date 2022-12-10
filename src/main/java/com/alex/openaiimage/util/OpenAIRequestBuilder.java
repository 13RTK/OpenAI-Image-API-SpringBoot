package com.alex.openaiimage.util;

import com.alex.openaiimage.entity.ImageResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.extern.java.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Log
public class OpenAIRequestBuilder {
    public static ImageResponse buildHTTPBody(String accessKey, String openAIImageUrl, String prompt, int number, String size) throws IOException {
        URL url = new URL(openAIImageUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 设置请求头信息
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", accessKey);

        // 设置POST请求的参数
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        String data = "{\"prompt\":\"" + prompt + "\"," +
                "\"n\":" + number + "," +
                "\"size\":\"" + size + "\"}";
        OutputStream os = conn.getOutputStream();
        os.write(data.getBytes());
        os.flush();
        os.close();

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            System.out.println("Http 请求失败，状态码：" + conn.getResponseCode() + "，错误信息：" + br.readLine());
            return null;
        }

        // 获取服务器的响应
        InputStream is = conn.getInputStream();
        return getResponseObjFromServer(is);
    }

    /**
     * 将服务器的响应流读取为字符串之后，再转换为我们的图片响应对象
     *
     * @param inputStream 服务器返回的输入流
     * @return 返回ImageResponse对象
     */
    private static ImageResponse getResponseObjFromServer(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(builder.toString(), JsonElement.class);

        log.info("OpenAI服务器返回的JSON格式内容: " + jsonElement.toString());

        return gson.fromJson(jsonElement, ImageResponse.class);
    }
}
