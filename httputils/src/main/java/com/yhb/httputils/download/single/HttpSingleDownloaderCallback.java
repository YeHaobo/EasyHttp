package com.yhb.httputils.download.single;

import android.os.Handler;
import android.os.Looper;
import com.yhb.httputils.download.HttpDownloadResult;

/**单个文件下载回调*/
public abstract class HttpSingleDownloaderCallback {

    /**回调线程*/
    private Handler handler;

    /**构造*/
    public HttpSingleDownloaderCallback(){
        this(Looper.myLooper());
    }
    /**构造*/
    public HttpSingleDownloaderCallback(Looper looper){
        this.handler = new Handler(looper);
    }

    /**进度切换线程回调*/
    public void exeProgress(final long total, final int progress){
        handler.post(new Runnable() {
            @Override
            public void run() {
                inProgress(total, progress);
            }
        });
    }
    /**结果切换线程回调*/
    public void exeCallback(final HttpDownloadResult result){
        handler.post(new Runnable() {
            @Override
            public void run() {
                onCallback(result);
            }
        });
    }
    /**取消切换线程回调*/
    public void exeCancle(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                onCancle();
            }
        });
    }

    /**进度回调*/
    public void inProgress(long total, int progress){

    }
    /**结果回调*/
    public abstract void onCallback(HttpDownloadResult result);
    /**取消回调*/
    public void onCancle(){}

}