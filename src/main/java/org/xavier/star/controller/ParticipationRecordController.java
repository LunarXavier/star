package org.xavier.star.controller;

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
import org.xavier.star.entity.PreviousEvent;
import org.xavier.star.service.ParticipationRecordService;

import java.util.List;

@Api(tags = "参与活动记录接口")
@RestController
@RequestMapping("/participation")
@Slf4j
public class ParticipationRecordController {

    @Autowired
    private ParticipationRecordService participationRecordService;

    @Auth
    @GetMapping
    @ApiOperation("根据用户id获取用户参与活动信息(近期活动)")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "Long")
    public Result<List<PreviousEvent>> getParticipationRecordByUserId(Long userId) {
        try{
            return Result.success(participationRecordService.getPreviousEvent(userId));
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }
}
