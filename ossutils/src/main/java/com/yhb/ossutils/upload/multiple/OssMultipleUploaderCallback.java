package com.yhb.ossutils.upload.multiple;

import android.os.Looper;
import com.yhb.ossutils.upload.OssUploaderResult;
import java.io.File;
import java.util.List;

/**多个文件上传回调*/
public abstract class OssMultipleUploaderCallback {

    /**回调线程*/
    private Looper looper;

    /**构造*/
    public OssMultipleUploaderCallback(){
        this(Looper.myLooper());
    }
    /**构造*/
    public OssMultipleUploaderCallback(Looper looper){
        this.looper = looper;
    }

    /**回调线程*/
    public Looper looper(){
        return looper;
    }

    /**取消回调*/
    public void onCancle(){}
    /**进度回调*/
    public void inProgress(List<File> fileList, int index, long total, int progress){ }
    /**结果回调*/
    public abstract void onCallback(List<OssUploaderResult> resultList);

}
