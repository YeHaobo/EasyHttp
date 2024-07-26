package com.yhb.ossutils.upload;

import com.alibaba.fastjson.JSONObject;
import com.yhb.ossutils.OssManager;
import java.io.File;

/**上传回调实体*/
public class OssUploaderResult {
    /**是否成功*/
    private boolean result;
    /**信息*/
    private String msg;
    /**远程链接*/
    private String url;
    /**本地路径*/
    private String path;
    /**文件名*/
    private String name;
    /**大小*/
    private long size;
    /**md5*/
    private String md5;

    /**构造*/
    public OssUploaderResult(String url, File file, String msg) {
        this.result = url != null && !url.isEmpty();
        this.msg = msg;
        this.url = url;
        this.path = file != null && file.exists() ? file.getPath() : "";
        this.name = file != null && file.exists() ? file.getName() : "";
        this.size = file != null && file.exists() ? file.length() : 0;
        this.md5 = file != null && file.exists() ? OssManager.config().md5(file) : "";
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

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
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