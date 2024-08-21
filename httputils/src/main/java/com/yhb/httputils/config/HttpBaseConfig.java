package com.yhb.httputils.config;

import android.os.Environment;
import android.util.Log;
import com.alibaba.fastjson.JSONObject;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**http基础配置*/
public class HttpBaseConfig implements HttpConfig {

    /**TAG*/
    private static final String TAG = "HttpBaseConfig";

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

    /**MD5值转换*/
    public String md5(String source) {
        try {
            if (source == null) return "";
            byte[] bytes = source.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytes);
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

    /**body值获取*/
    public String body(RequestBody body){
        try {
            if(body == null) return "";
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            Charset charset = null;
            MediaType contentType = body.contentType();
            if(contentType != null){
                charset = contentType.charset();//优先使用body的contentType编码
            }
            if(charset == null){
                charset = StandardCharsets.UTF_8;//无编码默认UTF_8
            }
            return buffer.readString(charset);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "body error: " + e.getMessage());
            return "";
        }
    }

}