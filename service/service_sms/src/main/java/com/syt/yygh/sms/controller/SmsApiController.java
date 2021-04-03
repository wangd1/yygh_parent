package com.syt.yygh.sms.controller;

import com.syt.yygh.common.result.Result;
import com.syt.yygh.sms.service.SmsService;
import com.syt.yygh.sms.utils.RandomUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author: wangdi
 * @Date: 2021/3/20
 */
@Api(tags = "短信发送")
@RestController
@RequestMapping("/api/sms")
public class SmsApiController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable String phone){
        // 从redis中获取验证码，获取到就ok
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)){
            return Result.ok().message("没有短信服务，模拟将验证码返回"+code);
        }
        // 获取不到，就用短信发送
        code = RandomUtil.getSixBitRandom();
        boolean isSend = smsService.send(phone,code);
        // 生成验证码放到redis，并设置过期时间
        if(isSend){
            redisTemplate.opsForValue().set(phone,code,3, TimeUnit.MINUTES);
            return Result.ok().message("没有短信服务，模拟将验证码返回"+code);
        }else{
            return Result.fail().message("发送短信失败");
        }
    }

}
