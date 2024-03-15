package org.xavier.star.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xavier.star.entity.WishValueRecord;
import org.xavier.star.mapper.WishValueRecordMapper;
import org.xavier.star.service.WishValueRecordService;

@Service
public class WishValueRecordServiceImpl extends ServiceImpl<WishValueRecordMapper, WishValueRecord> implements WishValueRecordService {
}
