package com.syt.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syt.yygh.common.exception.YyghException;
import com.syt.yygh.common.result.Result;
import com.syt.yygh.common.util.MD5;
import com.syt.yygh.hosp.service.HospitalSetService;
import com.syt.yygh.model.hosp.HospitalSet;
import com.syt.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * @Author: wangdi
 * @Date: 2021/3/11
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 查询医院所有信息
     * @return list
     */
    @ApiOperation(value = "查询所有医院信息")
    @GetMapping("/findAll")
    public Result findAll(){
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    /**
     * 根据id删除
     * @param id id
     * @return 是否成功
     */
    @ApiOperation("根据id删除医院")
    @DeleteMapping("{id}")
    public Result removeHosp(@PathVariable("id") Long id){
        boolean b = hospitalSetService.removeById(id);
        if(b){
            return Result.ok(b);
        }
        return Result.fail(b);
    }

    /**
     * 条件查询加分页
     * @param current 当前
     * @param limit 个数
     * @param hospitalSetQueryVo 查询条件
     * @return 结果
     */
    @ApiOperation("分页条件查询医院设置")
    @PostMapping("/findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> page = new Page<>(current,limit);
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hosname)) {
            wrapper.like("hosname", hosname);
        }
        if (!StringUtils.isEmpty(hoscode)) {
            wrapper.eq("hoscode", hoscode);
        }
        Page<HospitalSet> list = hospitalSetService.page(page, wrapper);
        return Result.ok(list);
    }

    /**
     * 添加医院设置
     * @param hospitalSet 信息
     * @return 结果
     */
    @PostMapping("/saveHospSet")
    @ApiOperation("添加医院设置")
    public Result saveHospSet(@RequestBody HospitalSet hospitalSet){
        hospitalSet.setStatus(1);
        // 签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
        boolean save = hospitalSetService.save(hospitalSet);
        if(!save){
            return Result.fail(save);
        }
        return Result.ok(save);
    }

    /**
     * 根据id获取医院设置
     * @param id id
     * @return 结果
     */
    @ApiOperation("根据id获取医院设置")
    @GetMapping("/getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id){
        HospitalSet hosp = hospitalSetService.getById(id);
        return Result.ok(hosp);
    }

    /**
     * 修改医院设置
     * @param hospitalSet 更新信息
     * @return 结果
     */
    @ApiOperation("修改医院设置")
    @PostMapping("/updateHospSet")
    public Result updateHospSet(@RequestBody HospitalSet hospitalSet){
        boolean b = hospitalSetService.updateById(hospitalSet);
        if(b){
            return Result.ok(b);
        }
        return Result.fail(b);
    }

    /**
     * 批量删除医院设置
     * @param idList id集合
     * @return 结果
     */
    @ApiOperation("批量删除医院设置")
    @DeleteMapping("batchRemoveHospSet")
    public Result batchRemove(@RequestBody List<String> idList){
        boolean b = hospitalSetService.removeByIds(idList);
        if(b){
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 锁定和解锁医院
     * @param id id
     * @param status 状态
     * @return 结果
     */
    @PutMapping("lockHospSet/{id}/{status}")
    @ApiOperation("锁定和解锁医院")
    public Result lockHospSet(@PathVariable Long id,@PathVariable Integer status){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    /**
     * 发送密钥
     * @param id id
     * @return 结果
     */
    @ApiOperation("发送密钥")
    @PutMapping("sendSignKey/{id}")
    public Result sendSignKey(@PathVariable Long id){
        try {
            int i = 1/0;
        }catch (Exception e){
            throw new YyghException("不可以除以0", 201);
        }
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        // TODO 发送短信
        return Result.ok();
    }

}
