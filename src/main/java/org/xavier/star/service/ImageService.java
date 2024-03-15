package org.xavier.star.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.entity.Image;

public interface ImageService extends IService<Image> {
    void upload(MultipartFile file);
}
