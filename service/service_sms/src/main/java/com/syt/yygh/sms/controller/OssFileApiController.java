package com.syt.yygh.sms.controller;

import com.syt.yygh.common.result.Result;
import com.syt.yygh.sms.service.OssFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author: wangdi
 * @Date: 2021/4/3
 */
@RestController
@RequestMapping("/api/sms/oss/file")
@Api(tags = "文件上传接口")
public class OssFileApiController {

    @Resource
    private OssFileService ossFileService;


    /**
     * 阿里云文件上传
     * @param file 文件
     * @return 地址
     */
    @PostMapping("/fileUpload")
    @ApiOperation("阿里云文件上传")
    public Result fileUpload(MultipartFile file){
        String url = null;
        try {
            url = ossFileService.upload(file);
        } catch (IOException e) {
            return Result.ok(e.getMessage());
        }
        return Result.ok(url);
    }

}
