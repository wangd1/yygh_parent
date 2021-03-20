package com.syt.yygh.user.processor;

import com.syt.yygh.vo.user.LoginVo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录接口抽象类
 * @Author: wangdi
 * @Date: 2021/3/20
 */
@Slf4j
public abstract class AbstractLogin implements ILogin {

    /**
     * 类初始化时，将子类对象存储在map中，key：登录类型
     */
    public static ConcurrentHashMap<Integer, AbstractLogin> loginMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        loginMap.put(getLoginType(),this);
    }

    @Override
    public final Map<String, Object> doLogin(LoginVo loginVo) {
        log.info("开始登录："+ loginVo);
        // 参数验证
        validate(loginVo);
        // 登录校验
        return doProcessor(loginVo);
    }

    /**
     * 在子类中去声明自己的登录类型
     * @return 类型
     */
    protected abstract int getLoginType();

    /**
     * 通过子类去完成验证
     * @param loginVo 登录参数
     */
    protected abstract void validate(LoginVo loginVo);

    /**
     * 登录校验
     * @param loginVo 登录参数
     * @return 结果
     */
    protected abstract Map<String, Object> doProcessor(LoginVo loginVo);

}
