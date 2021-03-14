package com.syt.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syt.yygh.model.cmn.Dict;
import com.syt.yygh.model.hosp.HospitalSet;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Wangdi
 */
public interface DictService extends IService<Dict> {
    /**
     * 根据id查询
     * @param id id
     * @return jg
     */
    List<Dict> findChildData(Long id);

    /**
     * 导出数据字典
     * @param response response
     */
    void exportData(HttpServletResponse response);

    /**
     * 导入数据字典
     * @param file 文件
     */
    void importData(MultipartFile file);

    /**
     * 根据dictCode和value查询字典
     * @param dictCode dictCode
     * @param value value
     * @return 名称
     */
    String getDictName(String dictCode, String value);

    /**
     * 根据dictCode查询子节点
     * @param dictCode dictCode
     * @return list
     */
    List<Dict> findByDictCode(String dictCode);
}
