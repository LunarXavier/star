package org.xavier.star.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.dto.DonateRecord;
import org.xavier.star.dto.PurchaseForm;
import org.xavier.star.entity.Goods;
import org.xavier.star.entity.PurchaseRecord;

import java.util.List;

public interface GoodsService extends IService<Goods> {
    List<Goods> listGoods(Integer type);

    boolean deleteById(Long id);

    void purchase(PurchaseForm purchaseForm, Long userId);

    String uploadImg(MultipartFile file, Long id);

    List<DonateRecord> getDonateRecord(Long goodsId);
}
