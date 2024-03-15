package org.xavier.star.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.entity.CurrentEventImg;
import org.xavier.star.entity.MaterialImg;

import java.util.List;

public interface CurrentEventImgService extends IService<CurrentEventImg> {
    String uploadImg(MultipartFile file, Long currentEventId);

    List<CurrentEventImg> getImg(Long currentEventId);
}
