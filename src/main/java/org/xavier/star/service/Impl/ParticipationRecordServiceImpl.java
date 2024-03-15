package org.xavier.star.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.star.entity.ParticipationRecord;
import org.xavier.star.entity.PreviousEvent;
import org.xavier.star.mapper.ParticipationRecordMapper;
import org.xavier.star.service.ParticipationRecordService;
import org.xavier.star.service.PreviousEventService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParticipationRecordServiceImpl extends ServiceImpl<ParticipationRecordMapper, ParticipationRecord> implements ParticipationRecordService {

    @Autowired
    private PreviousEventService previousEventService;

    @Override
    public List<PreviousEvent> getPreviousEvent(Long userId) {
        LambdaQueryWrapper<ParticipationRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ParticipationRecord::getUserId, userId);
        queryWrapper.orderByDesc(ParticipationRecord::getUpdateTime);
        List<ParticipationRecord> participationRecords = this.list(queryWrapper);
        List<PreviousEvent> previousEvents = new ArrayList<>();
        for(ParticipationRecord participationRecord : participationRecords) {
            Long previousEventId = participationRecord.getPreviousEventId();
            PreviousEvent previousEvent = previousEventService.getById(previousEventId);
            previousEvents.add(previousEvent);
        }
        return previousEvents;
    }
}
