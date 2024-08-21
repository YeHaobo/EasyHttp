package com.yhb.test;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.yhb.httputils.HttpManager;
import com.yhb.httputils.callback.HttpDefaultCallback;
import com.yhb.httputils.download.HttpDownloadResult;
import com.yhb.httputils.download.HttpDownloader;
import com.yhb.httputils.download.group.HttpGroupDownloadRes;
import com.yhb.httputils.download.group.HttpGroupDownloadResult;
import com.yhb.httputils.download.group.HttpGroupDownloaderCallback;
import com.yhb.httputils.request.HttpEasyRequest;
import com.yhb.ossutils.OssManager;
import com.yhb.ossutils.upload.OssUploader;
import com.yhb.ossutils.upload.OssUploaderResult;
import com.yhb.ossutils.upload.multiple.OssMultipleUploaderCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**TAG*/
    private static final String TAG = "MainActivity";

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HandlerThread handlerThread = new HandlerThread("yhb");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        HttpManager.initialize(new MyBaseConfig());
        OssManager.initialize(getApplicationContext(), new OssDefaultConfig());
        ossTest();
    }

    private void requestTest(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, Thread.currentThread().getName() + "  " + Thread.currentThread().getId());

//                try {
//                    Response response = HttpEasyRequest
//                            .get()
//                            .tag(TAG)
//                            .url("https://pm.aiyi.tv:9010/ayqhl-operation/themeAuditDeviceRel/getThemeByDevice")
//                            .params(map)
//                            .build()
//                            .execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                HttpEasyRequest
                        .get()
                        .tag(TAG)
                        .url("https://pm.aiyi.tv:9010/ayqhl-operation/themeAuditDeviceRel/getThemeByDevice")
                        .build()
                        .execute(new HttpDefaultCallback<String>(Looper.getMainLooper(), String.class) {
                            @Override
                            public void callback(boolean result, String data, String msg) {
                                Log.e(TAG, Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
                                Log.e(TAG, result + "\n" + data + "\n" + msg);
                            }
                        });

//                HttpEasyRequest
//                        .get()
//                        .tag(TAG)
//                        .url("https://pm.aiyi.tv:9010/ayqhl-operation/themeAuditDeviceRel/getThemeByDevice")
//                        .params(map)
//                        .build()
//                        .execute(new HttpCacheCallback<String>(Looper.getMainLooper(), String.class) {
//                            @Override
//                            public void callback(boolean result, String data, String requestMsg, String cacheMsg) {
//                                Log.e(TAG, Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
//                                Log.e(TAG, result + "\n" + data + "\n" + requestMsg + "\n" + cacheMsg);
//                            }
//                        });

            }
        });
    }

    private void downloadTest(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<String> urlList = new ArrayList<>();
                urlList.add("https://ayqhl-cloud.oss-cn-hangzhou.aliyuncs.com/prod/98/img/202405/5b18e170-d8e1-4190-9de2-96359c23b256.jpg");
                urlList.add("https://ayqhl-cloud.oss-cn-hangzhou.aliyuncs.com/prod/98/img/202407/f344dafd-45f5-4197-a3d6-153b3a3e4f13.jpg");
                urlList.add("https://ayqhl-cloud.oss-cn-hangzhou.aliyuncs.com/prod/98/img/202306/917e3bc9-7a54-4d1b-b77e-d8a6ca69a6ff.png");
                String folderPath = Environment.getExternalStorageDirectory().getPath() + "/test";

                Log.e(TAG, Thread.currentThread().getName() + "  " + Thread.currentThread().getId());

//                HttpDownloadResult result = HttpDownloader.createSingle().sync(folderPath, urlList.get(0));
//                Log.e(TAG, Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
//                Log.e(TAG, result.toString());

//                HttpDownloader.createSingle().async(folderPath, urlList.get(1), new HttpSingleDownloaderCallback(Looper.getMainLooper()) {
//                    boolean is = false;
//                    @Override
//                    public void inProgress(long total, int progress) {
//                        if(!is){
//                            Log.e(TAG, "pro " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
//                            is = true;
//                        }
//                    }
//                    @Override
//                    public void onCallback(HttpDownloadResult result) {
//                        Log.e(TAG, "back " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
//                        Log.e(TAG, result.toString());
//                    }
//                });

//                List<HttpDownloadResult> resultList = HttpDownloader.createMultiple().sync(folderPath, urlList);
//                Log.e(TAG, Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
//                for(HttpDownloadResult result : resultList){
//                    Log.e(TAG, result.toString());
//                }

//                HttpDownloader.createMultiple().async(folderPath, urlList, new HttpMultipleDownloaderCallback(Looper.myLooper()) {
//                    int in = -1;
//                    @Override
//                    public void inProgress(List<String> url, int index, long total, int progress) {
//                        if(in != index){
//                            Log.e(TAG, "pro " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
//                            in = index;
//                        }
//                    }
//                    @Override
//                    public void onCallback(List<HttpDownloadResult> resultList) {
//                        Log.e(TAG, "back " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
//                        for(HttpDownloadResult result : resultList){
//                            Log.e(TAG, result.toString());
//                        }
//                    }
//                });

                List<HttpGroupDownloadRes> resList = new ArrayList<>();
                resList.add(new HttpGroupDownloadRes(folderPath + "/11", urlList));
                resList.add(new HttpGroupDownloadRes(folderPath + "/22", urlList));
                resList.add(new HttpGroupDownloadRes(folderPath + "/33", urlList));
//                List<HttpGroupDownloadResult> resultList = HttpDownloader.createGroup().sync(resList);
//                Log.e(TAG, Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
//                for(HttpGroupDownloadResult result : resultList){
//                    for(HttpDownloadResult result1 : result.getResults()){
//                        Log.e(TAG, result1.toString());
//                    }
//                }

                HttpDownloader.createGroup().async(resList, new HttpGroupDownloaderCallback(Looper.getMainLooper()) {
                    int in1 = -1;
                    int in2 = -1;
                    @Override
                    public void inProgress(List<HttpGroupDownloadRes> groupResList, int pathIndex, int urlIndex, long total, int progress) {
                        if(pathIndex != in1 || urlIndex != in2){
                            Log.e(TAG, "pro " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
                            in1 = pathIndex;
                            in2 = urlIndex;
                        }
                    }
                    @Override
                    public void onCallback(List<HttpGroupDownloadResult> groupResultList) {
                        Log.e(TAG, "back " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
                        for(HttpGroupDownloadResult result : groupResultList){
                            for(HttpDownloadResult result1 : result.getResults()){
                                Log.e(TAG, result1.toString());
                            }
                        }
                    }
                });

            }
        });
    }

    private void ossTest(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<File> fileList = new ArrayList<>();
                fileList.add(new File(Environment.getExternalStorageDirectory().getPath() + "/test/11/111.png"));
                fileList.add(new File(Environment.getExternalStorageDirectory().getPath() + "/test/11/222.jpg"));
                fileList.add(new File(Environment.getExternalStorageDirectory().getPath() + "/test/11/333.jpg"));

                Log.e(TAG, Thread.currentThread().getName() + "  " + Thread.currentThread().getId());

//                OssUploaderResult result = OssUploader.createSingle().sync(fileList.get(0));
//                Log.e(TAG, Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
//                Log.e(TAG, result.toString());

//                OssUploader.createSingle().async(fileList.get(0), Looper.myLooper(), new OssSingleUploaderCallback(Looper.getMainLooper()) {
//                    boolean in = false;
//                    @Override
//                    public void inProgress(long total, int progress) {
//                        if(!in){
//                             Log.e(TAG, "pro " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
//                             in = true;
//                        }
//                    }
//                    @Override
//                    public void onCallback(OssUploaderResult result) {
//                        Log.e(TAG, "back " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
//                        Log.e(TAG, result.toString());
//                    }
//                });

//                List<OssUploaderResult> resultList = OssUploader.createMultiple().sync(fileList);
//                Log.e(TAG, Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
//                for(OssUploaderResult result : resultList){
//                    Log.e(TAG, result.toString());
//                }

                OssUploader.createMultiple().async(new OssDefaultUploadConfig("12:23:45:56:78"), fileList, new OssMultipleUploaderCallback(Looper.getMainLooper()) {
                    int in = -1;
                    @Override
                    public void inProgress(List<File> fileList, int index, long total, int progress) {
                        if(in != index){
                            Log.e(TAG, "pro " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
                            in = index;
                        }
                    }
                    @Override
                    public void onCallback(List<OssUploaderResult> resultList) {
                        Log.e(TAG, "back " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
                        for(OssUploaderResult result : resultList){
                            Log.e(TAG, result.toString());
                        }
                    }
                });

            }
        });
    }

}