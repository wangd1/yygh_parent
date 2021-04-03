package com.syt.yygh.user.api;

import com.baomidou.mybatisplus.extension.api.R;
import com.syt.yygh.common.result.Result;
import com.syt.yygh.common.utils.AuthContextHolder;
import com.syt.yygh.model.user.UserInfo;
import com.syt.yygh.user.processor.ILogin;
import com.syt.yygh.user.processor.PhoneCodeLoginProcessor;
import com.syt.yygh.user.service.UserInfoService;
import com.syt.yygh.vo.user.LoginVo;
import com.syt.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wangdi
 * @Date: 2021/3/20
 */
@Api(tags = "用户信息")
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Resource
    private UserInfoService userInfoService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        Map<String, Object> result = new HashMap<>(4);
        // 登录类型
        Integer loginType = loginVo.getLoginType()==null?3:loginVo.getLoginType();
        ILogin login = PhoneCodeLoginProcessor.loginMap.get(loginType);
        if(login==null){
            result.put("msg","暂不支持该种登录类型");
            result.put("code","500");
            return Result.fail(result);
        }
        return Result.ok(login.doLogin(loginVo));
    }

    /**
     * 用户认证
     * @param userAuthVo 认证对象
     * @param request
     * @return 结果
     */
    @PostMapping("/auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request){
        userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
        return Result.ok();
    }

    /**
     * 获取用户信息
     * @param request request
     * @return 结果
     */
    @GetMapping("/auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }

}
