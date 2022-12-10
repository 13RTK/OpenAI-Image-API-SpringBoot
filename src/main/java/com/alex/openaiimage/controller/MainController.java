package com.alex.openaiimage.controller;

import com.alex.openaiimage.entity.ImageSize;
import com.alex.openaiimage.entity.Response;
import com.alex.openaiimage.entity.UrlResponse;
import com.alex.openaiimage.service.ImageGenerate;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log
public class MainController {

    @Resource
    ImageGenerate imageGenerate;

    @PostMapping("/get-image")
    public Response getImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String prompt = request.getParameter("prompt");
        int number = Integer.parseInt(request.getParameter("n"));
        String size = request.getParameter("size");
        String srcLanguage = request.getParameter("from");

        if (StringUtils.isAnyBlank(prompt, size, request.getParameter("n"))) {
            log.warning("Parameter is null!\n" +
                    "Request from " + request.getRequestURI());
        }

        ImageSize imageSize;

        switch (size) {
            case "256x256" -> imageSize = ImageSize.SMALL;
            case "512x512" -> imageSize = ImageSize.MEDIUM;
            case "1024x1024" -> imageSize = ImageSize.LARGE;
            default -> {
                response.setStatus(400);
                log.info("Size number not matched!");
                return new Response(null, "Size must be specified number!");
            }
        }

        List<String> imageUrl = imageGenerate.getImageUrl(prompt, number, imageSize, srcLanguage);
        UrlResponse urlResponse = new UrlResponse();

        for (String s : imageUrl) {
            urlResponse.addIntoList(s);
        }

        response.setStatus(200);
        return new Response(urlResponse, "OK");
    }
}
