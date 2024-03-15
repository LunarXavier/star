package org.xavier.star.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xavier.star.entity.TimePoint;
import org.xavier.star.mapper.TimePointMapper;
import org.xavier.star.service.TimePointService;

@Service
public class TimePointServiceImpl extends ServiceImpl<TimePointMapper, TimePoint> implements TimePointService {
}
