package com.syt.yygh.common.utils;

import com.syt.yygh.common.helper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取当前用户信息的类
 * @Author: wangdi
 * @Date: 2021/4/3
 */
public class AuthContextHolder {

    /**
     * 获取当前用户id
     */
    public static Long getUserId(HttpServletRequest request){
        String token = request.getHeader("token");
        return JwtHelper.getUserId(token);
    }

    /**
     * 获取当前用户名称
     */
    public static String getUserName(HttpServletRequest request){
        String token = request.getHeader("token");
        return JwtHelper.getUserName(token);
    }
}
