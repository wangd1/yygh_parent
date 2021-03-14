package com.syt.yygh.hosp.controller;

import com.syt.yygh.common.result.Result;
import com.syt.yygh.hosp.service.HospitalService;
import com.syt.yygh.hosp.service.HospitalSetService;
import com.syt.yygh.model.hosp.Hospital;
import com.syt.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: wangdi
 * @Date: 2021/3/14
 */
@CrossOrigin
@Api(tags = "医院管理")
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @GetMapping("/list/{page}/{limit}")
    public Result findPageHospital(@PathVariable Integer page, @PathVariable Integer limit,
                                   HospitalQueryVo hospitalQueryVo){
        Page<Hospital> page1 = hospitalService.findPageHospital(page,limit,hospitalQueryVo);
        return Result.ok(page1);
    }

    @ApiOperation("更新医院上线状态")
    @PutMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable String id,@PathVariable Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    @ApiOperation("获取医院详情信息")
    @GetMapping("getHospitalDetail/{id}")
    public Result getHospitalDetail(@PathVariable String id){
        Map<String,Object> result = hospitalService.getHospitalById(id);
        return  Result.ok(result);
    }

}
