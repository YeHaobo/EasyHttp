package com.yhb.httputils.download;

import com.yhb.httputils.download.group.HttpGroupDownloader;
import com.yhb.httputils.download.multiple.HttpMultipleDownloader;
import com.yhb.httputils.download.single.HttpSingleDownloader;
import com.zhy.http.okhttp.OkHttpUtils;

/**下载器基类*/
public class HttpDownloader {

    /**创建单个文件下载器*/
    public static HttpSingleDownloader createSingle(){
        return new HttpSingleDownloader();
    }

    /**创建多个文件下载器*/
    public static HttpMultipleDownloader createMultiple(){
        return new HttpMultipleDownloader();
    }

    /**创建多组多文件下载器*/
    public static HttpGroupDownloader createGroup(){
        return new HttpGroupDownloader();
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
        OkHttpUtils.getInstance().cancelTag(this);
    }

}