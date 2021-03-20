package com.syt.yygh.user.processor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syt.yygh.common.exception.YyghException;
import com.syt.yygh.common.helper.JwtHelper;
import com.syt.yygh.common.result.ResultCodeEnum;
import com.syt.yygh.enums.LoginTypeEnum;
import com.syt.yygh.model.user.UserInfo;
import com.syt.yygh.user.mapper.UserInfoMapper;
import com.syt.yygh.vo.user.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wangdi
 * @Date: 2021/3/20
 */
@Service
public class PhoneCodeLoginProcessor extends AbstractLogin {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected int getLoginType() {
        return LoginTypeEnum.PHONE_CODE.getCode();
    }

    /**
     * 执行参数校验操作
     * @param loginVo 登录参数
     */
    @Override
    protected void validate(LoginVo loginVo) {
        // 验证手机号和验证码是否为空
        if(StringUtils.isEmpty(loginVo.getPhone())||StringUtils.isEmpty(loginVo.getCode())){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 执行登录操作
     * @param loginVo 登录参数
     * @return 登录结果
     */
    @Override
    protected Map<String, Object> doProcessor(LoginVo loginVo) {
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        // 判断手机号和验证码是否匹配
        String redisCode = redisTemplate.opsForValue().get(phone);
        if(!code.equals(redisCode)){
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }
        // 判断是否第一次登录(根据手机号查询，如果不存在，就是第一次登录)
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        // 如果是第一次，要插入数据到数据库，不是就直接登录
        if(userInfo==null){
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            userInfoMapper.insert(userInfo);
        }
        if(userInfo.getStatus()==0){
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        // 返回登录信息，返回登录用户名，返回token信息
        Map<String, Object> result = new HashMap<>(4);
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)){
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)){
            name = userInfo.getPhone();
        }
        result.put("name",name);
        result.put("token", JwtHelper.createToken(userInfo.getId(),name));
        return result;
    }
}
