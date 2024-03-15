package org.xavier.star.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.entity.PreviousEvent;

import java.time.LocalDateTime;

public interface PreviousEventService extends IService<PreviousEvent> {
    boolean insertPreviousEvent(String url, Long currentEventId);

    void uploadImg(MultipartFile file, Long id);
}
