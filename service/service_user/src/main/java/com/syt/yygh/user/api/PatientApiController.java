package com.syt.yygh.user.api;

import com.syt.yygh.common.result.Result;
import com.syt.yygh.common.utils.AuthContextHolder;
import com.syt.yygh.model.user.Patient;
import com.syt.yygh.user.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.util.List;

/**
 * @Author: wangdi
 * @Date: 2021/4/3
 */
@RestController
@RequestMapping("/api/user/patient")
@Api(tags = "就诊人信息")
public class PatientApiController {

    @Resource
    private PatientService patientService;

    /**
     * 获取就诊人列表
     */
    @ApiOperation("获取就诊人列表")
    @GetMapping("/auth/listPatient")
    public Result getPatientList(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> patientList = patientService.getPatientListByUserId(userId);
        return Result.ok(patientList);
    }

    /**
     * 添加就诊人
     */
    @ApiOperation("添加就诊人")
    @PostMapping("/auth/savePatient")
    public Result savePatient(@RequestBody Patient patient,HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        boolean save = patientService.save(patient);
        return Result.ok(save);
    }

    /**
     * 根据id获取就诊人
     */
    @ApiOperation("根据id获取就诊人")
    @GetMapping("/auth/get/{id}")
    public Result getPatientById(@PathVariable("id") Long id){
        Patient patient = patientService.getPatientById(id);
        return Result.ok(patient);
    }

    /**
     * 修改就诊人
     */
    @ApiOperation("修改就诊人")
    @PostMapping("/auth/updatePatient")
    public Result updatePatientById(@RequestBody Patient patient){
        boolean b = patientService.updateById(patient);
        return Result.ok(b);
    }

    /**
     * 删除就诊人
     */
    @ApiOperation("删除就诊人")
    @DeleteMapping("/auth/delete/{id}")
    public Result deletePatient(@PathVariable("id") Long id){
        boolean b = patientService.removeById(id);
        return Result.ok(b);
    }

}
