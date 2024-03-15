package org.xavier.star.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xavier.star.common.CommonErrorCode;
import org.xavier.star.common.CommonException;
import org.xavier.star.dto.RegistrationForm;
import org.xavier.star.entity.CurrentEvent;
import org.xavier.star.entity.RegistrationRecord;
import org.xavier.star.entity.User;
import org.xavier.star.mapper.RegistrationRecordMapper;
import org.xavier.star.service.CurrentEventService;
import org.xavier.star.service.RegistrationRecordService;
import org.xavier.star.service.UserService;

import java.time.LocalDateTime;

import static org.xavier.star.common.CommonErrorCode.REGISTRATION_REPEAT;

@Service
public class RegistrationRecordServiceImpl extends ServiceImpl<RegistrationRecordMapper, RegistrationRecord> implements RegistrationRecordService {

    @Autowired
    UserService userService;

    @Autowired
    CurrentEventService currentEventService;

    @Override
    @Transactional
    public void registration(Long userId, Long currentEventId, RegistrationForm registrationForm) {
        if(userId == null || currentEventId == null || registrationForm == null
            || registrationForm.getUserName() == null || registrationForm.getWx() == null || registrationForm.getPhone() == null)
            throw new CommonException(CommonErrorCode.REGISTRATION_FORM_ERROR);

        //判断用户的身份信息
        User user = userService.getUserById(userId);
        if(user == null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        if(user.getIdentity() == 1) throw new CommonException(CommonErrorCode.STUDENT_REGISTRATION);
        //判断活动是否结束并更新活动状态
        LocalDateTime now = LocalDateTime.now();
        CurrentEvent currentEvent = currentEventService.getById(currentEventId);
        if(currentEvent == null) throw new CommonException(CommonErrorCode.CURRENT_EVENT_NOT_EXIST);
        if(currentEvent.getEndTime() != null && currentEvent.getEndTime().isBefore(now)) {
            currentEvent.setStatus(1);
            currentEventService.updateById(currentEvent);
        }
        //判断是否已报名
        LambdaQueryWrapper<RegistrationRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RegistrationRecord::getUserId, user)
                .eq(RegistrationRecord::getCurrentEventId, currentEventId);
        RegistrationRecord one = this.getOne(queryWrapper);
        if(one != null) throw new CommonException(CommonErrorCode.REGISTRATION_REPEAT);
        //判断活动状态
        if(currentEvent.getStatus() != 0) throw new CommonException(CommonErrorCode.REGISTRATION_END);
        //创建报名表单
        RegistrationRecord registrationRecord = RegistrationRecord.builder()
                .wx(registrationForm.getWx())
                .phone(registrationForm.getPhone())
                .userId(userId)
                .currentEventId(currentEventId)
                .userName(registrationForm.getUserName())
                .eventName(currentEvent.getName())
                .build();
        this.baseMapper.insert(registrationRecord);
    }

    @Override
    public Boolean cancel(Long userId, Long currentEventId) {
        if(userId == null || currentEventId == null) return false;
        LambdaQueryWrapper<RegistrationRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RegistrationRecord::getUserId, userId).eq(RegistrationRecord::getCurrentEventId, currentEventId);
        RegistrationRecord registrationRecord = this.getOne(lambdaQueryWrapper);
        if(registrationRecord == null) return false;
        this.baseMapper.deleteById(registrationRecord.getId());
        return true;
    }
}
