package com.yhb.ossutils.upload.single;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.yhb.ossutils.OssManager;
import com.yhb.ossutils.config.OssUploadConfig;
import com.yhb.ossutils.upload.OssUploader;
import com.yhb.ossutils.upload.OssUploaderResult;
import java.io.File;

/**
 * OSS单文件上传器
 * https://ayqhl-cloud.oss-cn-hangzhou.aliyuncs.com/prod/device/8c:fc:a0:26:3a:f0/202307/f53124c0-1833-4fdb-a7ed-4ea4fec0fecf.png
 */
public class OssSingleUploader extends OssUploader {

    /**同步*/
    public OssUploaderResult sync(OssUploadConfig config, File file){
        if(isCancel()){
            return new OssUploaderResult(null, file, "file upload cancle");
        }
        String objectKey = config.objectKey(file);
        PutObjectRequest putRequest = new PutObjectRequest(config.bucketName(), objectKey, file.getPath());//构造请求
        putRequest.setCRC64(OSSRequest.CRC64Config.YES);//开启数据传输的完整性验证
        try{
            OssManager.client().putObject(putRequest);
        }catch (Exception e){
            e.printStackTrace();
            return new OssUploaderResult(null, file, e.getMessage());
        }
        return new OssUploaderResult(config.point() + "/" + objectKey, file, "file is uploaded");
    }

    /**异步*/
    public void async(final OssUploadConfig config, final File file, final OssSingleUploaderCallback callback){
        if(isCancel()){
            if(callback != null) callback.exeCancle();
            return;
        }
        final String objectKey = config.objectKey(file);
        PutObjectRequest putRequest = new PutObjectRequest(config.bucketName(), objectKey, file.getPath());//构造请求
        putRequest.setCRC64(OSSRequest.CRC64Config.YES);//开启数据传输的完整性验证
        putRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                if(callback != null) callback.exeProgress(totalSize, (int)(currentSize * 100 / totalSize));
            }
        });
        OssManager.client().asyncPutObject(putRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                if(callback != null) callback.exeCallback(new OssUploaderResult(null, file, "[client] " + (clientException != null ? clientException.getMessage() : "none") + "\n[service] " + (serviceException != null ? serviceException.getMessage() : "none")));
            }
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                if(callback != null) callback.exeCallback(new OssUploaderResult(config.point() + "/" + objectKey, file, "file is uploaded"));
            }
        });
    }

}