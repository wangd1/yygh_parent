package com.syt.yygh.hosp.service;

import com.syt.yygh.model.hosp.Department;
import com.syt.yygh.vo.hosp.DepartmentQueryVo;
import com.syt.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Wangdi
 */
public interface DepartmentService {

    /**
     * 上传科室
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 查询科室
     * @param page 当前页
     * @param limit 个数
     * @param departmentQueryVo 查询条件
     * @return 结果
     */
    Page<Department> findPage(int page, int limit, DepartmentQueryVo departmentQueryVo);

    /**
     * 删除科室
     * @param depcode 科室编号
     * @return
     */
    void removeDepartment(String hosCode,String depcode);

    /**
     * 查询医院所有科室，树结构
     * @param hosCode 医院编号
     * @return list
     */
    List<DepartmentVo> findDepartmentTree(String hosCode);

    /**
     * 获取科室信息
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @return 科室信息
     */
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
