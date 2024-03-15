package org.xavier.star.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;
import org.xavier.star.dto.RegistrationForm;
import org.xavier.star.entity.RegistrationRecord;

public interface RegistrationRecordService extends IService<RegistrationRecord> {

    void registration(Long userId, Long currentEventId, RegistrationForm registrationForm);

    Boolean cancel(Long userId, Long currentEventId);
}
