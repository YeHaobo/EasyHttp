package com.yhb.ossutils.upload;

import com.yhb.ossutils.upload.multiple.OssMultipleUploader;
import com.yhb.ossutils.upload.single.OssSingleUploader;

/**OSS上传器基类*/
public class OssUploader {

    /**创建单个文件下载器*/
    public static OssSingleUploader createSingle(){
        return new OssSingleUploader();
    }

    /**创建多个文件下载器*/
    public static OssMultipleUploader createMultiple(){
        return new OssMultipleUploader();
    }

    /**是否取消*/
    private boolean isCancel = false;

    /**是否取消*/
    public boolean isCancel(){
        return isCancel;
    }

    /**取消下载*/
    public void cancel(){
        isCancel = true;
    }

}