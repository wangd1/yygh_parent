package com.syt.yygh.hosp.controller;

import com.syt.yygh.common.result.Result;
import com.syt.yygh.hosp.service.DepartmentService;
import com.syt.yygh.model.hosp.Department;
import com.syt.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: wangdi
 * @Date: 2021/3/16
 */
@Api(tags = "科室管理")
@RequestMapping("/admin/hosp/department")
@RestController()
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation("查询医院所有科室")
    @GetMapping("listDepartment/{hosCode}")
    public Result listDepartment(@PathVariable String hosCode){
        List<DepartmentVo> list = departmentService.findDepartmentTree(hosCode);
        return Result.ok(list);
    }

}
