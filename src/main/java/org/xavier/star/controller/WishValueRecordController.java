package org.xavier.star.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xavier.star.annotation.Auth;
import org.xavier.star.common.CommonException;
import org.xavier.star.common.Result;
import org.xavier.star.entity.WishValueRecord;
import org.xavier.star.service.WishValueRecordService;

import java.util.List;

@Api(tags = "心愿值获取记录接口")
@RestController
@RequestMapping("/wishValue")
@Slf4j
public class WishValueRecordController {

    @Autowired
    private WishValueRecordService wishValueRecordService;

    @Auth
    @GetMapping
    @ApiOperation("根据用户id获取心愿值获取记录")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "Long")
    public Result<List<WishValueRecord>> getWishValueRecordByUserId(Long userId) {
        try {
            LambdaQueryWrapper<WishValueRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WishValueRecord::getUserId, userId);
            queryWrapper.orderByDesc(WishValueRecord::getUpdateTime);
            List<WishValueRecord> wishValueRecords = wishValueRecordService.list(queryWrapper);
            return Result.success(wishValueRecords);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }
}
