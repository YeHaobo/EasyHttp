package com.yhb.httputils.download.multiple;

import android.os.Looper;
import com.yhb.httputils.download.HttpDownloadResult;
import java.util.List;

/**多个文件下载回调*/
public abstract class HttpMultipleDownloaderCallback {

    /**回调线程*/
    private Looper looper;

    /**构造*/
    public HttpMultipleDownloaderCallback(){
        this(Looper.myLooper());
    }
    /**构造*/
    public HttpMultipleDownloaderCallback(Looper looper){
        this.looper = looper;
    }

    /**回调线程*/
    public Looper looper(){
        return looper;
    }

    /**取消回调*/
    public void onCancle(){}
    /**进度回调*/
    public void inProgress(List<String> url, int index, long total, int progress){ }
    /**结果回调*/
    public abstract void onCallback(List<HttpDownloadResult> resultList);

}