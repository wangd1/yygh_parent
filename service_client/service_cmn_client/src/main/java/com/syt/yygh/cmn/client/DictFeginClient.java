package com.syt.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Wangdi
 */
@FeignClient("service-cmn")
@Service
public interface DictFeginClient {

    /**
     * 根据dictCode和value查询字段名称
     * @param dictCode dictCode
     * @param value value
     * @return mc
     */
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    public String getDictName(@PathVariable("dictCode") String dictCode, @PathVariable("value") String value);

    /**
     * 根据value查询字段名称
     * @param value value
     * @return mc
     */
    @GetMapping("/admin/cmn/dict/getName/{value}")
    public String getDictName(@PathVariable("value") String value);

}
