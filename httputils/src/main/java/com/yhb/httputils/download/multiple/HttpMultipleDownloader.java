package com.yhb.httputils.download.multiple;

import com.yhb.httputils.download.HttpDownloadResult;
import com.yhb.httputils.download.single.HttpSingleDownloader;
import com.yhb.httputils.download.single.HttpSingleDownloaderCallback;
import java.util.ArrayList;
import java.util.List;

/**多个文件下载器*/
public class HttpMultipleDownloader extends HttpSingleDownloader {

    /**同步*/
    public List<HttpDownloadResult> sync(String folderPath, List<String> urls){
        return sync(folderPath, urls, 0, new ArrayList<HttpDownloadResult>());
    }
    /**同步*/
    private List<HttpDownloadResult> sync(String folderPath, List<String> urls, int index, List<HttpDownloadResult> resultList){
        if(urls.size() <= index){
            return resultList;
        }
        HttpDownloadResult result = sync(folderPath, urls.get(index));
        resultList.add(result);
        return sync(folderPath, urls, index + 1, resultList);
    }

    /**异步*/
    public void async(String folderPath, List<String> urls, HttpMultipleDownloaderCallback callback){
        async(folderPath, urls, 0, new ArrayList<HttpDownloadResult>(), callback);
    }
    /**异步*/
    private void async(final String folderPath, final List<String> urls, final int index, final List<HttpDownloadResult> resultList, final HttpMultipleDownloaderCallback callback){
        if(urls.size() <= index){
            callback.onCallback(resultList);
            return;
        }
        async(folderPath, urls.get(index), new HttpSingleDownloaderCallback(callback.looper()) {
            @Override
            public void onCancle() {
                callback.onCancle();
            }
            @Override
            public void inProgress(long total, int progress) {
                callback.inProgress(urls, index, total, progress);
            }
            @Override
            public void onCallback(HttpDownloadResult result) {
                resultList.add(result);
                async(folderPath, urls, index + 1, resultList, callback);
            }
        });
    }

}