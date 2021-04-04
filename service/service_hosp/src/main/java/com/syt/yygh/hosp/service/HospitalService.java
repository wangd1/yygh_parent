package com.syt.yygh.hosp.service;

import com.syt.yygh.model.hosp.Hospital;
import com.syt.yygh.vo.hosp.HospitalQueryVo;
import com.syt.yygh.vo.order.SignInfoVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Wangdi
 */
public interface HospitalService {
    /**
     * 上传医院
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 根据hoscode查询医院
     * @param code code
     */
    Hospital getHospitalByHosCode(String code);

    /**
     * 分页查询医院
     * @param page 当前页
     * @param limit 每页个数
     * @param hospitalQueryVo 查询条件
     * @return 结果
     */
    Page<Hospital> findPageHospital(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    /**
     * 更新医院上线状态
     * @param id id
     * @param status 状态
     */
    void updateStatus(String id, Integer status);

    /**
     * 获取医院详细信息
     * @param id id
     * @return 结果
     */
    Map<String,Object> getHospitalById(String id);

    /**
     * 根据医院名称模糊查询
     * @param hospName 医院名称
     * @return 查询结果
     */
    List<Hospital> findByHospName(String hospName);

    /**
     * 根据医院编号查询医院预约详细信息
     * @param hoscode 医院编号
     * @return 结果
     */
    Map<String, Object> findHospDetail(String hoscode);
}
