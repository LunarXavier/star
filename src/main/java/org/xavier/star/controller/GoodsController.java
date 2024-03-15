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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.annotation.Admin;
import org.xavier.star.annotation.Auth;
import org.xavier.star.common.CommonException;
import org.xavier.star.common.Result;
import org.xavier.star.dto.DonateRecord;
import org.xavier.star.dto.PurchaseForm;
import org.xavier.star.entity.Goods;
import org.xavier.star.service.GoodsService;

import java.util.List;

@Api(tags = "商品相关接口")
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @Auth
    @GetMapping("/list/{type}")
    @ApiOperation("根据商品种类获取商品")
    @ApiImplicitParam(name = "type", value = "商品种类", required = true, paramType = "path", dataType = "Integer")
    public Result<List<Goods>> listGoods(@PathVariable Integer type) {
        if(type != 0 && type != 1) return null;
        try {
            return Result.success(goodsService.listGoods(type));
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @PostMapping
    @ApiOperation("新增商品")
    @ApiImplicitParam(name = "goods", value = "商品", required = true, paramType = "body", dataTypeClass = Goods.class)
    public Result<String> insert(@RequestBody Goods goods) {
        try {
            if(goodsService.save(goods)) return Result.success("新增商品成功");
            return Result.fail("新增商品失败");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @DeleteMapping
    @ApiOperation("根据id删除商品信息")
    @ApiImplicitParam(name = "id", value = "商品id", required = true, paramType = "query", dataType = "Long")
    public Result<String> delete(Long id) {
        try {
            if(goodsService.deleteById(id))
                return Result.success("删除商品成功");
            return Result.fail("删除商品失败");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @PutMapping
    @ApiOperation("根据id更新商品信息")
    @ApiImplicitParam(name = "goods", value = "商品", required = true, paramType = "body", dataTypeClass = Goods.class)
    public Result<String> update(@RequestBody Goods goods) {
        try {
            if(goodsService.updateById(goods)) return Result.success("修改商品成功");
            return Result.fail("修改商品失败");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @GetMapping("/page")
    @ApiOperation(value = "分页查询所有商品，允许模糊查询商品名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize" ,value = "每页显示数量（不小于0）", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pageNum", value = "页数（不小于0）", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "商品名称", required = false, paramType = "query", dataType = "String")
    })
    public Result<Page<Goods>> page(@Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum, String name){
        try {
            //构造分页构造器
            Page<Goods> pageInfo = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(Goods::getName, name);
            queryWrapper.orderByDesc(Goods::getUpdateTime);
            //执行查询
            goodsService.page(pageInfo, queryWrapper);
            return Result.success(pageInfo);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PostMapping("/purchase")
    @ApiOperation("购买商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "purchaseForm", value = "购买表单", required = true, paramType = "body", dataTypeClass = PurchaseForm.class),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "Long"),
    })
    public Result<String> purchase(@RequestBody PurchaseForm purchaseForm, Long userId) {
        try {
            goodsService.purchase(purchaseForm, userId);
            return Result.success("商品购买成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @PostMapping(value = "/upload", produces = "application/json")
    @ApiOperation(value = "上传商品图片")
    @ApiImplicitParam(name = "id", value = "商品id", required = true, paramType = "query", dataType = "Long")
    public Result<String> uploadPoster(MultipartFile file, Long id) {
        try{
            return Result.success(goodsService.uploadImg(file, id));
        }
        catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @GetMapping("/donate")
    @ApiOperation("获取指定商品的捐赠人信息，用于显示感谢名单")
    @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, paramType = "query", dataType = "Long")
    public Result<List<DonateRecord>> getDonateRecord(Long goodsId) {
        try{
            return Result.success(goodsService.getDonateRecord(goodsId));
        }
        catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @GetMapping("/{id}")
    @ApiOperation("根据商品id获取商品信息")
    @ApiImplicitParam(name = "id", value = "商品id", required = true, paramType = "path", dataType = "Long")
    public Result<Goods> getById(@PathVariable Long id) {
        try{
            return Result.success(goodsService.getById(id));
        }
        catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }
}
