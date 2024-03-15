package org.xavier.star.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xavier.star.entity.PurchaseRecord;

import java.util.List;

public interface PurchaseRecordService extends IService<PurchaseRecord> {
    List<PurchaseRecord> getPurchaseRecord(Long userId);
}
