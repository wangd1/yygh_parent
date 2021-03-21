package com.syt.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syt.yygh.model.user.UserInfo;
import com.syt.yygh.user.mapper.UserInfoMapper;
import com.syt.yygh.user.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * @Author: wangdi
 * @Date: 2021/3/20
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {
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
}
