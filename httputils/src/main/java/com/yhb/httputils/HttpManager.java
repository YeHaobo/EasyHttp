package com.yhb.httputils;

import com.yhb.httputils.config.HttpBaseConfig;
import com.yhb.httputils.config.HttpDefaultConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import okhttp3.OkHttpClient;

/**http管理器*/
public class HttpManager {

    /**TAG*/
    private static final String TAG = "HttpManager";

    /**配置信息实体*/
    private static HttpBaseConfig httpConfig;

    /**初始化*/
    public static void initialize(){
        initialize(new HttpDefaultConfig());
    }

    /**初始化*/
    public static void initialize(HttpBaseConfig config){
        httpConfig = config;
        OkHttpUtils.initClient(new OkHttpClient
                .Builder()
                .connectTimeout(httpConfig.timeout(), httpConfig.timeUnit())//默认连接超时
                .writeTimeout(httpConfig.timeout(), httpConfig.timeUnit())//连接的默认写超时
                .readTimeout(httpConfig.timeout(), httpConfig.timeUnit())//连接的默认读取超时
                .build());
    }

    /**获取配置信息实体*/
    public static HttpBaseConfig config(){
        return httpConfig;
    }

}