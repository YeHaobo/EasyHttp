package com.yhb.test;

import com.yhb.ossutils.config.OssUploadConfig;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class OssDefaultUploadConfig implements OssUploadConfig {

    private SimpleDateFormat format;
    private String mac;

    public OssDefaultUploadConfig(String mac) {
        this.mac = mac;
        this.format = new SimpleDateFormat("yyyyMM", Locale.getDefault());
    }

    @Override
    public String point() {
        return "https://ayqhl-cloud.oss-cn-hangzhou.aliyuncs.com";
    }


    @Override
    public String bucketName() {
        return "ayqhl-cloud";
    }

    @Override
    public String objectKey(File file) {
        String postfix = file.getName().substring(file.getName().lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        return "prod/device/" + mac + "/" + format.format(new Date(System.currentTimeMillis())) + "/" + uuid + postfix;
    }

}