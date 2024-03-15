package org.xavier.star.service.Impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.common.CommonConstants;
import org.xavier.star.common.CommonErrorCode;
import org.xavier.star.common.CommonException;
import org.xavier.star.entity.*;
import org.xavier.star.mapper.PreviousEventMapper;
import org.xavier.star.service.*;
import org.xavier.star.util.WeChatCrawlerUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.xavier.star.common.CommonConstants.EVENT_FILE_PATH;
import static org.xavier.star.common.CommonConstants.USER_FILE_PATH;

@Service
public class PreviousEventServiceImpl extends ServiceImpl<PreviousEventMapper, PreviousEvent> implements PreviousEventService {

    @Autowired
    private RegistrationRecordService registrationRecordService;

    @Autowired
    private UserService userService;

    @Autowired
    private CurrentEventService currentEventService;

    @Autowired
    private WishValueRecordService wishValueRecordService;

    @Autowired
    private ParticipationRecordService participationRecordService;

    @Override
    @Transactional
    public boolean insertPreviousEvent(String url, Long currentEventId) {
        if(this.getOne(new LambdaQueryWrapper<PreviousEvent>().eq(PreviousEvent::getUrl, url)) != null)
            throw new CommonException(CommonErrorCode.ARTICLE_ALREADY_EXIST);
        try {
            PreviousEvent previousEvent = PreviousEvent.builder()
                    .url(url)
                    .cover(WeChatCrawlerUtil.getCoverUrl(url))
                    .title(WeChatCrawlerUtil.getTitle(url))
                    .realUrl(WeChatCrawlerUtil.getTrueUrl(url))
                    .build();

            this.save(previousEvent);
            if(currentEventId != null) {
                //为近期活动所有参与用户发放心愿值
                CurrentEvent currentEvent = currentEventService.getById(currentEventId);
                LambdaQueryWrapper<RegistrationRecord> recordLambdaQueryWrapper = new LambdaQueryWrapper<>();
                recordLambdaQueryWrapper.eq(RegistrationRecord::getCurrentEventId, currentEventId);
                List<RegistrationRecord> registrationRecordList = registrationRecordService.list(recordLambdaQueryWrapper);
                for(RegistrationRecord registrationRecord : registrationRecordList) {
                    Long userId = registrationRecord.getUserId();
                    User user = userService.getUserById(userId);
                    user.setWishValue(user.getWishValue() + currentEvent.getWishValue());
                    user.setFromParticipation(user.getFromParticipation() + currentEvent.getWishValue());
                    user.setParticipationTimes(user.getParticipationTimes() + 1);
                    userService.updateById(user);
                    //创建心愿值获取记录
                    WishValueRecord wishValueRecord = WishValueRecord.builder()
                            .userId(userId)
                            .numbers(currentEvent.getWishValue())
                            .access("参与活动：" + currentEvent.getName())
                            .build();
                    wishValueRecordService.save(wishValueRecord);
                    //创建参与用户与往期活动关联关系
                    ParticipationRecord participationRecord = ParticipationRecord.builder()
                            .userId(userId)
                            .previousEventId(previousEvent.getId())
                            .build();
                    participationRecordService.save(participationRecord);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void uploadImg(MultipartFile file, Long id) {
        PreviousEvent previousEvent = this.getById(id);
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = EVENT_FILE_PATH + flag + "-" + originalFilename;
        try {
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
        } catch (IOException e) {
            throw new CommonException(CommonErrorCode.READ_FILE_ERROR);
        }
        String link = CommonConstants.DOWNLOAD_PATH + rootFilePath;
        previousEvent.setImage(link);
        if (!this.updateById(previousEvent)) throw new CommonException(CommonErrorCode.UPDATE_FAIL);
    }
}
