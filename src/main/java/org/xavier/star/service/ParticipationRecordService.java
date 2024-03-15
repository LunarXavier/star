package org.xavier.star.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xavier.star.entity.ParticipationRecord;
import org.xavier.star.entity.PreviousEvent;

import java.util.List;

public interface ParticipationRecordService extends IService<ParticipationRecord> {
    List<PreviousEvent>  getPreviousEvent(Long userId);
}
