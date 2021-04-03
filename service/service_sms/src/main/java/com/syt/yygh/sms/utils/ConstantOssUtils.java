package com.syt.yygh.sms.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: wangdi
 * @Date: 2021/3/20
 */
@Component
public class ConstantOssUtils implements InitializingBean {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.secret}")
    private String secret;
    @Value("${aliyun.oss.bucket}")
    private String bucket;

    public static String ENDPOINT;
    public static String ACCESS_KEY_ID;
    public static String SECRET;
    public static String BUCKET;
    public static String FILE_URL;


    @Override
    public void afterPropertiesSet() throws Exception {
        ENDPOINT = this.endpoint;
        ACCESS_KEY_ID = this.accessKeyId;
        SECRET = this.secret;
        BUCKET = this.bucket;
        FILE_URL = "https://"+BUCKET+".oss-cn-beijing.aliyuncs.com/";
    }
}
