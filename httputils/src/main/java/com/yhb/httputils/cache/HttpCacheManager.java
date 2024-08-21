package com.yhb.httputils.cache;

import android.os.Handler;
import android.os.HandlerThread;
import com.alibaba.fastjson.JSONObject;
import com.yhb.httputils.HttpManager;
import com.yhb.httputils.config.HttpConfig;
import java.io.File;
import java.io.RandomAccessFile;
import okhttp3.Request;

/**缓存文件管理器*/
public class HttpCacheManager {

    /**单例模式*/
    private static HttpCacheManager instance;
    public static HttpCacheManager getInstance(){
        if(instance == null){
            synchronized (HttpCacheManager.class){
                if(instance == null){
                    instance = new HttpCacheManager();
                }
            }
        }
        return instance;
    }

    /**缓存读写消息线程名*/
    private static final String HANDLER_NAME = "httpCache";
    /**缓存读写消息handler*/
    private Handler handler;

    /**私有构造*/
    private HttpCacheManager() {
        HandlerThread handlerThread = new HandlerThread(HANDLER_NAME);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    /**缓存文件获取*/
    private File cacheFile(Request request){
        HttpConfig config = HttpManager.config();
        File folder = new File(config.cachePath());
        if(!folder.exists()){
            folder.mkdirs();
        }
        return new File(folder, config.cacheFileName(request));
    }

    /**写入缓存*/
    public <T> void write(final Request request, final T data, final HttpCacheResult<T> result){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    File file = cacheFile(request);
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    String json = JSONObject.toJSONString(data);
                    String charset = HttpManager.config().cacheCharset();
                    RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                    raf.setLength(0);//删除所有行/清空所有数据后写入
                    raf.write(json.getBytes(charset));
                    raf.close();
                    result.onResult(true, data, "write [" + request.url().toString() + "] success");
                }catch (Exception e){
                    e.printStackTrace();
                    result.onResult(false, null, "write [" + request.url().toString() + "] " + e.getMessage());
                }
            }
        });
    }

    /**读取缓存*/
    public <T> void read(final Request request, final Class<T> clz, final HttpCacheResult<T> result){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = cacheFile(request);
                    if(!file.exists()){
                        throw new Exception("not find cache file!");
                    }
                    String charset = HttpManager.config().cacheCharset();
                    RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                    byte[] bytes = new byte[(int)raf.length()];//初始化字符数组长度
                    raf.read(bytes);//读取数据到byte
                    raf.close();
                    String json = new String(bytes, charset);
                    T data = JSONObject.parseObject(json, clz);
                    result.onResult(true, data, "read [" + request.url().toString() + "] success");
                } catch (Exception e) {
                    e.printStackTrace();
                    result.onResult(false, null, "read [" + request.url().toString() + "] " + e.getMessage());
                }
            }
        });
    }

}