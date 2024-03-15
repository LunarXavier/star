package org.xavier.star.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.star.annotation.Admin;
import org.xavier.star.annotation.Auth;
import org.xavier.star.common.CommonException;
import org.xavier.star.common.Result;
import org.xavier.star.entity.CurrentEvent;
import org.xavier.star.entity.ParticipationRecord;
import org.xavier.star.entity.PreviousEvent;
import org.xavier.star.entity.RegistrationRecord;
import org.xavier.star.service.ParticipationRecordService;
import org.xavier.star.service.PreviousEventService;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "往期活动接口")
@RestController
@RequestMapping("/previous")
@Slf4j
public class PreviousEventController {

    @Autowired
    private PreviousEventService previousEventService;

    @Autowired
    private ParticipationRecordService participationRecordService;

    @GetMapping
    @ApiModelProperty("获取所有往期活动(公众号推文)")
    public Result<List<PreviousEvent>> getPreviousEvent() {
        try {
            LambdaQueryWrapper<PreviousEvent> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByDesc(PreviousEvent::getCreateTime);
            List<PreviousEvent> previousEvents = previousEventService.list(queryWrapper);
            return  Result.success(previousEvents);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @PostMapping
    @Admin
    @ApiModelProperty("新增往期活动/推文，同时给出对应的近期活动id，用于自动删除和发放心愿值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "公众号链接", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "currentEventId", value = "对应的近期活动id", required = false, paramType = "query", dataType = "Long")
    })
    public Result<String> insert(String url, Long currentEventId) {
        try {
            if(previousEventService.insertPreviousEvent(url, currentEventId)) {
                return Result.success("新增往期活动成功");
            }
            else return Result.fail("新增往期活动失败");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }


    @DeleteMapping
    @Admin
    @Transactional
    @ApiModelProperty("根据id删除往期活动信息")
    @ApiImplicitParam(name = "id", value = "往期活动id", required = true, paramType = "query", dataType = "Long")
    public Result<String> delete(Long id) {
        try {
            previousEventService.removeById(id);
            LambdaQueryWrapper<ParticipationRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ParticipationRecord::getPreviousEventId, id);
            participationRecordService.remove(queryWrapper);
            return Result.success("删除往期活动信息成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }


    @Admin
    @GetMapping("/page")
    @ApiOperation(value = "分页查询往期活动信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize" ,value = "每页显示数量（不小于0）", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pageNum", value = "页数（不小于0）", required = true, paramType = "query", dataType = "Integer"),
    })
    public Result<Page<PreviousEvent>> page(@Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum){
        try {
            //构造分页构造器
            Page<PreviousEvent> pageInfo = new Page<>(pageNum, pageSize);
            //添加排序条件
            LambdaQueryWrapper<PreviousEvent> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByDesc(PreviousEvent::getCreateTime);
            //执行查询
            previousEventService.page(pageInfo, queryWrapper);
            return Result.success(pageInfo);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id获取往期活动信息")
    @ApiImplicitParam(name = "id", value = "往期活动id", required = true, paramType = "path", dataType = "Long")
    public Result<PreviousEvent> getById(@PathVariable Long id) {
        try {
            PreviousEvent previousEvent = previousEventService.getById(id);
            return Result.success(previousEvent);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @PutMapping
    @ApiOperation("根据id修改往期活动")
    @ApiImplicitParam(name = "previousEvent", value = "往期活动", required = true, paramType = "body", dataTypeClass = PreviousEvent.class)
    public Result<String> update(@RequestBody PreviousEvent previousEvent) {
        try {
            previousEventService.updateById(previousEvent);
            return Result.success("修改往期活动成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @PostMapping("/upload")
    @ApiOperation("上传往期活动具体截图")
    @ApiImplicitParam(name = "id", value = "往期活动id", required = true, paramType = "query", dataType = "Long")
    public Result<String> uploadImg(MultipartFile file, Long id) {
        try {
            previousEventService.uploadImg(file, id);
            return Result.success("上传图片成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }
}
