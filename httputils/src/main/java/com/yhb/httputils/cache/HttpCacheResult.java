package com.yhb.httputils.cache;

/**缓存操作回调*/
public interface HttpCacheResult<T> {
    /**
     * 缓存操作回调
     * @param result 成功/失败
     * @param data 成功返回数据、失败返回null
     * @param msg 执行信息
     */
    void onResult(boolean result, T data, String msg);
}