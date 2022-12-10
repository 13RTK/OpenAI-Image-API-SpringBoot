package com.alex.openaiimage.service.impl;

import com.alex.openaiimage.entity.ImageSize;
import com.alex.openaiimage.service.ImageGenerate;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
/*
  测试图片获取的实现类
 */
class ImageGenerateImplTest {
    @Resource
    ImageGenerate imageGenerate;

    @Test
    void getImageUrlTestZh() throws Exception {
        String prompt = "一个长得像kpop明星的长头发韩国年轻女性，且她的脸比较小";
        int num = 2;
        ImageSize imageSize = ImageSize.SMALL;
        String srcLanguage = "zh";

        List<String> imageUrl = imageGenerate.getImageUrl(prompt, num, imageSize, srcLanguage);
        System.out.println("中文测试输出如下：");
        imageUrl.forEach(System.out::println);

        Assertions.assertTrue(imageUrl.size() > 0);
    }

    @Test
    void getImageUrlTestEn() throws Exception {
        String prompt = "A beautiful korean young lady with long hair looks like a k-pop star, she has thin face.";
        int num = 1;
        ImageSize imageSize = ImageSize.SMALL;
        String srcLanguage = "en";

        List<String> imageUrl = imageGenerate.getImageUrl(prompt, num, imageSize, srcLanguage);
        System.out.println("英文测试输出如下：");
        imageUrl.forEach(System.out::println);

        Assertions.assertTrue(imageUrl.size() > 0);
    }

//    @Test
//    void yamlParseTest() throws FileNotFoundException {
//        Yaml yaml = new Yaml();
//        InputStream resourceAsStream = ClassLoader.getSystemResourceAsStream("application.yaml");
//        System.out.println(resourceAsStream);
//
//        YamlConfiguration yamlConfiguration = yaml.loadAs(resourceAsStream, YamlConfiguration.class);
//    }
}