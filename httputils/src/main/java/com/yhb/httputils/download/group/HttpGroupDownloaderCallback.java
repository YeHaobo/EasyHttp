package com.yhb.httputils.download.group;

import android.os.Looper;
import java.util.List;

/**多组多文件下载回调*/
public abstract class HttpGroupDownloaderCallback {

    /**回调线程*/
    private Looper looper;

    /**构造*/
    public HttpGroupDownloaderCallback(){
        this(Looper.myLooper());
    }
    /**构造*/
    public HttpGroupDownloaderCallback(Looper looper){
        this.looper = looper;
    }

    /**回调线程*/
    public Looper looper(){
        return looper;
    }

    /**取消回调*/
    public void onCancle(){}
    /**进度回调*/
    public void inProgress(List<HttpGroupDownloadRes> groupResList, int pathIndex, int urlIndex, long total, int progress){ }
    /**结果回调*/
    public abstract void onCallback(List<HttpGroupDownloadResult> groupResultList);

}