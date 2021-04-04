package com.syt.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syt.yygh.common.exception.YyghException;
import com.syt.yygh.common.result.ResultCodeEnum;
import com.syt.yygh.hosp.mapper.HospitalSetMapper;
import com.syt.yygh.hosp.service.HospitalSetService;
import com.syt.yygh.model.hosp.HospitalSet;
import com.syt.yygh.vo.order.SignInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: wangdi
 * @Date: 2021/3/11
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet>
        implements HospitalSetService {

    @Override
    public String getSignKey(String code) {
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hoscode",code);
        HospitalSet hospitalSet = baseMapper.selectOne(queryWrapper);
        if(hospitalSet!=null){
            return hospitalSet.getSignKey();
        }
        return "";
    }

    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(null == hospitalSet) {
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }

}
