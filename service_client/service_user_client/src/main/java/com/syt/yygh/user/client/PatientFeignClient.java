package com.syt.yygh.user.client;

import com.syt.yygh.common.result.Result;
import com.syt.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Wangdi
 */
@FeignClient("service-user")
@Service
public interface PatientFeignClient {

    @GetMapping("/api/user/patient/inner/get/{id}")
    Patient getPatientOrder(@PathVariable("id") Long id);

}
