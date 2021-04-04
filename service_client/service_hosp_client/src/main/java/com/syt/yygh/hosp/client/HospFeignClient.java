package com.syt.yygh.hosp.client;

import com.syt.yygh.common.result.Result;
import com.syt.yygh.vo.hosp.ScheduleOrderVo;
import com.syt.yygh.vo.order.SignInfoVo;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Wangdi
 */
@FeignClient("service-hosp")
@Service
public interface HospFeignClient {

    /**
     * 根据排班id获取排班信息
     * @param scheduleId 排班id
     * @return 信息
     */
    @GetMapping("/api/hosp/hospital/inner/getScheduleOrder/{scheduleId}")
    ScheduleOrderVo getScheduleOrder(@PathVariable String scheduleId);

    /**
     * 获取医院签名信息
     * @param hoscode 医院编号
     * @return 结果
     */
    @GetMapping("/api/hosp/hospital/inner/getSignInfoVo/{hoscode}")
    SignInfoVo getSignInfoVo(@PathVariable("hoscode") String hoscode);

}
