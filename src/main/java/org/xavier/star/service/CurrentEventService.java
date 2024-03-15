package org.xavier.star.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.dto.CurrentEventWithImg;
import org.xavier.star.entity.CurrentEvent;

public interface CurrentEventService extends IService<CurrentEvent> {
    String uploadPoster(MultipartFile file, Long id);

    CurrentEventWithImg getByIdWithImg(Long id);
}
