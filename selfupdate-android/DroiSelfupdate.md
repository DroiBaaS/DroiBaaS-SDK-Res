# 版本更新  

## 简介
版本更新帮助开发者将应用升级到最新版本，可以让用户体验到应用的最新功能。

### 工作原理
版本更新SDK上传用户终端上的应用版本信息（versionName和versionCode）与版本更新服务器上开发者上传应用的版本信息进行比较。如果服务器的版本高，就会在客户端提示更新。  
![](http://i.imgur.com/zR4kMjR.jpg)

### 优点
*   集成方便快捷
*   支持不同渠道更新
*   通过CDN加速，更新下载速度快
*   多种UI样式供开发者选择
*   支持增量更新，节省流量

## 安装

### 快速入门
由于卓易版本更新SDK基于卓易CoreSDK，所以请在安装卓易版本更新SDK之前仔细阅读[快速入门](http://baastest.droi.cn/Index/docStart.html) 。

### Eclipse 安装SDK
1. 下载[SDK压缩包]()，解压后将 `droiselfupdatesdk.jar`和`Utility.jar` 包导入到工程的 `libs` 目录下；右键工程根目录，选择`Properties` -> `Java Build Path` -> `Libraries`，然后点击`Add External JARs...` 选择指向jar的路径，点击OK，即导入成功。`（ADT17及以上不需要手动导入）`  
将包含.so文件的文件夹直接复制到工程目录下`（如果不需要增量更新功能，可以不添加，以减小包的大小）`  ，将`res`文件夹直接复制到工程目录下，和工程本身`res`目录合并。请不要随意删除其中的文件**（`res`文件都以droi开头）**。
2. 在 AndroidManifest 中配置组件

    ``` xml
    <manifest...>
    <application...>
        <!--注册自更新对话框-->
        <activity
            android:name="com.droi.sdk.selfupdate.DroiUpdateDialogActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:theme="@android:style/Theme.Translucent.NoTitleBar">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
            </intent-filter>
        </activity>
        <receiver android:name="com.droi.sdk.selfupdate.NotificationClickReceiver" />
    </application>
    </manifest>
    ```

### Android Studio 安装SDK
Android Studio环境下只需要在Project的`build.gradle`中添加如下依赖：
``` groovy
dependencies {
	/*其他依赖 */
	compile 'com.droi.sdk:selfupdate:+'
	compile 'com.android.support:support-v4:23.3.0'
}
```

## 使用

### 初始化
在Application的`onCreate()`方法中调用：
``` java
DroiUpdate.initialize(this);
```

### 更新类型  
1. 自动检测更新  
在进入应用后的第一个activity的`onCreate`中调用`DroiUpdate.update()`。
示例：  
    ``` java
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	DroiUpdate.update(this);
    }
    ```

2. 手动更新  
你可以设置点击一个按钮时，触发手动更新。  
示例:
    ``` java
    public void onClick(View v) {
    	DroiUpdate.manualUpdate(mContext);
    }
    ```

### 自更新自定义功能
1. 恢复默认设置

    ``` java
    DroiUpdate.setDefault();
    ```
**注意：**
该方法会调用一下代码设为这些配置的默认值
``` java
setUpdateOnlyWifi(true);
setUpdateAutoPopup(true);
setUpdateUIStyle(UpdateUIStyle.STYLE_BOTH);
setUpdateListener(null);
```

2. 设置是否只在wifi下更新

    ``` java
    DroiUpdate.setUpdateOnlyWifi(false);
    ```
默认为true，设置为false时在所有网络下都更新。

3. 设置是否自动弹出更新提示

    ``` java
    DroiUpdate.setUpdateAutoPopup(false);
    ```
默认为true，设置为false时需要自己处理回调  

4. 设置通知样式  
    
    ```
    DroiUpdate.setUpdateUIStyle(UpdateUIStyle.STYLE_DIALOG);  
    ```
默认为`UpdateUIStyle.STYLE_BOTH`（对话框和通知栏都显示），也可以设置为`UpdateUIStyle.STYLE_DIALOG`（对话框）或者`UpdateUIStyle.STYLE_NOTIFICATION`（通知栏）。由于很多定制化OS都提供了禁止弹出通知栏的选项，所以我们建议设置为`UpdateUIStyle.STYLE_BOTH`或者`UpdateUIStyle.STYLE_DIALOG`。

### 设置回调监听事件
监听更新结果,该设置只对自动更新下的`NORMAL`（正常更新）和手动更新有效。  
在设置监听之前，请务必调用DroiUpdate.setUpdateAutoPopup(false)关闭默认UI显示。  
示例：
``` java
DroiUpdate.setUpdateAutoPopup(false);
DroiUpdate.setUpdateListener(new DroiUpdateListener() {
    @Override
    public void onUpdateReturned(int updateStatus,DroiUpdateResponse response) {
    		switch (updateStatus) {
       	case UpdateStatus.NO:
      		Toast.makeText(mContext,"没有更新",Toast.LENGTH_SHORT).show();
    		break;
      	case UpdateStatus.YES:
      	    //你可以在此回调处实现自己的更新UI
      		Toast.makeText(mContext,"发现更新",Toast.LENGTH_SHORT).show();
    		break;
       	case UpdateStatus.ERROR:
      		Toast.makeText(mContext,"发生错误",Toast.LENGTH_SHORT).show();
    		break;
     	case UpdateStatus.TIMEOUT:
      		Toast.makeText(mContext,"超时",Toast.LENGTH_SHORT).show();
    		break;
     	case UpdateStatus.NON_WIFI:
      		Toast.makeText(mContext,"没有wifi",Toast.LENGTH_SHORT).show();
    		break;
       	case UpdateStatus.UPDATING:
       		Toast.makeText(mContext,"正在更新中",Toast.LENGTH_SHORT).show();
      		break;
     	}
    }
});
```

`DroiUpdateListener`的`onUpdateReturned(int updateStatus,DroiUpdateResponse response)`回调会返回检测更新结果。  
`updateStatus`会有六种可能的值：  

| 方法名称 | 说明 |
|---------|:---------|
|`UpdateStatus.NO`			|没有更新|
|`UpdateStatus.YES`			|有更新|
|`UpdateStatus.ERROR`		|发生错误| 
|`UpdateStatus.TIMEOUT`		|网络超时|
|`UpdateStatus.NON_WIFI`	|不是wifi网络环境（只在设置了wifiOnly的情况下提示）|
|`UpdateStatus.UPDATING`	|正在更新中|

`response`包含了本次请求更新返回的数据,以下是`DroiUpdateResponse`的实例方法：

| 方法名称 | 说明 |
|---------|:---------|
|`getErrorCode()`	|获取错误code，没有错误返回0，在使用其他方法前使用|
|`getErrorCode()` 	|`getErrorCode() == 0`判断是否发送成功|
|`getTitle()`			|获取更新标题（可以为空）|
|`getContent()`		|获取更新内容|
|`getUpdateType()`	|获取更新类型，在`UpdateType`中定义，包含四种值：  `NONEW`（没有更新）、`NORMAL`（正常更新）、`FROECE`（强制更新）、`SILENT`（静默更新）、`MARKET`（跳转卓易市场更新）。|
|`getVersionName()`	|获取新安装包的`VersionName`|  
|`getVersionCode()`	|获取新安装包的`VersionCode`| 
|`getNewMd5()`		|获取新安装包的md5值（可以用于校验下载的包是否正确| 
|`getNewSize()`		|获取新安装包的大小|
|`getUrl()`			|获取安装包的下载链接（如果是增量更新，则为空）|
|`isDeltaUpdate()` 	|获取是否是增量更新|
|`getPatchUrl()`    |获取增量包的下载链接| 
|`getPatchMd5()`		|获取增量包的md5值（可以用于校验下载的包是否正确）|
|`getPatchSize()`	|获取增量包的大小|

当获取到`DroiUpdateResponse`的实例之后，可以使用我们提供的以下方法：  
(1)显示对话框
``` java
DroiUpdate.showUpdateDialog(Context context,DroiUpdateResponse response);
```
(2)显示通知栏
``` java
DroiUpdate.showUpdateNotification(Context context,DroiUpdateResponse response);
```
(3)检查更新是否被忽略
``` java
DroiUpdate.isUpdateIgnore(Context context,DroiUpdateResponse response);
```
(4)设置该更新忽略
``` java
DroiUpdate.setUpdateIgnore(Context context,DroiUpdateResponse response);
```
(5)获取已经下载的文件，如果没有下载则返回null
``` java
DroiUpdate.getDownloadedFile(Context context,DroiUpdateResponse response);
```
(6)安装应用
``` java
DroiUpdate.installApp(Context context, File file, int updateType);
```
其中`file`可以通过`DroiUpdate.getDownloadedFile(Context context,DroiUpdateResponse response)`获得。   
(7)下载并安装应用
``` java
DroiUpdate.downloadApp(Context context,DroiUpdateResponse response);
```

### 特殊更新类型-应用内更新
你可能有更新`jar`包，`so`文件或者`配置文件`的需求，可以调用下面的接口来实现。  
示例：
``` java
String fileVersion = "1.0";
String filePath = Environment.getExternalStorageDirectory()
         .getAbsolutePath() + File.separator + "my.cnf";
DroiUpdate.inappUpdate(mContext, fileVersion,
    filePath, new DroiInappUpdateListener() {
    @Override
    public void onStart(long l) {
    }
    @Override
    public void onProgress(float v) {
    }
    @Override
    public void onFinished(String s) {
        Toast.makeText(mContext, "有更新，下载成功", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onFailed(int failCode) {
        switch (failCode){
            case InappUpdateFailCode.NO_UPDATE:
                Toast.makeText(mContext, "没有更新", Toast.LENGTH_SHORT).show();
                break;
            case InappUpdateFailCode.DOWNLOAD_FAILED:
                Toast.makeText(mContext, "下载时发生错误", Toast.LENGTH_SHORT).show();
                break;
            case InappUpdateFailCode.REQUEST_FAILED:
                Toast.makeText(mContext, "请求更新时发生错误", Toast.LENGTH_SHORT).show();
                break;
        }
    }
});
```

`DroiInappUpdateListener`有四个回调方法：

| 方法名称 | 说明 |
|:---------|---------| 
|`onStart()`		|有更新开始下载|
|`onProgress()`	|下载进度|
|`onFinished()`	|下载成功|
|`onFailed()`		|没有更新、下载失败或请求失败|

`response`包含了本次请求更新返回的数据，以下是`DroiInappUpdateResponse`的实例方法： 

| 方法名称 | 说明 |
|---------|:---------| 
|`getTitle()`			|获取更新标题（可以为空）|
|`getContent()` 		|获取更新内容|
|`getFileVersion()`	|获取文件|
|`getFileMd5()`		|获取文件md5值（可以用于校验下载的包是否正确）|
|`getFileSize()`		|获取文件大小|
|`getFileUrl()`		|获取文件下载链接| 