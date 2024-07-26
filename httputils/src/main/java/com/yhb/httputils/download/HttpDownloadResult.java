package com.yhb.httputils.download;

import com.alibaba.fastjson.JSONObject;
import com.yhb.httputils.HttpManager;
import java.io.File;

/**下载回调实体*/
public class HttpDownloadResult {
    /**是否成功*/
    private boolean result;
    /**信息*/
    private String msg;
    /**本地路径*/
    private String path;
    /**远程链接*/
    private String url;
    /**文件名*/
    private String name;
    /**大小*/
    private long size;
    /**md5*/
    private String md5;

    /**构造*/
    public HttpDownloadResult(File file, String url, String msg) {
        this.result = file != null && file.exists();
        this.msg = msg;
        this.path = result ? file.getPath() : "";
        this.url = url;
        this.name = result ? file.getName() : "";
        this.size = result ? file.length() : 0;
        this.md5 = result ? HttpManager.config().md5(file) : "";
    }

    public boolean isResult() {
        return result;
    }
    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }
    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String toString(){
        return JSONObject.toJSONString(this);
    }

}