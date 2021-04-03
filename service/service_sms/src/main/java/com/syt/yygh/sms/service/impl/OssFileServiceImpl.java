package com.syt.yygh.sms.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.syt.yygh.sms.service.OssFileService;
import com.syt.yygh.sms.utils.ConstantOssUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * @Author: wangdi
 * @Date: 2021/4/3
 */
@Service
public class OssFileServiceImpl implements OssFileService {

    @Override
    public String upload(MultipartFile file) {

        String endpoint = ConstantOssUtils.ENDPOINT;
        String accessKeyId = ConstantOssUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantOssUtils.SECRET;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 填写网络流地址。
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        String fileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        fileName = uuid+fileName;

        String timeUrl = new DateTime().toString("yyyy/MM/dd");
        fileName = timeUrl+"/"+ fileName;

        ossClient.putObject(ConstantOssUtils.BUCKET, fileName, inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
        return ConstantOssUtils.FILE_URL+fileName;
    }
}
