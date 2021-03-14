package com.syt.yygh.hosp.repository;

import com.syt.yygh.model.hosp.Department;
import com.syt.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

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
}
