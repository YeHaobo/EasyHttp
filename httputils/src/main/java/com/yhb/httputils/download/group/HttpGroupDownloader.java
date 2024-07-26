package com.yhb.httputils.download.group;

import com.yhb.httputils.download.HttpDownloadResult;
import com.yhb.httputils.download.multiple.HttpMultipleDownloader;
import com.yhb.httputils.download.multiple.HttpMultipleDownloaderCallback;
import java.util.ArrayList;
import java.util.List;

/**多组多文件下载器*/
public class HttpGroupDownloader extends HttpMultipleDownloader {

    /**同步*/
    public List<HttpGroupDownloadResult> sync(List<HttpGroupDownloadRes> groupResList){
        return sync(groupResList, 0, new ArrayList<HttpGroupDownloadResult>());
    }
    /**同步*/
    private List<HttpGroupDownloadResult> sync(List<HttpGroupDownloadRes> groupResList, int pathIndex, List<HttpGroupDownloadResult> groupResultList){
        if(groupResList.size() <= pathIndex){
            return groupResultList;
        }
        HttpGroupDownloadRes groupRes = groupResList.get(pathIndex);
        List<HttpDownloadResult> resultList = sync(groupRes.getPath(), groupRes.getUrls());
        groupResultList.add(new HttpGroupDownloadResult(groupRes.getPath(), resultList));
        return sync(groupResList, pathIndex + 1, groupResultList);
    }

    /**异步*/
    public void async(List<HttpGroupDownloadRes> resList, HttpGroupDownloaderCallback callback){
        async(resList, 0, new ArrayList<HttpGroupDownloadResult>(), callback);
    }
    /**异步*/
    private void async(final List<HttpGroupDownloadRes> groupResList, final int pathIndex, final List<HttpGroupDownloadResult> groupResultList, final HttpGroupDownloaderCallback callback){
        if(groupResList.size() <= pathIndex){
            callback.onCallback(groupResultList);
            return;
        }
        async(groupResList.get(pathIndex).getPath(), groupResList.get(pathIndex).getUrls(), new HttpMultipleDownloaderCallback(callback.looper()) {
            @Override
            public void onCancle() {
                callback.onCancle();
            }
            @Override
            public void inProgress(List<String> url, int index, long total, int progress) {
                callback.inProgress(groupResList, pathIndex, index, total, progress);
            }
            @Override
            public void onCallback(List<HttpDownloadResult> resultList) {
                groupResultList.add(new HttpGroupDownloadResult(groupResList.get(pathIndex).getPath(), resultList));
                async(groupResList, pathIndex + 1, groupResultList, callback);
            }
        });
    }
    
}