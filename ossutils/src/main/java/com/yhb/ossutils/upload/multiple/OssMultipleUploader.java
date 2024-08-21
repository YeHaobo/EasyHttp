package com.yhb.ossutils.upload.multiple;

import com.yhb.ossutils.config.OssUploadConfig;
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
    public List<OssUploaderResult> sync(OssUploadConfig config, List<File> fileList){
        return sync(config, fileList, 0, new ArrayList<OssUploaderResult>());
    }
    /**同步*/
    private List<OssUploaderResult> sync(OssUploadConfig config, List<File> fileList, int index, List<OssUploaderResult> resultList){
        if(fileList.size() <= index){
            return resultList;
        }
        OssUploaderResult result = sync(config, fileList.get(index));
        resultList.add(result);
        return sync(config, fileList, index + 1, resultList);
    }

    /**异步*/
    public void async(OssUploadConfig config, List<File> fileList, OssMultipleUploaderCallback callback){
        async(config, fileList, 0, new ArrayList<OssUploaderResult>(), callback);
    }
    /**异步*/
    private void async(final OssUploadConfig config, final List<File> fileList, final int index, final List<OssUploaderResult> resultList, final OssMultipleUploaderCallback callback){
        if(fileList.size() <= index){
            callback.onCallback(resultList);
            return;
        }
        async(config, fileList.get(index), new OssSingleUploaderCallback(callback.looper()) {
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
                async(config, fileList, index + 1, resultList, callback);
            }
        });
    }

}