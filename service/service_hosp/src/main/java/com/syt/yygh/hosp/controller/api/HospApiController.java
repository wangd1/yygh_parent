package com.syt.yygh.hosp.controller.api;

import com.syt.yygh.common.result.Result;
import com.syt.yygh.hosp.service.DepartmentService;
import com.syt.yygh.hosp.service.HospitalService;
import com.syt.yygh.hosp.service.HospitalSetService;
import com.syt.yygh.hosp.service.ScheduleService;
import com.syt.yygh.model.hosp.Hospital;
import com.syt.yygh.vo.hosp.DepartmentVo;
import com.syt.yygh.vo.hosp.HospitalQueryVo;
import com.syt.yygh.vo.hosp.ScheduleOrderVo;
import com.syt.yygh.vo.order.SignInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangdi
 * @Date: 2021/3/19
 */
@Api(tags = "医院查询")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {

    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;
    @Resource
    private ScheduleService scheduleService;

    @ApiOperation("查询医院列表")
    @GetMapping("/findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page,
                               @PathVariable Integer limit,
                               HospitalQueryVo hospitalQueryVo){
        Page<Hospital> pageHospital = hospitalService.findPageHospital(page, limit, hospitalQueryVo);
        return Result.ok(pageHospital);
    }

    @ApiOperation("根据医院名称模糊查询")
    @GetMapping("/findByHospName/{hospName}")
    public Result findByHospName(@PathVariable String hospName){
        List<Hospital> list = hospitalService.findByHospName(hospName);
        return Result.ok(list);
    }

    @ApiOperation("根据医院编号查询所有科室")
    @GetMapping("/findAllDepartment/{hoscode}")
    public Result findAllDepartment(@PathVariable String hoscode){
        List<DepartmentVo> departmentTree = departmentService.findDepartmentTree(hoscode);
        return Result.ok(departmentTree);
    }

    @ApiOperation("根据医院编号查询医院预约详情信息")
    @GetMapping("/findHospDetail/{hoscode}")
    public Result findHospDetail(@PathVariable String hoscode){
        Map<String,Object> map = hospitalService.findHospDetail(hoscode);
        return Result.ok(map);
    }

    /**
     * 获取可预约排班数据
     * @param page 页码
     * @param limit 个数
     * @param hosCode 医院编号
     * @param depCode 科室编号
     * @return 结果
     */
    @ApiOperation(value = "获取可预约排班数据")
    @GetMapping("auth/getBookingScheduleRule/{page}/{limit}/{hosCode}/{depCode}")
    public Result getBookingSchedule(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Integer page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,
            @ApiParam(name = "hosCode", value = "医院code", required = true)
            @PathVariable String hosCode,
            @ApiParam(name = "depCode", value = "科室code", required = true)
            @PathVariable String depCode) {
        return Result.ok(scheduleService.getBookingScheduleRule(page, limit, hosCode, depCode));
    }

    @ApiOperation(value = "获取排班数据")
    @GetMapping("auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result findScheduleList(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode,
            @ApiParam(name = "workDate", value = "排班日期", required = true)
            @PathVariable String workDate) {
        return Result.ok(scheduleService.getScheduleDetail(hoscode, depcode, workDate));
    }

    @ApiOperation(value = "根据排班id获取排班数据")
    @GetMapping("getSchedule/{scheduleId}")
    public Result getSchedule(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable String scheduleId) {
        return Result.ok(scheduleService.getById(scheduleId));
    }

    @ApiOperation(value = "根据排班id获取排班数据")
    @GetMapping("/inner/getScheduleOrder/{scheduleId}")
    public ScheduleOrderVo getScheduleOrder(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable String scheduleId) {
        return scheduleService.getScheduleOrderVo(scheduleId);
    }

    @ApiOperation(value = "获取医院签名信息")
    @GetMapping("/inner/getSignInfoVo/{hoscode}")
    public SignInfoVo getSignInfoVo(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable("hoscode") String hoscode) {
        return hospitalSetService.getSignInfoVo(hoscode);
    }


}
