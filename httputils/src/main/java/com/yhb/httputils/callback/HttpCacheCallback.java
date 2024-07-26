package com.yhb.httputils.callback;

import android.os.Handler;
import android.os.Looper;
import com.yhb.httputils.HttpManager;
import com.yhb.httputils.cache.HttpCacheManager;
import com.yhb.httputils.cache.HttpCacheResult;
import com.zhy.http.okhttp.callback.Callback;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 缓存回调
 * 1、网络请求异常时自动读取缓存数据
 * 2、数据获取失败时自动读取缓存数据
 * 3、数据解析异常时自动读取缓存数据
 */
public abstract class HttpCacheCallback<T> extends Callback<T> {

    /**TAG*/
    private static final String TAG = "HttpCacheCallback";

    /**请求回调*/
    public abstract void callback(boolean result, T data, String requestMsg, String cacheMsg);

    /**数据实体类型*/
    private Class<T> clz;
    /**回调线程*/
    private Handler handler;
    /**请求*/
    private Request request;

    /**构造（默认回调主线程）*/
    public HttpCacheCallback(Class<T> clz){
        this(Looper.myLooper(), clz);
    }

    /**构造（指定线程回调，当前线程looper不存在时使用主线程回调）*/
    public HttpCacheCallback(Looper looper, Class<T> clz){
        this.clz = clz;
        this.handler = new Handler(looper);
    }

    /**数据解析（异常后回调至onError，成功则回调onResponse）*/
    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        request = response.request();//无网络不会进入此方法
        return HttpManager.config().parseResponse(response, clz);
    }

    /**错误回调（包括请求超时异常、数据解析异常）*/
    @Override
    public void onError(Call call, final Exception e, int id) {
        request = call.request();//无网络时使用call内的request
        HttpCacheManager.getInstance().read(request, clz, new HttpCacheResult<T>() {
            @Override
            public void onResult(final boolean result, final T bean, final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback(result, bean, e.getMessage(), msg);
                    }
                });
            }
        });
    }

    /**响应回调*/
    @Override
    public void onResponse(final T bean, int id) {
        HttpCacheManager.getInstance().write(request, bean, new HttpCacheResult<T>() {
            @Override
            public void onResult(final boolean result, final T bean, final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback(result, bean, "request success", msg);
                    }
                });
            }
        });
    }

}