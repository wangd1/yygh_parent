package com.syt.yygh.order.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syt.yygh.common.result.Result;
import com.syt.yygh.common.utils.AuthContextHolder;
import com.syt.yygh.model.order.OrderInfo;
import com.syt.yygh.order.service.OrderService;
import com.syt.yygh.vo.order.OrderQueryVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: wangdi
 * @Date: 2021/4/4
 */
@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderApiController {

    @Resource
    private OrderService orderService;

    /**
     * 生成订单
     * @param scheduleId 排班id
     * @param patientId 就诊人id
     * @return 结果
     */
    @PostMapping("/auth/submitOrder/{scheduleId}/{patientId}")
    public Result saveOrder(@PathVariable("scheduleId") String scheduleId,@PathVariable("patientId") Long patientId){
        Long orderId = orderService.saveOrder(scheduleId,patientId);
        return Result.ok(orderId);
    }

    //根据订单id查询订单详情
    @GetMapping("auth/getOrders/{orderId}")
    public Result getOrders(@PathVariable String orderId) {
        OrderInfo orderInfo = orderService.getOrder(orderId);
        return Result.ok(orderInfo);
    }

    //订单列表（条件查询带分页）
    @GetMapping("auth/{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       OrderQueryVo orderQueryVo, HttpServletRequest request) {
        //设置当前用户id
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));
        Page<OrderInfo> pageParam = new Page<>(page,limit);
        IPage<OrderInfo> pageModel =
                orderService.selectPage(pageParam,orderQueryVo);
        return Result.ok(pageModel);
    }


}
