package com.syt.yygh.hosp.controller.api;

import com.syt.yygh.common.result.Result;
import com.syt.yygh.hosp.service.DepartmentService;
import com.syt.yygh.hosp.service.HospitalService;
import com.syt.yygh.model.hosp.Hospital;
import com.syt.yygh.vo.hosp.DepartmentVo;
import com.syt.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
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
    private DepartmentService departmentService;

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

}
