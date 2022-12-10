package com.alex.openaiimage.entity;

import lombok.Data;

@Data
public class IFlyResponse {
    private int code;
    private String message;
    private String sid;
    private Object data;
}
