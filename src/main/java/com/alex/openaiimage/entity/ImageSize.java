package com.alex.openaiimage.entity;

import lombok.Getter;

@Getter
public enum ImageSize {
    LARGE("1024x1024"),
    MEDIUM("512x512"),
    SMALL("256x256");

    private final String sizeNum;

    ImageSize(String sizeNum) {
        this.sizeNum = sizeNum;
    }
}
