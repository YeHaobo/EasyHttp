package com.yhb.ossutils.upload.single;

import android.os.Handler;
import android.os.Looper;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.yhb.ossutils.OssManager;
import com.yhb.ossutils.upload.OssUploader;
import com.yhb.ossutils.upload.OssUploaderResult;
import com.yhb.ossutils.config.OssBaseConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * OSS单文件上传器
 * https://ayqhl-cloud.oss-cn-hangzhou.aliyuncs.com/prod/device/8c:fc:a0:26:3a:f0/202307/f53124c0-1833-4fdb-a7ed-4ea4fec0fecf.png
 */
public class OssSingleUploader extends OssUploader {

    /**文件复制*/
    private File copyFile(File fromFile, String dirPath){
        if(fromFile == null || !fromFile.exists() || !fromFile.isFile()){
            return null;
        }
        File dirFile = new File(dirPath);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        File toFile = new File(dirFile, fromFile.getName());
        if(toFile.exists()){
            toFile.delete();
        }
        try {
            FileInputStream in = new FileInputStream(fromFile);
            FileOutputStream out = new FileOutputStream(toFile);
            byte[] bt = new byte[1024];
            int c;
            while ((c = in.read(bt)) > 0) {
                out.write(bt, 0, c);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return toFile;
    }

    /**同步*/
    public OssUploaderResult sync(File file){
        if(isCancel()){
            return new OssUploaderResult(null, file, "file upload cancle");
        }
        OssBaseConfig config = OssManager.config();//获取配置
        File uploadFile = copyFile(file, config.cachePath());//先拷贝文件
        if(uploadFile == null || !uploadFile.exists()) {
            return new OssUploaderResult(null, file, "file copy failed");
        }
        String objectKey = config.objectKey(uploadFile);
        PutObjectRequest putRequest = new PutObjectRequest(config.bucketName(), objectKey, uploadFile.getPath());//构造请求
        putRequest.setCRC64(OSSRequest.CRC64Config.YES);//开启数据传输的完整性验证
        try{
            OssManager.client().putObject(putRequest);
        }catch (Exception e){
            e.printStackTrace();
            if(uploadFile.exists()){
                uploadFile.delete();
            }
            return new OssUploaderResult(null, file, e.getMessage());
        }
        if(uploadFile.exists()){
            uploadFile.delete();
        }
        return new OssUploaderResult(config.point() + "/" + objectKey, file, "file is uploaded");
    }

    /**异步*/
    public void async(final File file, Looper copyLooper, final OssSingleUploaderCallback callback){
        if(isCancel()){
            if(callback != null) callback.exeCancle();
            return;
        }
        new Handler(copyLooper).post(new Runnable() {
            @Override
            public void run() {
                final OssBaseConfig config = OssManager.config();//获取配置
                final File uploadFile = copyFile(file, config.cachePath());//先拷贝文件
                if(uploadFile == null || !uploadFile.exists()) {
                    if(callback != null) callback.exeCallback(new OssUploaderResult(null, file, "file copy failed"));
                    return;
                }
                final String objectKey = config.objectKey(uploadFile);
                PutObjectRequest putRequest = new PutObjectRequest(config.bucketName(), objectKey, uploadFile.getPath());//构造请求
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
                        if(uploadFile.exists()) uploadFile.delete();
                        if(callback != null) callback.exeCallback(new OssUploaderResult(null, file, "[client] " + (clientException != null ? clientException.getMessage() : "none") + "\n[service] " + (serviceException != null ? serviceException.getMessage() : "none")));
                    }
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        if(uploadFile.exists()) uploadFile.delete();
                        if(callback != null) callback.exeCallback(new OssUploaderResult(config.point() + "/" + objectKey, file, "file is uploaded"));
                    }
                });
            }
        });
    }

}