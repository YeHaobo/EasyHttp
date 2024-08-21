package com.yhb.ossutils.upload;

import android.util.Log;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**上传回调实体*/
public class OssUploaderResult {
    /**TAG*/
    private static final String TAG = "OssUploaderResult";

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
        this.md5 = file != null && file.exists() ? md5(file) : "";
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