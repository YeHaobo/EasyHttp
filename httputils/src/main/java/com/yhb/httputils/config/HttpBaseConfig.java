package com.yhb.httputils.config;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**http配置抽象基类*/
public abstract class HttpBaseConfig {

    /**TAG*/
    private static final String TAG = "HttpBaseConfig";

    /**超时时间*/
    public abstract int timeout();

    /**超时时间单位*/
    public abstract TimeUnit timeUnit();

    /**缓存文件夹路径*/
    public abstract String cachePath();

    /**缓存文件编码格式*/
    public abstract String cacheCharset();

    /**缓存文件后缀名*/
    public abstract String cachePostfix();

    /**缓存文件全名*/
    public abstract String cacheFileName(Request request);

    /**全局请求头*/
    public abstract Map<String, String> headers();

    /**数据解析*/
    public abstract <T> T parseResponse(Response response, Class<T> clz) throws Exception;

    /**下载完成的文件名*/
    public abstract String downloadedName(String url);

    /**下载时文件名后缀*/
    public abstract String downloadingPostfix();

    /**下载时的文件全名*/
    public abstract String downloadingFileName(String url);

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