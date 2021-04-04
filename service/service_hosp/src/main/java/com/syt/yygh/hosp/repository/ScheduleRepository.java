package com.syt.yygh.hosp.repository;

import com.syt.yygh.model.hosp.Department;
import com.syt.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Wangdi
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {
    /**
     * 查询
     * @param hoscode 医院编号
     * @param hosScheduleId 排版百年好
     * @return
     */
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    /**
     * 根据医院编号，科室编号，工作日期查询
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @param workDate 工作日期
     * @return 结果
     */
    List<Schedule> getScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date workDate);

    /**
     * 根据id获取
     * @param id id
     * @return 结果
     */
    Schedule getScheduleById(String id);
}
