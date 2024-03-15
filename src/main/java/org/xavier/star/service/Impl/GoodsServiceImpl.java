package org.xavier.star.service.Impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.cos.exception.CRC32MismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.common.CommonConstants;
import org.xavier.star.common.CommonErrorCode;
import org.xavier.star.common.CommonException;
import org.xavier.star.dto.DonateRecord;
import org.xavier.star.dto.PurchaseForm;
import org.xavier.star.entity.Goods;
import org.xavier.star.entity.Material;
import org.xavier.star.entity.PurchaseRecord;
import org.xavier.star.entity.User;
import org.xavier.star.mapper.GoodsMapper;
import org.xavier.star.service.GoodsService;
import org.xavier.star.service.PurchaseRecordService;
import org.xavier.star.service.UserService;

import java.awt.color.CMMException;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static org.xavier.star.common.CommonConstants.GOODS_FILE_PATH;
import static org.xavier.star.common.CommonConstants.MATERIAL_FILE_PATH;
import static org.xavier.star.common.CommonErrorCode.*;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    PurchaseRecordService purchaseRecordService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    UserService userService;

    @Override
    public List<Goods> listGoods(Integer type) {
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goods::getType, type);
        queryWrapper.orderByDesc(Goods::getUpdateTime);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if(id == null) return false;
        this.removeById(id);
        LambdaQueryWrapper<PurchaseRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchaseRecord::getGoodsId, id);
        purchaseRecordService.remove(queryWrapper);
        return true;
    }

    @Override
    @Transactional
    public void purchase(PurchaseForm purchaseForm, Long userId) {
        if(purchaseForm == null || purchaseForm.getGoodsId() == null
            || purchaseForm.getWx() == null || purchaseForm.getMessage() == null
            || purchaseForm.getNumbers() == null || purchaseForm.getNumbers() <= 0)
            throw new CommonException(PURCHASE_FORM_ERROR);
        Long goodsId = purchaseForm.getGoodsId();
        Goods goods = this.getById(purchaseForm.getGoodsId());
        if(goods == null) throw new CommonException(GOODS_ERROR);
        Integer limitation = goods.getLimitation();
        if(limitation == null) throw new CommonException(GOODS_ERROR);
        //检查商品购买限制
        if(limitation == 0 || limitation == 1) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            LambdaQueryWrapper<PurchaseRecord> purchaseRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
            purchaseRecordLambdaQueryWrapper.eq(PurchaseRecord::getUserId, userId)
                    .ge(PurchaseRecord::getCreateTime, startOfWeek)
                    .le(PurchaseRecord::getCreateTime, endOfWeek)
                    .eq(PurchaseRecord::getGoodsId, goodsId);
            List<PurchaseRecord> purchaseRecordList = purchaseRecordService.list(purchaseRecordLambdaQueryWrapper);
            //每周一次
            if(limitation == 0)
                if(purchaseRecordList != null && !purchaseRecordList.isEmpty() || purchaseForm.getNumbers() > 1)
                    throw new CommonException(PURCHASE_LIMITATION);
            //每周3次
            else if(limitation == 1) {
                if(purchaseForm.getNumbers() > 3)
                    throw new CommonException(PURCHASE_LIMITATION);
                if(purchaseRecordList != null) {
                    Integer cnt = purchaseForm.getNumbers();
                    for(PurchaseRecord purchaseRecord : purchaseRecordList) {
                        if(purchaseRecord.getNumbers() != null)
                            cnt += purchaseRecord.getNumbers();
                        if(cnt > 3)
                            throw new CommonException(PURCHASE_LIMITATION);
                    }
                }
            }
        }

        User user = userService.getUserById(userId);
        if(user == null)
            throw new CommonException(USER_NOT_EXIST);
        if(user.getIdentity() == 1)
            throw new CommonException(STUDENT_PURCHASE);
        if(goods.getType() == 1 && goods.getDonationProcess() == 100) {
            throw new CommonException(DONATION_PROCESS_ENOUGH);
        }
        //减少用户的心愿值
        if(user.getWishValue() - purchaseForm.getNumbers()*goods.getValue() < 0) {
            throw new CommonException(WISH_VALUE_NOT_ENOUGH);
        }
        user.setWishValue(user.getWishValue() - purchaseForm.getNumbers()*goods.getValue());
        userService.updateById(user);
        //创建商品购买记录
        PurchaseRecord purchaseRecord = PurchaseRecord.builder()
                .userId(userId)
                .goodsId(goodsId)
                .buyerName(user.getNickname())
                .goodsName(goods.getName())
                .type(goods.getType())
                .numbers(purchaseForm.getNumbers())
                .cost(purchaseForm.getNumbers()*goods.getValue())
                .wx(purchaseForm.getWx())
                .build();
        if(purchaseForm.getMessage() != null) purchaseRecord.setMessage(purchaseForm.getMessage());
        purchaseRecordService.save(purchaseRecord);
        //购买人数加1
        if(goods.getType() == 1) goods.setDonationProcess(goods.getDonationProcess() + purchaseForm.getNumbers());
        goods.setBuyersCnt(goods.getBuyersCnt() + 1);
        this.updateById(goods);
    }

    @Override
    public String uploadImg(MultipartFile file, Long id) {
        Goods goods = this.getById(id);
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = GOODS_FILE_PATH + flag + "-" + originalFilename;
        try {
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
        } catch (IOException e) {
            throw new CommonException(CommonErrorCode.READ_FILE_ERROR);
        }
        String link = CommonConstants.DOWNLOAD_PATH + rootFilePath;
        goods.setImg(link);
        if (!this.updateById(goods)) throw new CommonException(CommonErrorCode.UPDATE_FAIL);
        return link;
    }

    @Override
    public List<DonateRecord> getDonateRecord(Long goodsId) {
        LambdaQueryWrapper<PurchaseRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchaseRecord::getGoodsId, goodsId);
        queryWrapper.orderByDesc(PurchaseRecord::getCreateTime);
        List<PurchaseRecord> purchaseRecordList = purchaseRecordService.list(queryWrapper);
        List<DonateRecord> donateRecords = new ArrayList<>();
        for(PurchaseRecord purchaseRecord : purchaseRecordList) {
            Long userId = purchaseRecord.getUserId();
            User user = userService.getById(userId);
            DonateRecord donateRecord = DonateRecord.builder()
                    .userName(user.getNickname())
                    .portrait(user.getPortrait())
                    .message(purchaseRecord.getMessage())
                    .build();
            donateRecords.add(donateRecord);
        }
        return donateRecords;
    }
}
