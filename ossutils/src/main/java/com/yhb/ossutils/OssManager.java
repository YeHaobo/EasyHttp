package com.yhb.ossutils;

import android.content.Context;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.yhb.ossutils.config.OssBaseConfig;

/**OSS管理器*/
public class OssManager {

    /**TAG*/
    private static final String TAG = "OssManager";

    /**配置信息实体*/
    private static OssBaseConfig ossConfig;

    /**oss客户端*/
    private static OSSClient ossClient;

    /**初始化*/
    public static void initialize(Context applicationContext, OssBaseConfig config){
        ossConfig = config;
        //使用OSSAuthCredentialsProvider提供STS权限，token过期自动更新
        OSSAuthCredentialsProvider auth = new OSSAuthCredentialsProvider(config.authServerUrl());
        ClientConfiguration configuration = new ClientConfiguration();//客户端配置
        configuration.setConnectionTimeout(config.connectionTimeout()); //连接超时时间
        configuration.setSocketTimeout(config.socketTimeout()); //Socket超时时间
        configuration.setMaxConcurrentRequest(config.maxConcurrentRequest()); //最大并发请求数
        configuration.setMaxErrorRetry(config.maxErrorRetry()); //失败后最大重试次数
        ossClient = new OSSClient(applicationContext, config.endPoint(), auth, configuration);//初始化OSS客户端
    }

    /**获取配置信息实体*/
    public static OssBaseConfig config(){
        return ossConfig;
    }

    /**获取oss客户端*/
    public static OSSClient client(){
        return ossClient;
    }

}