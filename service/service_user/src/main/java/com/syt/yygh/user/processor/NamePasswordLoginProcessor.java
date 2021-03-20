package com.syt.yygh.user.processor;

import com.syt.yygh.common.helper.JwtHelper;
import com.syt.yygh.enums.LoginTypeEnum;
import com.syt.yygh.vo.user.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wangdi
 * @Date: 2021/3/20
 */
@Service
@Slf4j
public class NamePasswordLoginProcessor extends AbstractLogin{

    @Override
    protected int getLoginType() {
        return LoginTypeEnum.NORMAL.getCode();
    }

    @Override
    protected void validate(LoginVo loginVo) {
        log.info("用户名密码登录");
    }

    @Override
    protected Map<String, Object> doProcessor(LoginVo loginVo) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("name", "张三");
        result.put("token", JwtHelper.createToken(1L,"张三"));
        return result;
    }
}
