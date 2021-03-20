package com.syt.yygh.msm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.syt.yygh.msm.service.SmsService;
import com.syt.yygh.msm.utils.ConstantPropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wangdi
 * @Date: 2021/3/20
 */
@Service
public class SmsServiceImpl implements SmsService {

    @Override
    public boolean send(String phone, String code) {
        // 判断手机号是否为空
        if(StringUtils.isEmpty(phone)){
            return false;
        }
        // 发送，整合短信服务(短信改版，个人不给申请了)
        // 设置相关参数
        /* Config config = new Config();
        config.accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        config.accessKeySecret = ConstantPropertiesUtils.SECRET;
        config.endpoint = "ecs.aliyuncs.com";
        try {
            Client client = new Client(config);
            Map<String,Object> param = new HashMap<>(1);
            param.put("code",code);
            SendSmsRequest smsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phone)
                    .setSignName("")
                    .setTemplateCode("")
                    .setTemplateParam(JSONObject.toJSONString(param));
            SendSmsResponse sendSmsResponse = client.sendSms(smsRequest);
            String responseCode = sendSmsResponse.getBody().code;
            return "OK".equals(responseCode);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }*/
        return true;
    }
}
