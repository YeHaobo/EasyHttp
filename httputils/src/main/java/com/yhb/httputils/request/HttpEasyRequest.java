package com.yhb.httputils.request;

import com.yhb.httputils.HttpManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.OtherRequestBuilder;
import com.zhy.http.okhttp.builder.PostFileBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;

/**http请求*/
public class HttpEasyRequest {

    /**get*/
    public static GetBuilder get(){
        return OkHttpUtils.get().headers(HttpManager.config().headers());
    }

    /**post*/
    public static PostFormBuilder post(){
        return OkHttpUtils.post().headers(HttpManager.config().headers());
    }

    /**postString*/
    public static PostStringBuilder postString(){
        return OkHttpUtils.postString().headers(HttpManager.config().headers());
    }

    /**postFile*/
    public static PostFileBuilder postFile(){
        return OkHttpUtils.postFile().headers(HttpManager.config().headers());
    }

    /**put*/
    public static OtherRequestBuilder put(){
        return OkHttpUtils.put().headers(HttpManager.config().headers());
    }

    /**delete*/
    public static OtherRequestBuilder delete(){
        return OkHttpUtils.delete().headers(HttpManager.config().headers());
    }

    /**patch*/
    public static OtherRequestBuilder patch(){
        return OkHttpUtils.patch().headers(HttpManager.config().headers());
    }

    /**head*/
    public static GetBuilder head(){
        return OkHttpUtils.head().headers(HttpManager.config().headers());
    }

    /**取消请求*/
    public static void cancel(Object tag){
        OkHttpUtils.getInstance().cancelTag(tag);
    }

}