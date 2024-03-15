package org.xavier.star.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xavier.star.annotation.Admin;
import org.xavier.star.common.Result;
import org.xavier.star.entity.TimePoint;
import org.xavier.star.service.TimePointService;

import java.util.List;


@Api(tags = "时间点接口")
@RestController
@RequestMapping("/timePoint")
@Slf4j
public class TimePointController {

    @Autowired
    private TimePointService timePointService;

    @GetMapping
    public Result<List<TimePoint>> getAll() {
        return Result.success(timePointService.list(new LambdaQueryWrapper<TimePoint>().orderByAsc(TimePoint::getCreateTime)));
    }

    @Admin
    @PostMapping
    public Result<String> insert(@RequestBody TimePoint timePoint) {
        timePointService.save(timePoint);
        return Result.success("添加成功");
    }
}
