package com.syt.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.syt.yygh.hosp.repository.DepartmentRepository;
import com.syt.yygh.hosp.service.DepartmentService;
import com.syt.yygh.model.hosp.Department;
import com.syt.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Author: wangdi
 * @Date: 2021/3/13
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        Department department = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Department.class);
        Department departmentExists = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if(departmentExists==null){
            // 插入
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }else{
            // 更新
            departmentExists.setUpdateTime(new Date());
            departmentExists.setIsDeleted(0);
            departmentRepository.save(departmentExists);
        }
    }

    @Override
    public Page<Department> findPage(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        Pageable pageable = PageRequest.of(page-1,limit);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);
        Example<Department> example = Example.of(department,matcher);

        return departmentRepository.findAll(example,pageable);
    }

    @Override
    public void removeDepartment(String hosCode,String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hosCode, depcode);
        if (department!=null) {
            departmentRepository.deleteById(department.getId());
        }
    }
}
