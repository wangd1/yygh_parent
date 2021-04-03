package com.syt.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syt.yygh.model.user.Patient;

import java.util.List;

/**
 * @Author: wangdi
 * @Date: 2021/4/3
 */
public interface PatientService extends IService<Patient> {

    /**
     * 根据userId获取就诊人
     * @param userId 用户id
     * @return 就诊人
     */
    List<Patient> getPatientListByUserId(Long userId);

    /**
     * 根据id获取就诊人
     * @param id id
     * @return 就诊人
     */
    Patient getPatientById(Long id);
}
