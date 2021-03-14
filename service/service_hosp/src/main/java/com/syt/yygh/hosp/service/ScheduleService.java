package com.syt.yygh.hosp.service;

import com.syt.yygh.model.hosp.Schedule;
import com.syt.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author Wangdi
 */
public interface ScheduleService {
    /**
     * 上传排班
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 分页查询排班
     * @param page
     * @param limit
     * @param scheduleQueryVo
     * @return
     */
    Page<Schedule> findPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    /**
     * 删除排班
     * @param code 医院编号
     * @param hosScheduleId 排班编号
     */
    void removeSchedule(String code, String hosScheduleId);
}
