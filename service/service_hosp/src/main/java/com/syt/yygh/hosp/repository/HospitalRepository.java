package com.syt.yygh.hosp.repository;

import com.syt.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Wangdi
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {

    /**
     * 判断是否存在医院
     * @param code 医院编号
     * @return 姐u共
     */
    Hospital getHospitalByHoscode(String code);
}
