package com.yhb.httputils.download;

import android.util.Log;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**下载回调实体*/
public class HttpDownloadResult {
    /**TAG*/
    private static final String TAG = "HttpDownloadResult";

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
        this.md5 = result ? md5(file) : "";
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

    /**MD5值转换*/
    public String md5(File file) {
        if (file == null || !file.exists() || !file.isFile()) return "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            InputStream is = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int readBytes = 0;
            while ((readBytes = is.read(buffer)) != -1) {
                md5.update(buffer, 0, readBytes);
            }
            is.close();
            byte[] digest = md5.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "md5 error: " + e.getMessage());
            return "";
        }
    }

    /**转json*/
    public String toString(){
        return JSONObject.toJSONString(this);
    }

}