package org.xavier.star.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.entity.MaterialImg;

import java.util.List;

public interface MaterialImgService extends IService<MaterialImg> {
    String uploadImg(MultipartFile file, Long materialId);

    List<MaterialImg> getImg(Long materialId);
}
