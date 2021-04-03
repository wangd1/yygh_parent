package com.syt.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syt.yygh.cmn.client.DictFeginClient;
import com.syt.yygh.enums.DictEnum;
import com.syt.yygh.model.user.Patient;
import com.syt.yygh.user.mapper.PatientMapper;
import com.syt.yygh.user.service.PatientService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: wangdi
 * @Date: 2021/4/3
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService{

    @Resource
    private DictFeginClient dictFeginClient;

    @Override
    public List<Patient> getPatientListByUserId(Long userId) {
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<Patient> patientList = baseMapper.selectList(queryWrapper);
        // 通过远程调用，获取字典表信息
        patientList.forEach(this::packPatient);
        return patientList;
    }

    @Override
    public Patient getPatientById(Long id) {
        Patient patient = baseMapper.selectById(id);
        this.packPatient(patient);
        return patient;
    }

    /**
     * 封装其他参数
     * @param patient 就诊人信息
     */
    private void packPatient(Patient patient){
        String certificatesTypeString = dictFeginClient.getDictName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        patient.getParam().put("certificatesTypeString",certificatesTypeString );
        //联系人证件类型
        String contactsCertificatesTypeString =
                dictFeginClient.getDictName(DictEnum.CERTIFICATES_TYPE.getDictCode(),patient.getContactsCertificatesType());
        //省
        String provinceString = dictFeginClient.getDictName(patient.getProvinceCode());
        //市
        String cityString = dictFeginClient.getDictName(patient.getCityCode());
        //区
        String districtString = dictFeginClient.getDictName(patient.getDistrictCode());
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());

    }
}
