package org.xavier.star.service.Impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.common.CommonConstants;
import org.xavier.star.common.CommonErrorCode;
import org.xavier.star.common.CommonException;
import org.xavier.star.dto.CurrentEventWithImg;
import org.xavier.star.entity.CurrentEvent;
import org.xavier.star.entity.CurrentEventImg;
import org.xavier.star.entity.User;
import org.xavier.star.mapper.CurrentEventMapper;
import org.xavier.star.service.CurrentEventImgService;
import org.xavier.star.service.CurrentEventService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.xavier.star.common.CommonConstants.EVENT_FILE_PATH;
import static org.xavier.star.common.CommonConstants.USER_FILE_PATH;

@Service
public class CurrentEventServiceImpl extends ServiceImpl<CurrentEventMapper, CurrentEvent> implements CurrentEventService {

    @Autowired
    CurrentEventImgService currentEventImgService;

    @Override
    public String uploadPoster(MultipartFile file, Long id) {
        CurrentEvent currentEvent = this.getById(id);
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = EVENT_FILE_PATH + flag + "-" + originalFilename;
        try {
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
        } catch (IOException e) {
            throw new CommonException(CommonErrorCode.READ_FILE_ERROR);
        }
        String link = CommonConstants.DOWNLOAD_PATH + rootFilePath;
        currentEvent.setPoster(link);
        if (!this.updateById(currentEvent)) throw new CommonException(CommonErrorCode.UPDATE_FAIL);
        return link;
    }

    @Override
    public CurrentEventWithImg getByIdWithImg(Long id) {
        if(id == null) throw new CommonException(CommonErrorCode.CURRENT_EVENT_ID_EMPTY);
        CurrentEvent currentEvent = this.getById(id);
        if(currentEvent == null) throw new CommonException(CommonErrorCode.CURRENT_EVENT_NOT_EXIST);
        LocalDateTime now = LocalDateTime.now();
        if(currentEvent.getStatus() == 0 && currentEvent.getEndTime() != null && currentEvent.getEndTime().isBefore(now)) {
            currentEvent.setStatus(1);
            this.baseMapper.updateById(currentEvent);
        }
        LambdaQueryWrapper<CurrentEventImg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CurrentEventImg::getCurrentEventId, id);
        queryWrapper.orderByAsc(CurrentEventImg::getCreateTime);
        List<CurrentEventImg> currentEventImgList = currentEventImgService.list(queryWrapper);
        CurrentEventWithImg currentEventWithImg = CurrentEventWithImg.builder()
                .currentEvent(currentEvent)
                .currentEventImgList(currentEventImgList)
                .build();
        return currentEventWithImg;
    }
}
