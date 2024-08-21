package com.yhb.test;

import com.yhb.ossutils.config.OssConfig;

public class OssDefaultConfig implements OssConfig {

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
        return "***************************************************";
    }

    @Override
    public String endPoint() {
        return "https://oss-cn-hangzhou.aliyuncs.com";
    }

}
