package com.yhb.ossutils.config;

/**oss基础配置接口*/
public interface OssConfig {

    /**传输超时时间（单位：毫秒）*/
    int socketTimeout();

    /**连接超时时间（单位：毫秒）*/
    int connectionTimeout();

    /**最大重试次数*/
    int maxErrorRetry();

    /**最大并发数*/
    int maxConcurrentRequest();

    /**STS鉴权服务地址*/
    String authServerUrl();

    /**二级域*/
    String endPoint();

}