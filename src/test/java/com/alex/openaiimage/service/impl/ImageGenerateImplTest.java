package com.alex.openaiimage.service.impl;

import com.alex.openaiimage.service.ImageGenerate;
import com.alex.openaiimage.service.ImageSize;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ImageGenerateImplTest {
    @Resource
    ImageGenerate imageGenerate;

    @Test
    void getImageUrlTest() throws IOException {
        String prompt = "A beautiful young lady with long hair looks like a k-pop star";
        int num = 2;
        ImageSize imageSize = ImageSize.SMALL;

        List<String> imageUrl = imageGenerate.getImageUrl(prompt, num, imageSize);
        imageUrl.forEach(System.out::println);
    }
}