package com.syt.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.syt.yygh.model.order.OrderInfo;
import com.syt.yygh.vo.order.OrderQueryVo;

/**
 * @Author: wangdi
 * @Date: 2021/4/4
 */
public interface OrderService extends IService<OrderInfo> {

    /**
     * 生成订单
     * @param scheduleId 排班id
     * @param patientId 就诊人id
     * @return 结果
     */
    Long saveOrder(String scheduleId, Long patientId);

    OrderInfo getOrder(String orderId);

    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);
}
