package com.syt.yygh.sms.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: wangdi
 * @Date: 2021/3/20
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean {

    @Value("${aliyun.sms.regionId}")
    private String regionId;
    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.sms.secret}")
    private String secret;

    public static String REGION_Id;
    public static String ACCESS_KEY_ID;
    public static String SECRET;


    @Override
    public void afterPropertiesSet() throws Exception {
        REGION_Id = this.regionId;
        ACCESS_KEY_ID = this.accessKeyId;
        SECRET = this.secret;
        System.out.println("===================="+SECRET);
    }
}
