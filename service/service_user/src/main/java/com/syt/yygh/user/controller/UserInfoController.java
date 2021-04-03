package com.syt.yygh.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syt.yygh.common.result.Result;
import com.syt.yygh.model.user.UserInfo;
import com.syt.yygh.user.service.UserInfoService;
import com.syt.yygh.vo.user.UserInfoQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: wangdi
 * @Date: 2021/4/3
 */
@RestController
@RequestMapping("/admin/user")
@Api(tags = "用户信息")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    /**
     * 分页查询用户
     * @param page 页数
     * @param limit 个数
     * @param queryVo 查询条件
     * @return 列表
     */
    @ApiOperation("用户列表")
    @GetMapping("/{page}/{limit}")
    public Result list(@PathVariable("page") int page,
                       @PathVariable("limit") int limit,
                       UserInfoQueryVo queryVo){
        Page<UserInfo> pageParam = new Page<>(page,limit);
        IPage<UserInfo> pages = userInfoService.selectPage(pageParam,queryVo);
        return Result.ok(pages);
    }

    /**
     * 锁定用户
     * @param userId 用户id
     * @param status 用户状态
     * @return 结果
     */
    @ApiOperation("锁定用户")
    @GetMapping("/lock/{userId}/{status}")
    public Result lock(@PathVariable("userId") Long userId,
                       @PathVariable("status") Integer status){
        userInfoService.lock(userId,status);
        return Result.ok();
    }

    /**
     * 认证用户审批
     * @param userId 用户id
     * @param authStatus 用户认证状态
     * @return 结果
     */
    @ApiOperation("认证用户审批")
    @GetMapping("/approval/{userId}/{authStatus}")
    public Result approval(@PathVariable("userId") Long userId,
                       @PathVariable("authStatus") Integer authStatus){
        userInfoService.approval(userId,authStatus);
        return Result.ok();
    }

    /**
     * 显示用户详情
     * @param id id
     * @return 结果
     */
    @ApiOperation("显示详情")
    @GetMapping("/show/{id}")
    public Result show(@PathVariable("id") Long id){
        Map<String,Object> map = userInfoService.show(id);
        return Result.ok(map);
    }
}
