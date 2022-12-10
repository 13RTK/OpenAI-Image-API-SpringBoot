package com.alex.openaiimage.util;

import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Http Client 工具类
 *
 * @author alex
 */
@Log
public class HttpUtil {
    /**
     * 发送post请求，根据 Content-Type 返回不同的返回值
     *
     * @param url    请求的URL地址
     * @param header 需要设置的所有请求头信息对应的Map
     * @param body   接收buildHttpBody方法中返回的body字符串
     * @return 将API请求结果（Content-Type和body）作为Map返回
     */
    public static Map<String, Object> doPost(String url, Map<String, String> header, String body, String apiType) {
        Map<String, Object> resultMap = new HashMap<>();
        PrintWriter out;
        try {
            // 设置 url，并获取连接实例
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            // 从header映射中获取对应的请求头，并加载到连接实例中去
            for (String key : header.keySet()) {
                httpURLConnection.setRequestProperty(key, header.get(key));
            }
            // 设置请求 body
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            out = new PrintWriter(httpURLConnection.getOutputStream());
            // 保存body
            out.print(body);
            // 发送body
            out.flush();

            // 如果请求失败，则返回错误信息
            if (HttpURLConnection.HTTP_OK != httpURLConnection.getResponseCode()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
                log.info("Http 请求失败，状态码：" + httpURLConnection.getResponseCode() + "，错误信息：" + br.readLine());
                return Collections.emptyMap();
            }

            // 设置请求 body
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            resultMap.put("Content-Type", "text/plain");

            if (apiType.equals("iFly")) {
                resultMap.put("body", result.toString());
                log.info("响应内容为: " + resultMap.get("body"));
            } else {
                resultMap.put("data", result.toString());
                log.info("响应内容为: " + resultMap.get("data"));
            }


            return resultMap;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }
}