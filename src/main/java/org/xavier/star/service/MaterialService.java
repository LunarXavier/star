package org.xavier.star.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.entity.Material;

public interface MaterialService extends IService<Material> {
    boolean deleteById(Long id);

    String uploadCover(MultipartFile file, Long id);

    Integer addDownloadTimes(Long id);
}
