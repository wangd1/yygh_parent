package com.syt.yygh.sms.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Wangdi
 */
public interface OssFileService {
    /**
     * 文件上传
     * @param file
     * @return
     */
    String upload(MultipartFile file) throws IOException;
}
