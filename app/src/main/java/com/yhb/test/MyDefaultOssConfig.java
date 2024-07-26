package com.yhb.test;

import android.os.Environment;
import com.yhb.ossutils.config.OssBaseConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class MyDefaultOssConfig extends OssBaseConfig {

    private SimpleDateFormat format;

    public MyDefaultOssConfig() {
        this.format = new SimpleDateFormat("yyyyMM", Locale.getDefault());
    }

    @Override
    public int socketTimeout() {
        return 15 * 1000;
    }

    @Override
    public int connectionTimeout() {
        return 15 * 1000;
    }

    @Override
    public int maxErrorRetry() {
        return 2;
    }

    @Override
    public int maxConcurrentRequest() {
        return 5;
    }

    @Override
    public String authServerUrl() {
        return "";
    }

    @Override
    public String point() {
        return "https://ayqhl-cloud.oss-cn-hangzhou.aliyuncs.com";
    }

    @Override
    public String endPoint() {
        return "https://oss-cn-hangzhou.aliyuncs.com";
    }

    @Override
    public String bucketName() {
        return "ayqhl-cloud";
    }

    @Override
    public String objectKey(File file) {
        String postfix = file.getName().substring(file.getName().lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        return "prod/device/" + mac() + "/" + format.format(new Date(System.currentTimeMillis())) + "/" + uuid + postfix;
    }

    @Override
    public String cachePath() {
        return Environment.getExternalStorageDirectory().getPath() + "/test/upload";
    }

    /**获取mac地址*/
    public static String mac() {
        String mac = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("sys/class/net/eth0/address"));
            mac = reader.readLine();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac.trim();
    }

}