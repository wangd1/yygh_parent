package com.syt.yygh.user.service;

import com.syt.yygh.model.user.UserInfo;

/**
 * @author Wangdi
 */
public interface UserInfoService {
    boolean save(UserInfo userInfo);

    /**
     * 根据openid判断是否微信登录过
     * @param openId openid
     * @return 信息
     */
    UserInfo getByOpenid(String openId);
}
