package com.syt.yygh.sms.receiver;

import com.rabbitmq.client.Channel;
import com.syt.common.rabbit.constant.MqConst;
import com.syt.yygh.sms.service.SmsService;
import com.syt.yygh.vo.sms.SmsVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: wangdi
 * @Date: 2021/4/4
 */
@Component
public class SmsReceiver {

    @Resource
    private SmsService smsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_SMS_ITEM, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_SMS),
            key = {MqConst.ROUTING_SMS_ITEM}
    ))
    public void send(SmsVo smsVo, Message message, Channel channel) {
        smsService.send(smsVo);
    }


}
