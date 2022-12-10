package com.alex.openaiimage.entity;

import lombok.Data;

@Data
public class Response {
    UrlResponse urlResponse;
    String errorMessage;

    public Response(UrlResponse urlResponse, String errorMessage) {
        this.urlResponse = urlResponse;
        this.errorMessage = errorMessage;
    }
}
