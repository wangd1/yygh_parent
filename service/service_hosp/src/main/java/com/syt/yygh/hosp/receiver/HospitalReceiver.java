package com.syt.yygh.hosp.receiver;

import com.rabbitmq.client.Channel;
import com.syt.common.rabbit.constant.MqConst;
import com.syt.common.rabbit.service.RabbitService;
import com.syt.yygh.hosp.service.ScheduleService;
import com.syt.yygh.model.hosp.Schedule;
import com.syt.yygh.vo.order.OrderMqVo;
import com.syt.yygh.vo.sms.SmsVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author: wangdi
 * @Date: 2021/4/4
 */
@Component
public class HospitalReceiver {
    @Resource
    private ScheduleService scheduleService;

    @Resource
    private RabbitService rabbitService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ORDER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
            key = {MqConst.ROUTING_ORDER}
    ))
    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) throws IOException {
        //下单成功更新预约数
        Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
        schedule.setReservedNumber(orderMqVo.getReservedNumber());
        schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
        scheduleService.update(schedule);
        //发送短信
        SmsVo smsVo = orderMqVo.getMsmVo();
        if(null != smsVo) {
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_SMS, MqConst.ROUTING_SMS_ITEM, smsVo);
        }
    }

}
