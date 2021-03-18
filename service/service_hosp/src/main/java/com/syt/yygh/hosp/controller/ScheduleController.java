package com.syt.yygh.hosp.controller;

import com.syt.yygh.common.result.Result;
import com.syt.yygh.hosp.service.ScheduleService;
import com.syt.yygh.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangdi
 * @Date: 2021/3/18
 */
@Api(tags = "排班接口")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("获取医院科室排班规则")
    @GetMapping("/getScheduleRules/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRules(@PathVariable long page,
                                   @PathVariable long limit,
                                   @PathVariable String hoscode,
                                   @PathVariable String depcode){
        Map<String,Object> map = scheduleService.getScheduleRules(page,limit,hoscode,depcode);
        return Result.ok(map);
    }

    @ApiOperation("获取排班详细")
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode,
                                    @PathVariable String depcode,
                                    @PathVariable String workDate){
        List<Schedule> list = scheduleService.getScheduleDetail(hoscode,depcode,workDate);
        return Result.ok(list);
    }
}
