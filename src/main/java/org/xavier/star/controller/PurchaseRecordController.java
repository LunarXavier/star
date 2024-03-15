package org.xavier.star.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xavier.star.annotation.Admin;
import org.xavier.star.annotation.Auth;
import org.xavier.star.common.CommonException;
import org.xavier.star.common.Result;
import org.xavier.star.entity.PurchaseRecord;
import org.xavier.star.service.PurchaseRecordService;

import java.util.List;

@Slf4j
@Api(tags = "购买记录接口")
@RestController
@RequestMapping("/purchase")
public class PurchaseRecordController {

    @Autowired
    PurchaseRecordService purchaseRecordService;

    @Admin
    @GetMapping("/page")
    @ApiOperation(value = "分页查询所有购买记录，允许模糊查询商品名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize" ,value = "每页显示数量（不小于0）", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pageNum", value = "页数（不小于0）", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "商品名称", required = false, paramType = "query", dataType = "String")
    })
    public Result<Page<PurchaseRecord>> page(@Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum, String name){
        try {
            //构造分页构造器
            Page<PurchaseRecord> pageInfo = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<PurchaseRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(PurchaseRecord::getGoodsName, name);
            queryWrapper.orderByDesc(PurchaseRecord::getUpdateTime);
            //执行查询
            purchaseRecordService.page(pageInfo, queryWrapper);
            return Result.success(pageInfo);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @DeleteMapping
    @ApiOperation("根据id删除购买记录")
    @ApiImplicitParam(name = "id", value = "购买记录id", required = true, paramType = "query", dataType = "Long")
    public Result<String> delete(Long id) {
        try {
            purchaseRecordService.removeById(id);
            return Result.success("删除购买记录成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @GetMapping
    @ApiOperation("获取指定用户的购买记录")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "Long")
    public Result<List<PurchaseRecord>> getPurchaseRecord(Long userId) {
        try {
            return Result.success(purchaseRecordService.getPurchaseRecord(userId));
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }
}
