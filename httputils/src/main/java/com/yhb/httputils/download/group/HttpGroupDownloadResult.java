package com.yhb.httputils.download.group;

import com.yhb.httputils.download.HttpDownloadResult;
import java.util.List;

/**多组多文件下载回调*/
public class HttpGroupDownloadResult {
    /**文件夹路径*/
    private String path;
    /**资源链接*/
    private List<HttpDownloadResult> results;

    /**构造*/
    public HttpGroupDownloadResult(String path, List<HttpDownloadResult> results) {
        this.path = path;
        this.results = results;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public List<HttpDownloadResult> getResults() {
        return results;
    }
    public void setResults(List<HttpDownloadResult> results) {
        this.results = results;
    }

}