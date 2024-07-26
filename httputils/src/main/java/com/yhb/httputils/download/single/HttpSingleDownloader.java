package com.yhb.httputils.download.single;

import com.yhb.httputils.HttpManager;
import com.yhb.httputils.download.HttpDownloader;
import com.yhb.httputils.download.HttpDownloadResult;
import com.yhb.httputils.request.HttpRequest;
import com.zhy.http.okhttp.callback.FileCallBack;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.Response;

/**单个文件下载器*/
public class HttpSingleDownloader extends HttpDownloader {

    /**保存文件*/
    private void save(Response response, File file) throws Exception {
        InputStream in = null;
        FileOutputStream out = null;
        byte[] buf = new byte[2048];
        int len = 0;
        try {
            in = response.body().byteStream();
            out = new FileOutputStream(file);
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
        } finally {
            try {
                response.body().close();
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (Exception e) {
                e.printStackTrace();//防止在下载完成关闭数据流时报错
            }
        }
    }

    /**同步*/
    public HttpDownloadResult sync(String folderPath, String url){
        if(isCancel()){
            return new HttpDownloadResult(null, url, "file download cancle");
        }
        File folderFile = new File(folderPath);//文件夹
        if(!folderFile.exists()){//文件夹不存在则创建
            folderFile.mkdirs();
        }
        String name = HttpManager.config().downloadedName(url);//下载文件名
        String rName = HttpManager.config().downloadingFileName(url);//预下载文件名
        File file = new File(folderFile, name);//下载文件
        if(file.exists()){//已存在
            return new HttpDownloadResult(file, url, "file is exists");
        }
        File rFile = new File(folderFile, rName);//预下载文件
        if(rFile.exists()){//存在则删除
            rFile.delete();
        }
        try{
            Response response = HttpRequest.get().tag(HttpSingleDownloader.this).url(url).build().execute();//请求
            save(response, rFile);//保存
        }catch (Exception e){
            if(rFile.exists()){
                rFile.delete();
            }
            return new HttpDownloadResult(null, url, e.getMessage());
        }
        if(file.exists()) file.delete();//下载文件存在则删除
        rFile.renameTo(file);//预下载重命名为下载文件
        return new HttpDownloadResult(file, url, "file is downloaded");
    }

    /**异步*/
    public void async(String folderPath, final String url, final HttpSingleDownloaderCallback callback){
        if(isCancel()){
            if(callback != null) callback.exeCancle();
            return;
        }
        File folderFile = new File(folderPath);//文件夹
        if(!folderFile.exists()){//文件夹不存在则创建
            folderFile.mkdirs();
        }
        String name = HttpManager.config().downloadedName(url);//下载文件名
        String rName = HttpManager.config().downloadingFileName(url);//预下载文件名
        final File file = new File(folderFile, name);//下载文件
        if(file.exists()){//已存在
            if(callback != null) callback.exeCallback(new HttpDownloadResult(file, url, "file is exists"));
            return;
        }
        final File rFile = new File(folderFile, rName);//预下载文件
        if(rFile.exists()){//存在则删除
            rFile.delete();
        }
        HttpRequest.get().tag(HttpSingleDownloader.this).url(url).build().execute(new FileCallBack(folderPath, rName) {
            @Override
            public void inProgress(float progress, long total, int id) {
                if(callback != null) callback.exeProgress(total, (int)(progress * 100));
            }
            @Override
            public void onError(Call call, Exception e, int id) {
                if(rFile.exists()) rFile.delete();
                if(callback != null) callback.exeCallback(new HttpDownloadResult(null, url, e.getMessage()));
            }
            @Override
            public void onResponse(File response, int id) {
                if(file.exists()) file.delete();
                response.renameTo(file);
                if(callback != null) callback.exeCallback(new HttpDownloadResult(file, url, "file is downloaded"));
            }
        });
    }

}