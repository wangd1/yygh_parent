package com.syt.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syt.yygh.enums.AuthStatusEnum;
import com.syt.yygh.model.user.Patient;
import com.syt.yygh.model.user.UserInfo;
import com.syt.yygh.user.mapper.UserInfoMapper;
import com.syt.yygh.user.service.PatientService;
import com.syt.yygh.user.service.UserInfoService;
import com.syt.yygh.vo.user.UserAuthVo;
import com.syt.yygh.vo.user.UserInfoQueryVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangdi
 * @Date: 2021/3/20
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Resource
    private PatientService patientService;

    @Override
    public boolean save(UserInfo entity) {
        return baseMapper.insert(entity)>=1;
    }

    @Override
    public UserInfo getByOpenid(String openId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        // 根据用户id查询用户信息
        UserInfo userInfo = baseMapper.selectById(userId);
        // 设置认证信息
        if(userInfo!=null){
            userInfo.setName(userAuthVo.getName());
            userInfo.setCertificatesType(userAuthVo.getCertificatesType());
            userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
            userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
            userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        }
        // 进行信息更新
        baseMapper.updateById(userInfo);
    }

    @Override
    public UserInfo getById(Long userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo queryVo) {
        QueryWrapper<UserInfo> queryWrapper = checkUserInfo(queryVo);
        Page<UserInfo> userInfoPage = baseMapper.selectPage(pageParam, queryWrapper);
        userInfoPage.getRecords().forEach(this::packageUserInfo);
        return userInfoPage;
    }

    @Override
    public void lock(Long userId, Integer status) {
        if(status==0||status==1){
            UserInfo userInfo = baseMapper.selectById(userId);
            if(userInfo!=null){
                userInfo.setStatus(status);
                baseMapper.updateById(userInfo);
            }
        }
    }

    @Override
    public void approval(Long userId, Integer authStatus) {
        if(authStatus==2||authStatus==-1){
            UserInfo userInfo = baseMapper.selectById(userId);
            if(userInfo!=null){
                userInfo.setAuthStatus(authStatus);
                baseMapper.updateById(userInfo);
            }
        }
    }

    @Override
    public Map<String, Object> show(Long id) {
        Map<String,Object> result = new HashMap<>(2);
        // 查询用户详情
        UserInfo userInfo = baseMapper.selectById(id);
        this.packageUserInfo(userInfo);
        result.put("userInfo",userInfo);
        // 添加就诊人信息
        List<Patient> patientList = patientService.getPatientListByUserId(id);
        result.put("patientList",patientList);
        return result;
    }

    private void packageUserInfo(UserInfo userInfo){
        //处理认证状态编码
        userInfo.getParam().put("authStatusString",AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        //处理用户状态 0  1
        String statusString = userInfo.getStatus() ==0 ?"锁定" : "正常";
        userInfo.getParam().put("statusString",statusString);

    }

    /**
     * 封装分页查询条件
     * @param queryVo 查询信息
     * @return 查询wrapper
     */
    private QueryWrapper<UserInfo> checkUserInfo(UserInfoQueryVo queryVo){
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        //UserInfoQueryVo获取条件值
        String name = queryVo.getKeyword();
        Integer status = queryVo.getStatus();
        Integer authStatus = queryVo.getAuthStatus();
        String createTimeBegin = queryVo.getCreateTimeBegin();
        String createTimeEnd = queryVo.getCreateTimeEnd();
        //对条件值进行非空判断
        if(!StringUtils.isEmpty(name)) {
            wrapper.like("name",name);
        }
        if(status!=null) {
            wrapper.eq("status",status);
        }
        if(authStatus!=null) {
            wrapper.eq("auth_status",authStatus);
        }
        if(!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time",createTimeEnd);
        }

        return wrapper;
    }
}
