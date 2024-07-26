package com.yhb.ossutils.config;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**oss配置抽象基类*/
public abstract class OssBaseConfig {

    /**TAG*/
    private static final String TAG = "OssBaseConfig";

    /**传输超时时间（单位：毫秒）*/
    public abstract int socketTimeout();

    /**连接超时时间（单位：毫秒）*/
    public abstract int connectionTimeout();

    /**最大重试次数*/
    public abstract int maxErrorRetry();

    /**最大并发数*/
    public abstract int maxConcurrentRequest();

    /**STS鉴权服务地址*/
    public abstract String authServerUrl();

    /**完整域*/
    public abstract String point();

    /**二级域*/
    public abstract String endPoint();

    /**存储空间*/
    public abstract String bucketName();

    /**存储空间文件路径*/
    public abstract String objectKey(File file);

    /**上传前的缓存文件路径*/
    public abstract String cachePath();

    /**MD5值转换*/
    public String md5(File file) {
        if (file == null || !file.exists() || !file.isFile()) return "";
        try {
            InputStream in = new FileInputStream(file);
            byte[] imageBytes = new byte[in.available()];
            in.read(imageBytes);
            in.close();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(imageBytes);
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

}