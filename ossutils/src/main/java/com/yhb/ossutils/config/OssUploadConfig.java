package com.yhb.ossutils.config;

import java.io.File;

/**oss上传配置接口*/
public interface OssUploadConfig {

    /**完整域*/
    String point();

    /**存储空间*/
    String bucketName();

    /**存储空间文件路径*/
    String objectKey(File file);
    
}
