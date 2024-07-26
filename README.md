# EasyHttp
Http请求、文件下载、OSS上传工具

基于okhttp3（com.squareup.okhttp3）、okhttputils（com.zhy）、oss-android-sdk（com.aliyun.dpa）重新封装的http请求与文件互传工具  
1、支持请求数据自动缓存以及自动转化实体bean  
2、支持单文件、多文件、多组多文件下载  
3、支持单文件、多文件上传  
4、请求、下载、上传的回调支持线程切换  

***

### 依赖
（1）在Project的build.gradle文件中添加
```java
  allprojects {
    repositories {
      ... ...
      maven { url 'https://jitpack.io' }
      ... ...
    }
  }
```
（2）在app的build.gradle文件中添加
```java
  dependencies {
    ... ...
    implementation 'com.github.YeHaobo.EasyHttp:httputils:1.1'//httputils
    implementation 'com.squareup.okhttp3:okhttp:3.10.0'//OkHttp3
    implementation 'com.zhy:okhttputils:2.6.2'//OkHttp3Utils

    implementation 'com.github.YeHaobo.EasyHttp:ossutils:1.1'//ossutils
    implementation 'com.aliyun.dpa:oss-android-sdk:2.9.13'//阿里云OSS

    implementation 'com.alibaba:fastjson:1.2.56'//fastjson
    ... ...
  }
```

### 权限
（1）Android6.0+注意权限的动态获取
```java
<uses-permission android:name="android.permission.INTERNET"/>6.0+必须
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>6.0+必须
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>6.0+必须
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"/>11.0+版本按需添加授权
```

### 初始化
#### 1、httputils
（1）新建配置文件继承自HttpDefaultConfig，并按需重写方法
```java
public class MyDefaultConfig extends HttpDefaultConfig {

    /**TAG*/
    private static final String TAG = "MyDefaultConfig";

    @Override
    public Map<String, String> headers() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("device-type", "0");
        hashMap.put("app-version", BuildConfig.VERSION_NAME);
        hashMap.put("app-package", BuildConfig.APPLICATION_ID);
        hashMap.put("mac", mac());
        return hashMap;
    }

}
```
（2）在当前项目的 application 中初始化
```java
        HttpManager.initialize(new MyDefaultConfig());
```
#### 2、ossutils.
（1）新建配置文件继承并实现OssBaseConfig
```java
public class MyDefaultOssConfig extends OssBaseConfig {

    @Override
    public int socketTimeout() {return 15 * 1000;}

    @Override
    public int connectionTimeout() {return 15 * 1000; }

    @Override
    public int maxErrorRetry() { return 2; }

    @Override
    public int maxConcurrentRequest() {return 5;}

    @Override
    public String authServerUrl() {return "";}//鉴权地址

    @Override
    public String point() {return "https://ayqhl-cloud.oss-cn-hangzhou.aliyuncs.com";}

    @Override
    public String endPoint() {return "https://oss-cn-hangzhou.aliyuncs.com";}

    @Override
    public String bucketName() {return "ayqhl-cloud";}

    @Override
    public String objectKey(File file) {
        String postfix = file.getName().substring(file.getName().lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        return "prod/device/" + mac() + "/" + format.format(new Date(System.currentTimeMillis())) + "/" + uuid + postfix;
    }

    @Override
    public String cachePath() {return Environment.getExternalStorageDirectory().getPath() + "/test/upload";}

}
```
（2）在当前项目的 application 中初始化
```java
        OssManager.initialize(getApplicationContext(), new MyDefaultOssConfig());
```

### 请求
#### 同步请求
（1）不缓存
```java
        try {
            Response response = HttpEasyRequest.get().tag(TAG).url("...").params(map).build().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
```

#### 异步请求
（1）不缓存
```java
        HttpEasyRequest
                .get()
                .tag(TAG)
                .url("...")
                .params(map)
                .build()
                .execute(new HttpDefaultCallback<String>(Looper.getMainLooper(), String.class) {
                    @Override
                    public void callback(boolean result, String data, String msg) {
                        Log.e(TAG, Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
                        Log.e(TAG, result + "\n" + data + "\n" + msg);
                    }
                });
```
（2）缓存
```java
        HttpEasyRequest
                .get()
                .tag(TAG)
                .url("...")
                .params(map)
                .build()
                .execute(new HttpCacheCallback<String>(Looper.getMainLooper(), String.class) {
                    @Override
                    public void callback(boolean result, String data, String requestMsg, String cacheMsg) {
                        Log.e(TAG, Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
                        Log.e(TAG, result + "\n" + data + "\n" + requestMsg + "\n" + cacheMsg);
                    }
                });
```
_HttpDefaultCallback、HttpCacheCallback构造可传入Looper来指定回调线程，默认使用当前线程回调_  

#### 取消请求
```java
      HttpRequest.cancel(TAG);
```
### 下载
#### 单文件下载
（1）同步  
```java
      HttpDownloadResult result = HttpDownloader.createSingle().sync(folderPath, url);
```
（2）异步
```java
      HttpDownloader.createSingle().async(folderPath, url, new HttpSingleDownloaderCallback(Looper.getMainLooper()) {
          boolean is = false;
          @Override
          public void inProgress(long total, int progress) {
              if(!is){
                  Log.e(TAG, "pro " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
                  is = true;
              }
          }
          @Override
          public void onCallback(HttpDownloadResult result) {
              Log.e(TAG, "back " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
              Log.e(TAG, result.toString());
          }
      });
```
#### 多文件下载
（1）同步
```java
     List<HttpDownloadResult> resultList = HttpDownloader.createMultiple().sync(folderPath, urlList);
```
（2）异步
```java
      HttpDownloader.createMultiple().async(folderPath, urlList, new HttpMultipleDownloaderCallback(Looper.myLooper()) {
          int in = -1;
          @Override
          public void inProgress(List<String> url, int index, long total, int progress) {
              if(in != index){
                  Log.e(TAG, "pro " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
                  in = index;
              }
          }
          @Override
          public void onCallback(List<HttpDownloadResult> resultList) {
              Log.e(TAG, "back " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
              for(HttpDownloadResult result : resultList){
                  Log.e(TAG, result.toString());
              }
          }
      });
```
#### 多组多文件下载
（1）同步
```java
      List<HttpGroupDownloadResult> resultList = HttpDownloader.createGroup().sync(resList);
```
（2）异步
```java
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
```
_HttpSingleDownloaderCallback、HttpMultipleDownloaderCallback、HttpGroupDownloaderCallback构造可传入Looper来指定回调线程，默认使用当前线程回调_  
#### 取消下载
```java
  httpSingleDownloader.cancel();
  httpMultipleDownloader.cancel();
  httpGroupDownloader.cancel();
```
### 上传
#### 单文件上传
（1）同步
```java
      OssUploaderResult result = OssUploader.createSingle().sync(fileList.get(0));
```
（2）异步
```java
      OssUploader.createSingle().async(fileList.get(0), Looper.myLooper(), new OssSingleUploaderCallback(Looper.getMainLooper()) {
          boolean in = false;
          @Override
          public void inProgress(long total, int progress) {
              if(!in){
                   Log.e(TAG, "pro " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
                   in = true;
              }
          }
          @Override
          public void onCallback(OssUploaderResult result) {
              Log.e(TAG, "back " + Thread.currentThread().getName() + "  " + Thread.currentThread().getId());
              Log.e(TAG, result.toString());
          }
      });
```
#### 多文件上传  
（1）同步
```java
      List<OssUploaderResult> resultList = OssUploader.createMultiple().sync(fileList);
```
（2）异步
```java
      OssUploader.createMultiple().async(fileList, Looper.myLooper(), new OssMultipleUploaderCallback(Looper.getMainLooper()) {
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
```
_OssSingleUploaderCallback、OssMultipleUploaderCallback构造可传入Looper来指定回调线程，默认使用当前线程回调_  
_上传文件前需要拷贝一次文件，防止文件在上传时发生变更，故异步时需要拷贝文件的线程looper_  
#### 取消上传
```java
  ossSingleUploader.cancel();
  ossMultipleUploader.cancel();
```

### 问题及其他
_暂无_



