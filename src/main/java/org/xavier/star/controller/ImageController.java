package org.xavier.star.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.annotation.Admin;
import org.xavier.star.common.CommonException;
import org.xavier.star.common.Result;
import org.xavier.star.entity.Image;
import org.xavier.star.service.ImageService;

@Api(tags = "图片存储相关接口")
@RestController
@RequestMapping("/image")
@Slf4j
public class ImageController {

    @Autowired
    ImageService imageService;

    @GetMapping
    public Result<Image> getImage() {
        return Result.success(imageService.getById(1));
    }

    @Admin
    @PostMapping
    public Result<String> upload(MultipartFile file) {
        try {
            imageService.upload(file);
            return Result.success("上传图片成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }
}
