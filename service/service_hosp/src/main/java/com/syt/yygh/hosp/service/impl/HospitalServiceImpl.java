package com.syt.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.syt.yygh.cmn.client.DictFeginClient;
import com.syt.yygh.hosp.repository.HospitalRepository;
import com.syt.yygh.hosp.service.HospitalService;
import com.syt.yygh.model.hosp.Hospital;
import com.syt.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wangdi
 * @Date: 2021/3/13
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private DictFeginClient dictFeginClient;

    @Override
    public void save(Map<String, Object> paramMap) {
        String s = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(s, Hospital.class);
        String code = hospital.getHoscode();
        Hospital isExists = hospitalRepository.getHospitalByHoscode(code);
        if(isExists==null){
            // 添加
            hospital.setStatus(0);
            hospital.setIsDeleted(0);
            hospital.setUpdateTime(new Date());
            hospital.setCreateTime(new Date());
        }else{
            // 更新
            hospital.setStatus(isExists.getStatus());
            hospital.setIsDeleted(0);
            hospital.setUpdateTime(new Date());
            hospital.setCreateTime(isExists.getCreateTime());
        }
        hospitalRepository.save(hospital);
    }

    @Override
    public Hospital getHospitalByHosCode(String code) {
        return hospitalRepository.getHospitalByHoscode(code);
    }

    @Override
    public Page<Hospital> findPageHospital(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        // 创建pagable对象
        Pageable pagable = PageRequest.of(page - 1, limit);
        // 构建条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        //对象转换
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        // 创建对象
        Example<Hospital> example = Example.of(hospital,exampleMatcher);
        // 调用方法查询
        Page<Hospital> pages = hospitalRepository.findAll(example, pagable);
        pages.getContent().forEach(this::setHospitalTypeName);
        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Map<String,Object> getHospitalById(String id) {
        Map<String,Object> result = new HashMap<>(2);
        Hospital hospital = hospitalRepository.findById(id).get();
        setHospitalTypeName(hospital);
        result.put("bookingRules",hospital.getBookingRule());
        hospital.setBookingRule(null);
        result.put("hospital",hospital);
        return result;
    }

    /**
     * 设置医院级别名称
     * @param hospital 医院信息
     */
    private void setHospitalTypeName(Hospital hospital){
        String hosType = dictFeginClient.getDictName("Hostype", hospital.getHostype());
        String provinceString = dictFeginClient.getDictName(hospital.getProvinceCode());
        String cityString = dictFeginClient.getDictName(hospital.getCityCode());
        String districtString = dictFeginClient.getDictName(hospital.getDistrictCode());
        hospital.getParam().put("hospitalString",hosType);
        hospital.getParam().put("fullAddress",provinceString+cityString+districtString);
    }
}
