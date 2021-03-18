package com.syt.yygh.cmn.controller;

import com.syt.yygh.cmn.service.DictService;
import com.syt.yygh.common.result.Result;
import com.syt.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: wangdi
 * @Date: 2021/3/13
 */
@Api(tags = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     * 根据id，查询子数据
     * @param id id
     * @return 结果
     */
    @ApiOperation("根据id查询子数据")
    @GetMapping("/findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    /**
     * 导出excel
     * @param response
     * @return
     */
    @ApiOperation("导出excel")
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response){
        dictService.exportData(response);
    }

    /**
     * 导入数据
     */
    @PostMapping("importData")
    public void importData(MultipartFile file){
        dictService.importData(file);
    }

    /**
     * 根据dictCode和value查询字段名称
     * @param dictCode dictCode
     * @param value value
     * @return mc
     */
    @GetMapping("getName/{dictCode}/{value}")
    public String getDictName(@PathVariable String dictCode,@PathVariable String value){
        return dictService.getDictName(dictCode,value);
    }

    /**
     * 根据value查询字段名称
     * @param value value
     * @return mc
     */
    @GetMapping("getName/{value}")
    public String getDictName(@PathVariable String value){
        return dictService.getDictName("",value);
    }

    @ApiOperation("根据dictCode获取子节点")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode){
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }
}
