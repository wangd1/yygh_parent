package com.syt.yygh.sms.service;

/**
 * @author Wangdi
 */
public interface SmsService {
    /**
     * 发送短信验证码
     * @param phone 手机号
     * @param code 验证码
     * @return 发送是否成功
     */
    boolean send(String phone, String code);
}
