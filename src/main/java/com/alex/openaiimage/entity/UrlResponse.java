package com.alex.openaiimage.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UrlResponse {
    private final List<String> urlList = new ArrayList<>();

    public void addIntoList(String urlStr) {
        urlList.add(urlStr);
    }
}
