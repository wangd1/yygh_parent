package com.syt.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.syt.yygh.cmn.listener.DictListener;
import com.syt.yygh.cmn.mapper.DictMapper;
import com.syt.yygh.cmn.service.DictService;
import com.syt.yygh.model.cmn.Dict;
import com.syt.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wangdi
 * @Date: 2021/3/11
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict>
        implements DictService {

    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        List<Dict> list = baseMapper.selectList(queryWrapper);
        list.forEach(dict -> {
            dict.setHasChildren(isChildren(dict.getId()));
        });
        return list;
    }

    @Override
    public void exportData(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict-"+System.currentTimeMillis();
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

        List<Dict> dictList = baseMapper.selectList(null);
        List<DictEeVo> dictEeList = new ArrayList<>();
        dictList.forEach(dict -> {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict,dictEeVo);
            dictEeList.add(dictEeVo);
        });
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictEeList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @CacheEvict(value = "dict",allEntries = true)
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDictName(String dictCode, String value) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        // 直接根据value查询
        if(StringUtils.isEmpty(dictCode)){
            queryWrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(queryWrapper);
            if(dict!=null){
                return dict.getName();
            }else{
                return "";
            }
        }else{
            // 根据dictCode查询id，根据id和value查询
            Dict dict = this.getDictByDictCode(dictCode);
            Long id = dict.getId();
            queryWrapper.eq("parent_id",id).eq("value",value);
            Dict finalDict = baseMapper.selectOne(queryWrapper);
            if(finalDict!=null){
                return finalDict.getName();
            }else{
                return "";
            }
        }
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        // 根据dictCode获取id
        Dict dict = getDictByDictCode(dictCode);
        // 根据id获取子节点
        return findChildData(dict.getId());
    }

    /**
     * 根据dictCode获取dict
     * @param dictCode code
     * @return dict
     */
    private Dict getDictByDictCode(String dictCode){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        // 根据dictCode查询id，根据id和value查询
        queryWrapper.eq("dict_code",dictCode);
        return baseMapper.selectOne(queryWrapper);
    }


    /**
     * 是否有子节点
     * @param id id
     * @return 结果
     */
    private boolean isChildren(Long id){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count>0;
    }
}
