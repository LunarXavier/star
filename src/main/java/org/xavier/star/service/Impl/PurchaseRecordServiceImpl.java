package org.xavier.star.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.star.common.CommonErrorCode;
import org.xavier.star.common.CommonException;
import org.xavier.star.entity.PurchaseRecord;
import org.xavier.star.mapper.PurchaseRecordMapper;
import org.xavier.star.service.PurchaseRecordService;

import java.util.List;

@Service
public class PurchaseRecordServiceImpl extends ServiceImpl<PurchaseRecordMapper, PurchaseRecord> implements PurchaseRecordService {

    @Override
    public List<PurchaseRecord> getPurchaseRecord(Long userId) {
        if(userId == null) throw new CommonException(CommonErrorCode.USER_ID_EMPTY);
        LambdaQueryWrapper<PurchaseRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchaseRecord::getUserId, userId);
        List<PurchaseRecord> purchaseRecordList = this.list(queryWrapper);
        return purchaseRecordList;
    }
}
