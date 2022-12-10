package com.alex.openaiimage.service;

import com.alex.openaiimage.entity.ImageSize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageGenerate {

    /**
     *
     * @param prompt 目标图像的描述
     * @param number 生成目标图像的数量
     * @param imageSize 目标图像的大小
     * @param srcLanguage 描述的语言
     * @return 返回生成的目标图像对应的URL链接集合
     */
    List<String> getImageUrl(String prompt, int number, ImageSize imageSize, String srcLanguage) throws Exception;
}
