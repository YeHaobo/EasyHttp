package com.yhb.httputils.config;

import android.os.Environment;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Request;
import okhttp3.Response;

/**默认http配置*/
public class HttpDefaultConfig extends HttpBaseConfig {

    /**TAG*/
    private static final String TAG = "HttpDefaultConfig";

    /**超时时间*/
    @Override
    public int timeout() {
        return 10;
    }

    /**超时时间单位*/
    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;//秒
    }

    /**缓存文件夹路径*/
    @Override
    public String cachePath() {
        return Environment.getExternalStorageDirectory().getPath() + "/EasyHttp";
    }

    /**缓存文件编码格式*/
    @Override
    public String cacheCharset() {
        return "UTF-8";
    }

    /**缓存文件后缀名*/
    @Override
    public String cachePostfix() {
        return ".txt";
    }

    /**缓存文件全名*/
    @Override
    public String cacheFileName(Request request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("method", request.method());
        jsonObject.put("url", request.url().toString());
        jsonObject.put("headers", request.headers().toString());
        jsonObject.put("body", body(request.body()));
        String info = jsonObject.toJSONString();
        String md5 = md5(info);
        return md5 + cachePostfix();
    }

    /**全局请求头*/
    @Override
    public Map<String, String> headers() {
        return new HashMap<>();
    }

    /**数据解析*/
    @Override
    public <T> T parseResponse(Response response, Class<T> clz) throws Exception {
        String data = response.body().string();//获取body
        JSONObject jsonObject = JSONObject.parseObject(data);//转换JSONObject
        if(jsonObject.getInteger("code") != 200){//数据无效
            throw new Exception(data);//抛出异常
        }else{//数据有效
            return JSONObject.parseObject(data, clz);//返回对象
        }
    }

    /**下载完成的文件名*/
    @Override
    public String downloadedName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**下载时文件名后缀*/
    @Override
    public String downloadingPostfix() {
        return ".download";
    }

    /**下载时的文件全名*/
    @Override
    public String downloadingFileName(String url) {
        return downloadedName(url) + downloadingPostfix();
    }


}