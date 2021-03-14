package com.syt.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syt.yygh.model.hosp.Hospital;
import com.syt.yygh.model.hosp.HospitalSet;
import com.syt.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

/**
 * @author Wangdi
 */
public interface HospitalSetService extends IService<HospitalSet> {

    /**
     * 根据医院code获取医院签名
     * @param code code
     * @return 签名
     */
    String getSignKey(String code);

}
