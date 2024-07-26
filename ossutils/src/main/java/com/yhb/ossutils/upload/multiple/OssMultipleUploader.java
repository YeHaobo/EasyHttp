package com.yhb.ossutils.upload.multiple;

import android.os.Looper;
import com.yhb.ossutils.upload.OssUploaderResult;
import com.yhb.ossutils.upload.single.OssSingleUploader;
import com.yhb.ossutils.upload.single.OssSingleUploaderCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * OSS多文件上传器
 * https://ayqhl-cloud.oss-cn-hangzhou.aliyuncs.com/prod/device/8c:fc:a0:26:3a:f0/202307/f53124c0-1833-4fdb-a7ed-4ea4fec0fecf.png
 */
public class OssMultipleUploader extends OssSingleUploader {

    /**同步*/
    public List<OssUploaderResult> sync(List<File> fileList){
        return sync(fileList, 0, new ArrayList<OssUploaderResult>());
    }
    /**同步*/
    private List<OssUploaderResult> sync(List<File> fileList, int index, List<OssUploaderResult> resultList){
        if(fileList.size() <= index){
            return resultList;
        }
        OssUploaderResult result = sync(fileList.get(index));
        resultList.add(result);
        return sync(fileList, index + 1, resultList);
    }

    /**异步*/
    public void async(List<File> fileList, Looper copyLooper, OssMultipleUploaderCallback callback){
        async(fileList, 0, new ArrayList<OssUploaderResult>(), copyLooper, callback);
    }
    /**异步*/
    private void async(final List<File> fileList, final int index, final List<OssUploaderResult> resultList, final Looper copyLooper, final OssMultipleUploaderCallback callback){
        if(fileList.size() <= index){
            callback.onCallback(resultList);
            return;
        }
        async(fileList.get(index), copyLooper, new OssSingleUploaderCallback(callback.looper()) {
            @Override
            public void onCancle() {
                callback.onCancle();
            }
            @Override
            public void inProgress(long total, int progress) {
                callback.inProgress(fileList, index, total, progress);
            }
            @Override
            public void onCallback(OssUploaderResult result) {
                resultList.add(result);
                async(fileList, index + 1, resultList, copyLooper, callback);
            }
        });
    }

}