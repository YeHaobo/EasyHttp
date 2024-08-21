package com.yhb.httputils.config;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Request;
import okhttp3.Response;

/**http基础配置接口*/
public interface HttpConfig {

    /**超时时间*/
    int timeout();

    /**超时时间单位*/
    TimeUnit timeUnit();

    /**缓存文件夹路径*/
    String cachePath();

    /**缓存文件编码格式*/
    String cacheCharset();

    /**缓存文件后缀名*/
    String cachePostfix();

    /**缓存文件全名*/
    String cacheFileName(Request request);

    /**全局请求头*/
    Map<String, String> headers();

    /**数据解析*/
    <T> T parseResponse(Response response, Class<T> clz) throws Exception;

    /**下载完成的文件名*/
    String downloadedName(String url);

    /**下载时文件名后缀*/
    String downloadingPostfix();

    /**下载时的文件全名*/
    String downloadingFileName(String url);

}