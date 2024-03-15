package org.xavier.star.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xavier.star.annotation.Admin;
import org.xavier.star.annotation.Auth;
import org.xavier.star.common.CommonException;
import org.xavier.star.common.Result;
import org.xavier.star.dto.RegistrationForm;
import org.xavier.star.entity.CurrentEvent;
import org.xavier.star.entity.RegistrationRecord;
import org.xavier.star.service.RegistrationRecordService;

@Slf4j
@Api(tags = "报名记录接口")
@RestController
@RequestMapping("/registration")
public class RegistrationRecordController {

    @Autowired
    private RegistrationRecordService registrationRecordService;

    @Auth
    @PostMapping
    @ApiOperation("报名活动")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "userId", name = "用户id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(value = "currentEventId", name = "近期活动id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(value = "RegistrationForm", name = "报名表单", required = true, paramType = "body", dataTypeClass = RegistrationRecord.class)
    })
    public Result<String> register(Long userId, Long currentEventId, @RequestBody RegistrationForm registrationForm) {
        try {
            registrationRecordService.registration(userId, currentEventId, registrationForm);
            return Result.success("报名成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @GetMapping
    @ApiOperation("判断用户是否已报名, 1表示已报名, 0表示未报名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "currentEventId", value = "近期活动id", required = true, paramType = "query", dataType = "Long")
    })
    public Result<Integer> isRegistered(Long userId, Long currentEventId) {
        LambdaQueryWrapper<RegistrationRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RegistrationRecord::getUserId, userId)
                .eq(RegistrationRecord::getCurrentEventId, currentEventId);
        RegistrationRecord one = registrationRecordService.getOne(queryWrapper);
        if(one != null) {
            return Result.success(1);
        }
        else return Result.success(0);
    }

    @Admin
    @GetMapping("/page")
    @ApiOperation(value = "分页查询所有报名记录，允许模糊查询近期活动名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize" ,value = "每页显示数量（不小于0）", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pageNum", value = "页数（不小于0）", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "近期活动名称", required = false, paramType = "query", dataType = "String")
    })
    public Result<Page<RegistrationRecord>> page(@Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum, String name){
        try {
            //构造分页构造器
            Page<RegistrationRecord> pageInfo = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<RegistrationRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(RegistrationRecord::getEventName, name);
            queryWrapper.orderByDesc(RegistrationRecord::getUpdateTime);
            //执行查询
            registrationRecordService.page(pageInfo, queryWrapper);
            return Result.success(pageInfo);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @DeleteMapping
    @ApiOperation("根据id删除报名记录")
    @ApiImplicitParam(name = "id", value = "报名记录id", required = true, paramType = "query", dataType = "Long")
    public Result<String> delete(Long id) {
        try {
            registrationRecordService.removeById(id);
            return Result.success("删除报名记录成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }


    @Auth
    @DeleteMapping("/cancel")
    @ApiOperation("取消报名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "currentEventId", value = "近期活动id", required = true, paramType = "query", dataType = "Long")
    })
    public Result<String> cancel(Long userId, Long currentEventId) {
        try {
            if(registrationRecordService.cancel(userId, currentEventId))
                return Result.success("取消报名成功");
            else return Result.fail("取消报名失败，请联系管理员");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }
}
