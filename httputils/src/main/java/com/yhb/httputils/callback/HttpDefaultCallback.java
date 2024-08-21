package com.yhb.httputils.callback;

import android.os.Handler;
import android.os.Looper;
import com.yhb.httputils.HttpManager;
import com.zhy.http.okhttp.callback.Callback;
import okhttp3.Call;
import okhttp3.Response;

/**默认回调*/
public abstract class HttpDefaultCallback<T> extends Callback<T> {

    /**请求回调*/
    public abstract void callback(boolean result, T data, String msg);

    /**数据实体类型*/
    private Class<T> clz;
    /**回调线程*/
    private Handler handler;

    /**构造*/
    public HttpDefaultCallback(Class<T> clz){
        this(null, clz);
    }

    /**构造（指定线程回调，当前线程looper不存在时使用主线程回调）*/
    public HttpDefaultCallback(Looper looper, Class<T> clz){
        this.clz = clz;
        this.handler = new Handler(looper != null ? looper : Looper.getMainLooper());
    }

    /**数据解析（异常后回调至onError，成功则回调onResponse）*/
    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        return HttpManager.config().parseResponse(response, clz);
    }

    /**错误回调（包括请求超时异常、数据解析异常）*/
    @Override
    public void onError(Call call, final Exception e, int id) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback(false, null, e.getMessage());
            }
        });
    }

    /**响应回调*/
    @Override
    public void onResponse(final T bean, int id) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback(true, bean, "request success");
            }
        });
    }

}