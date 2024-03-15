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
import org.xavier.star.common.CommonException;
import org.xavier.star.common.Result;
import org.xavier.star.dto.CurrentEventWithImg;
import org.xavier.star.entity.CurrentEvent;
import org.xavier.star.entity.CurrentEventImg;
import org.xavier.star.entity.RegistrationRecord;
import org.xavier.star.service.CurrentEventImgService;
import org.xavier.star.service.CurrentEventService;
import org.xavier.star.service.RegistrationRecordService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "近期活动接口")
@RestController
@RequestMapping("/current")
@Slf4j
public class CurrentEventController {

    @Autowired
    private CurrentEventService currentEventService;

    @Autowired
    private RegistrationRecordService registrationRecordService;

    @Autowired
    private CurrentEventImgService currentEventImgService;

    @GetMapping
    @ApiModelProperty("获取所有近期活动")
    public Result<List<CurrentEvent>> getCurrentEvent() {
        try {
            LambdaQueryWrapper<CurrentEvent> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByDesc(CurrentEvent::getStartTime);
            List<CurrentEvent> currentEvents = currentEventService.list(queryWrapper);
            return  Result.success(currentEvents);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }


    @PostMapping
    @Admin
    @ApiModelProperty("新增近期活动")
    @ApiImplicitParam(name = "currentEvent", value = "近期活动json", required = true, paramType = "body", dataTypeClass = CurrentEvent.class)
    public Result<String> insert(@NotNull @RequestBody CurrentEvent currentEvent) {
        try {
            currentEventService.save(currentEvent);
            return Result.success("新增近期活动成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }


    @DeleteMapping
    @Admin
    @Transactional
    @ApiModelProperty("根据id删除近期活动信息")
    @ApiImplicitParam(name = "id", value = "近期活动id", required = true, paramType = "query", dataType = "Long")
    public Result<String> delete(Long id) {
        try {
            currentEventService.removeById(id);
            LambdaQueryWrapper<RegistrationRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(RegistrationRecord::getCurrentEventId, id);
            registrationRecordService.remove(queryWrapper);
            return Result.success("删除近期活动信息成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }


    @Admin
    @GetMapping("/page")
    @ApiOperation(value = "分页查询近期活动信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize" ,value = "每页显示数量（不小于0）", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pageNum", value = "页数（不小于0）", required = true, paramType = "query", dataType = "Integer"),
    })
    public Result<Page<CurrentEvent>> page(@Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum){
        try {
            //构造分页构造器
            Page<CurrentEvent> pageInfo = new Page<>(pageNum, pageSize);
            //添加排序条件
            LambdaQueryWrapper<CurrentEvent> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByDesc(CurrentEvent::getStartTime);
            //执行查询
            currentEventService.page(pageInfo, queryWrapper);
            return Result.success(pageInfo);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }


    @GetMapping("/{id}")
    @ApiOperation("根据id获取近期活动信息和对应的图片列表")
    @ApiImplicitParam(name = "id", value = "近期活动id", required = true, paramType = "path", dataType = "Long")
    public Result<CurrentEventWithImg> getById(@PathVariable Long id) {
        try {
            return Result.success(currentEventService.getByIdWithImg(id));
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @PutMapping
    @ApiOperation("根据id修改近期活动")
    @ApiImplicitParam(name = "currentEvent", value = "近期活动", required = true, paramType = "body", dataTypeClass = CurrentEvent.class)
    public Result<String> update(@RequestBody CurrentEvent currentEvent) {
        try {
            currentEventService.updateById(currentEvent);
            return Result.success("修改近期活动成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @PostMapping(value = "/upload", produces = "application/json")
    @ApiOperation(value = "上传近期活动海报")
    @ApiImplicitParam(name = "id", value = "近期活动id", required = true, paramType = "query", dataType = "Long")
    public Result<String> uploadPoster(MultipartFile file, Long id) {
        try{
            return Result.success(currentEventService.uploadPoster(file, id));
        }
        catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @PostMapping(value = "/img", produces = "application/json")
    @ApiOperation(value = "上传近期活动图片")
    @ApiImplicitParam(name = "currentEventId", value = "近期活动id", required = true, paramType = "query", dataType = "Long")
    public Result<String> uploadImg(MultipartFile file, Long currentEventId) {
        try{
            return Result.success(currentEventImgService.uploadImg(file, currentEventId));
        }
        catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @DeleteMapping(value = "/img")
    @ApiOperation(value = "根据近期活动图片id删除近期活动图片")
    @ApiImplicitParam(name = "id", value = "近期活动图片id", required = true, paramType = "query", dataType = "Long")
    public Result<String> deleteImg(Long id) {
        try{
            if(currentEventImgService.removeById(id))
                return Result.success("删除近期活动图片成功");
            else return Result.fail("删除近期活动图片失败");
        }
        catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Admin
    @GetMapping(value = "/img")
    @ApiOperation(value = "根据近期活动id获取对应的近期活动图片")
    @ApiImplicitParam(name = "currentEventId", value = "近期活动id", required = true, paramType = "query", dataType = "Long")
    public Result<List<CurrentEventImg>> getImg(Long currentEventId) {
        try{
            return Result.success(currentEventImgService.getImg(currentEventId));
        }
        catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }
}
