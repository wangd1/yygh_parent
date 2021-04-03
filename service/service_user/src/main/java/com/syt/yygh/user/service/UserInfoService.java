package com.syt.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syt.yygh.model.user.UserInfo;
import com.syt.yygh.vo.user.UserAuthVo;
import com.syt.yygh.vo.user.UserInfoQueryVo;

import java.util.Map;

/**
 * @author Wangdi
 */
public interface UserInfoService {
    /**
     * 保存
     * @param userInfo 用户信息
     * @return 是否成功
     */
    boolean save(UserInfo userInfo);

    /**
     * 根据openid判断是否微信登录过
     * @param openId openid
     * @return 信息
     */
    UserInfo getByOpenid(String openId);

    /**
     * 用户认证
     * @param userId 用户id
     * @param userAuthVo 认证对象
     */
    void userAuth(Long userId, UserAuthVo userAuthVo);

    /**
     * 根据id获取用户
     * @param userId 用户id
     * @return 用户
     */
    UserInfo getById(Long userId);

    /**
     * 分页条件查询
     * @param pageParam 分页
     * @param queryVo 条件
     * @return 列表
     */
    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo queryVo);

    /**
     * 锁定用户
     * @param userId 用户id
     * @param status 状态
     */
    void lock(Long userId, Integer status);

    /**
     * 显示用户详情
     * @param id id
     * @return 结果
     */
    Map<String, Object> show(Long id);

    /**
     * 认证用户审批
     * @param userId 用户id
     * @param authStatus 认证状态
     */
    void approval(Long userId, Integer authStatus);
}
