# 统计分析

## 简介

### 什么是卓易统计分析
卓易统计(DroiAnalytics)是一项面向移动应用开发者的数据统计服务。开发者集成DroiAnalytics SDK之后，应用的使用数据会传输到卓易统计服务器。开发者通过卓易数据分析后台可以查看数据的分析结果。开发者可以通过数据分析结果全方位了解用户行为，调整优化应用，为用户提供更好的产品体验。  

### 卓易统计分析的工作原理
卓易统计SDK，收集用户的信息、使用行为，为云端分析提供数据。主要收集的信息包括：  
*   多种UI样式供开发者选择
*   自动收集信息（设备信息，应用信息等）  
*   页面信息  
*	自定义事件  
*	崩溃错误信息  
卓易统计分析工作流程如下图：  
![](http://baastest.droi.cn/Uploads/DocFile/5767a8b04ba5c.png) 

## 安装
### 快速入门
由于卓易统计SDK基于卓易CoreSDK，所以请在安装卓易统计SDK之前仔细阅读[快速入门](http://baastest.droi.cn/Index/docStart.html)，并确保已经完成快速入门的所有步骤。

### Eclipse 安装SDK
1. 下载[SDK压缩包]()，解压后将 `droianalyticssdk.jar` 包导入到工程的 `libs` 目录下；右键工程根目录，选择`Properties` -> `Java Build Path` -> `Libraries`，然后点击`Add External JARs...` 选择指向jar的路径，点击OK，即导入成功。***（ADT17及以上不需要手动导入）***

2. 需要在 AndroidManifest 中配置权限

    ``` xml
    <uses-permission android:name="android.permission.GET_TASKS" />
    ```

### Android Studio 安装SDK
Android Studio环境下只需要在Project的`build.gradle`中添加如下依赖：
``` groovy
dependencies {
    /*其他依赖 */
    compile 'com.droi.sdk:analytics:+'
}
```

## 使用

### 初始化
在Application的`onCreate()`方法中初始化。
``` java
DroiAnalytics.initialize(this);
```
### 用户使用统计
会话（Session）代表在某一段时间内，用户与应用之间的交互。记录会话可获取新增用户、活跃用户、启动次数、使用时长等基本数据。
一次完整的Session包括如下三种情况：  
1.从启动应用到关闭应用  
2.从启动应用到应用退至后台，且在后台运行时间超过30s  
3.启动应用后设备熄屏，熄屏时间超过30s  

***方式一***  
可以在`Application`的`onCreate()`中调用以下代码：
``` java
DroiAnalytics.enableActivityLifecycleCallbacks(this, true)
```

***方式二***  
在每个Activity中调用`DroiAnalytics.onResume()`和`DroiAnalytics.onPause()`方法对。  
``` java
@Override
protected void onResume() {
    super.onResume();
    DroiAnalytics.onResume(this);
}
@Override
protected void onPause() {
    super.onPause();
    DroiAnalytics.onPause(this);
}
```
也可以新建一个BaseActivity继承自Activity，在BaseActivity的`onResume()`和`onPause()`方法中分别调用`DroiAnalytics.onResume()`和`DroiAnalytics.onPause()`方法。  

### Fragment页面统计
在需要统计的Fragment中调用`onFragmentStart()`和`onFragmentEnd()`。请保证不同页面之间相互独立，没有交叉。  
``` java
String mPageName = "page1";
@Override
public void onResume() {
    super.onResume();
    DroiAnalytics.onFragmentStart(getActivity(), mPageName);
}
@Override
public void onPause() {
    super.onPause();
    DroiAnalytics.onFragmentEnd(getActivity(), mPageName);
}
```
### 自定义事件
1. 计数事件  
在需要统计的事件中调用如下方法：  
    ``` java
    String eventId = "login";
    DroiAnalytics.onEvent(mContext, eventId);
    ```
如果您需要添加附加信息，可以调用如下方法：  
    ``` java
    String eventId = "share";
    Map<String,String> kv=new HashMap<String, String>();
    kv.put("shareto", "QQ");//表示分享到QQ
    DroiAnalytics.onEvent(mContext, eventId, kv);
    ```

2. 计算事件  
在您需要对某个事件进行计算统计时，调用如下方法:  
    ``` java
    Map<String,String> kv=new HashMap<String, String>();
    kv.put("itemName", "apple");
    kv.put("count", 5);
    int du = 10;//表示购买总金额
    String eventId = "buy";
    DroiAnalytics.onCalculateEvent(mContext, eventId, kv, du);
    ```

***注意：***  
事件`id`以及`map`中的`key`和`value`可用英文、数字和下划线“_”，不要使用中文、特殊字符和英文句号“.”。事件`id`长度不能超过128个字节，`key`不能超过128个字节，`value`不能超过256个字节。

### 错误统计
1. 崩溃信息  
SDK通过`Thread.UncaughtExceptionHandler`捕获程序崩溃日志，并在程序下次启动时发送到服务器。该功能默认打开，如需关闭此功能，在Application的`onCreate`中调用如下方法：
	``` java
	DroiAnalytics.setCrashReport(false);
	```
如果您需要在崩溃日志上传时同时上传附加log信息，在Application的`onCreate`中调用如下方法：
	``` java
	DroiAnalytics.setCrashReportWithLog(true);
	```
***注意：***  
只有使用我们提供的Logger类记录的log，才能在崩溃时作为附加log信息上传，所以需要使用该类来替换Android的Log类。
另外在开发时，可以通过以下方法来设置在LogCat中显示通过`Logger`产生的log信息（默认为true），在正式上线时设置为false。
	``` java
	DroiAnalytics.setDebugMode(true);
	```

2. 自定义错误信息  
您也可以发送自定义错误信息。
	``` java
	//发送一个错误String
	String errorInfo = "this is an error";
	DroiAnalytics.onError(mContext, errorInfo);
	//发送一个Exception
	Exception e = new Exception("exception");
	DroiAnalytics.onError(mContext, e);
	```
***注意：***  
`errorInfo`可用英文、数字和下划线“_”，不要使用中文、特殊字符和英文句号“.”。`errorInfo`长度不能超过512个字节。

### 发送策略
实时发送：每当有一条新的记录，就会激发一次发送（不考虑网络环境），实际发送的间隔是3分钟。  
非实时发送：开发者可以指定从半小时到1天之间的任意时间间隔上报数据记录，也可指定只在WIFI环境下才发送。  
默认的发送策略是实时发送，也可以设置为非实时发送。在非实时策略略下，可以设置是否只在wifi下发送以及发送间隔***（以分钟为单位）***。
``` java
//设置为非实时
DroiAnalytics.setDefaultReportPolicy(SendPolicy.SCHEDULE);
//设置非实时策略下 是否只在wifi下发送及发送间隔
DroiAnalytics.setScheduleReportPolicy(false, 10);
```

### 测试模式
如果开发者需要在logcat中看到SDK产生的log，需要在SD卡目录下的`droi`文件夹中（如果没有请创建）放入`dev_data.json`文件，文件中写入字符串 `{"debug_mode":true}`。

