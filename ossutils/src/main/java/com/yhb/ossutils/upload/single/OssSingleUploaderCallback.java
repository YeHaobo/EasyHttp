package com.yhb.ossutils.upload.single;

import android.os.Handler;
import android.os.Looper;
import com.yhb.ossutils.upload.OssUploaderResult;

/**单个文件上传回调*/
public abstract class OssSingleUploaderCallback {

    /**回调线程*/
    private Handler handler;

    /**构造*/
    public OssSingleUploaderCallback(){
        this(null);
    }
    /**构造*/
    public OssSingleUploaderCallback(Looper looper){
        this.handler = new Handler(looper != null ? looper : Looper.getMainLooper());
    }

    /**切换线程回调进度*/
    public void exeProgress(final long total, final int progress){
        handler.post(new Runnable() {
            @Override
            public void run() {
                inProgress(total, progress);
            }
        });
    }
    /**切换线程回调结果*/
    public void exeCallback(final OssUploaderResult result){
        handler.post(new Runnable() {
            @Override
            public void run() {
                onCallback(result);
            }
        });
    }
    /**切换线程回调取消*/
    public void exeCancle(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                onCancle();
            }
        });
    }

    /**进度回调*/
    public void inProgress(long total, int progress){ }
    /**结果回调*/
    public abstract void onCallback(OssUploaderResult result);
    /**取消回调*/
    public void onCancle(){}

}