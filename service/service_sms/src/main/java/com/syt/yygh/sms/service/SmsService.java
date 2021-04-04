package com.syt.yygh.sms.service;

import com.syt.yygh.vo.sms.SmsVo;

import java.util.Map;

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

    /**
     * 发送其他
     * @param phone 手机号
     * @param params 其他
     * @return 发送是否成功
     */
    boolean send(String phone, Map<String,Object> params);

    /**
     * mq发送短信
     * @param smsVo 欧版
     * @return 是否成功
     */
    boolean send(SmsVo smsVo);
}
