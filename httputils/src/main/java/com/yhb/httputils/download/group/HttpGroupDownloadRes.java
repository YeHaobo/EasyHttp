package com.yhb.httputils.download.group;

import java.util.List;

/**多组多文件下载资源*/
public class HttpGroupDownloadRes {
    /**文件夹路径*/
    private String path;
    /**资源链接*/
    private List<String> urls;

    /**构造*/
    public HttpGroupDownloadRes(String path, List<String> urls) {
        this.path = path;
        this.urls = urls;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getUrls() {
        return urls;
    }
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

}