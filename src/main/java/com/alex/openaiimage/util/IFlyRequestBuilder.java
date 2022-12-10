package com.alex.openaiimage.util;

import com.alex.openaiimage.entity.YamlConfiguration;
import com.google.gson.JsonObject;
import org.yaml.snakeyaml.Yaml;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class IFlyRequestBuilder {
    private IFlyRequestBuilder() {
    }

    private static String WebOTS_URL;
    // 应用ID（到控制台获取）
    private static String APPID;
    // 接口APISecret（到控制台机器翻译服务页面获取）
    private static String API_SECRET;
    // 接口APIKey（到控制台机器翻译服务页面获取）
    private static String API_KEY;

    // 翻译语种
    private static final String FROM = "cn";
    // 目标语种
    private static final String TO = "en";

    static {
        Yaml yaml = new Yaml();
        YamlConfiguration yamlConfiguration = yaml.loadAs(ClassLoader.getSystemResourceAsStream("application.yaml"), YamlConfiguration.class);

        WebOTS_URL = yamlConfiguration.getIflytek().getUrl();
        APPID = yamlConfiguration.getIflytek().getAppId();
        API_SECRET = yamlConfiguration.getIflytek().getSecret();
        API_KEY = yamlConfiguration.getIflytek().getKey();
    }

    /**
     * 组装http请求体
     */
    public static String buildHttpBody(String text) {
        JsonObject body = new JsonObject();
        JsonObject business = new JsonObject();
        JsonObject common = new JsonObject();
        JsonObject data = new JsonObject();
        //填充common
        common.addProperty("app_id", APPID);
        //填充business
        business.addProperty("from", FROM);
        business.addProperty("to", TO);
        //填充data
        byte[] textByte = text.getBytes(StandardCharsets.UTF_8);
        String textBase64 = Base64.getEncoder().encodeToString(textByte);
        data.addProperty("text", textBase64);
        //填充body
        body.add("common", common);
        body.add("business", business);
        body.add("data", data);
        return body.toString();
    }

    /**
     * 组装http请求头
     */
    public static Map<String, String> buildHttpHeader(String body) throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException {
        Map<String, String> header = new HashMap<>();
        URL url = new URL(WebOTS_URL);

        //时间戳
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dateD = new Date();
        String date = format.format(dateD);

        //对body进行sha256签名,生成digest头部，POST请求必须对body验证
        String digestBase64 = "SHA-256=" + signBody(body);

        //hmacsha256加密原始字符串
        String builder = "host: " + url.getHost() + "\n" +
                //
                "date: " + date + "\n" +
                //
                "POST " + url.getPath() + " HTTP/1.1" + "\n" +
                //
                "digest: " + digestBase64;
        String sha = hmacsign(builder, API_SECRET);

        //组装authorization
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", API_KEY, "hmac-sha256", "host date request-line digest", sha);

        header.put("Authorization", authorization);
        header.put("Content-Type", "application/json");
        header.put("Accept", "application/json,version=1.0");
        header.put("Host", url.getHost());
        header.put("Date", date);
        header.put("Digest", digestBase64);
        return header;
    }

    /**
     * 对body进行SHA-256加密
     */
    private static String signBody(String body) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(body.getBytes(StandardCharsets.UTF_8));
            encodestr = Base64.getEncoder().encodeToString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    /**
     * hmacsha256加密
     */
    private static String hmacsign(String signature, String apiSecret) throws NoSuchAlgorithmException, InvalidKeyException {
        Charset charset = StandardCharsets.UTF_8;
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(signature.getBytes(charset));
        return Base64.getEncoder().encodeToString(hexDigits);
    }
}
