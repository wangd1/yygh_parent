package com.syt.yygh.user.processor;

import com.syt.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 * 登录接口
 * @author Wangdi
 */
public interface ILogin {

    /**
     * 登录方法
     * @param loginVo 登录参数
     * @return 结果
     */
    Map<String,Object> doLogin(LoginVo loginVo);

}
