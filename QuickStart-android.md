# 快速入门-Android

## DroiBaaS快速入门流程示意图 

![](http://baastest.droi.cn/Uploads/DocFile/57870a0bc708d.jpg)

## 第一步：注册成为DroiBaaS开发者

打开[DroiBaaS官网](http://baastest.droi.cn/)，点击右上角的注册按钮，填入邮箱或者手机号注册成为DroiBaaS的开发者。同时也支持新浪微博和QQ的快捷登录方式。注册成为开发者并且登录后，请完善开发者详细信息，以便更好的沟通以及问题的解决。

![](http://baastest.droi.cn/Uploads/DocFile/57870a6c795f3.png)

**友情提醒：**如果是企业开发者，建议在注册账号时使用企业邮箱，尽量避免使用个人邮箱，防止由于个人离职带来的问题。

## 第二步：创建应用，获取应用信息

登录进入管理后台——应用管理中心——创建应用，填写好应用的简单信息后在应用秘钥界面就能查看到集成SDK所需要的各种Key。

![](http://baastest.droi.cn/Uploads/DocFile/57870b46d16b5.png)

创建应用完成后请进入应用详情界面完善应用的详细信息——Android的PackageName，应用图标以及应用描述。

![](http://baastest.droi.cn/Uploads/DocFile/57870b7553163.png)

## 第三步：下载Core SDK

打开[DroiBaaS官网](http://baastest.droi.cn/)，点击上方导航栏的下载按钮，就可以下载到所有的SDK，其中DroiCoreSDK是DroiBaaS的基础SDK，安装其他SDK之前都需要先安装DroiCoreSdk。

![](http://baastest.droi.cn/Uploads/DocFile/57870bbd6db9a.png)

## 第四步：安装Core SDK

下载好SDK后开始安装，第二步中获取到的一些应用信息需要配置在AndroidManifest.xml中,目前DroiCoreSDK最低支持Android API Level 15 

### <a id="androidstudio_start"></a>Android Studio 安装

#### 使用 Gradle 安裝

`Android Studio` 使用 `Gradle` 构建手机应用程序，Gradle 可直接使用 `Maven` 套件，我们建议直接使用此方式安装 DroiCore SDK，避免手动下载困扰。

1. 在`Project`的`build.gradle`文件中增加`DroiCore SDK`的来源地址如下：

    ``` groovy
    buildscript {
        repositories {
            jcenter()
        }
        dependencies {
            classpath 'com.android.tools.build:gradle:2.1.2'
            classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
        }
    }
    ```

2. 在`app`下的`build.gradle`文件增加`DroiCore SDK`及相关的`libraries`如下：

    ``` groovy
    // 开启使用APT功能
    apply plugin: 'android-apt'
    
    repositories {
        jcenter()
        // DroiCore SDK来源地址
        maven {
            url "https://github.com/DroiBaaS/DroiBaaS-SDK-Android/raw/master/"
        }
    }

    dependencies {
        compile fileTree(dir: 'libs', include: ['*.jar'])
        // 使用APT工具产生辅助程序代码
        apt 'com.droi.sdk:annotationprocessor:0.5.+'
        // DroiCore SDK使用的3rd http函数库
        compile 'com.squareup.okhttp3:okhttp:3.0.1'
        // DroiCore SDK
        compile 'com.droi.sdk:Core:0.5.+'
    }
    ```
3. 在 AndroidManifest 中加入 DroiBaaS 开发平台产生的 metadata 以及渠道号 

    ``` xml
    <meta-data android:name="com.droi.sdk.application_id" android:value="[DroiBaaS Application ID]" />
    <meta-data android:name="com.droi.sdk.platform_key" android:value="[DroiBaaS ClientKey]" />
    <meta-data android:name="com.droi.sdk.channel_name" android:value="[ChannelName]" />

    ```
    
### <a id="eclipse_start"></a>Eclipse 安装

#### 安装aar/jar文件

1. 请下载最新的 DroiBaaS SDK([aar](https://github.com/DroiBaaS/DroiBaaS-SDK-Android/tree/master/com/droi/sdk/Core/0.5.2898), [jar](https://github.com/DroiBaaS/DroiBaaS-SDK-Android/tree/master/com/droi/sdk/Core/0.5.2898_jar))，把 `Core.aar/.jar` 复制到工程下的 `libs` 目录；右键工程根目录，选择`Properties` -> `Java Build Path` -> `Libraries`，然后点击`Add External JARs...` 选择指向jar的路径，点击OK，即导入成功。`（ADT17及以上不需要手动导入）`

2. 请到[OkHttp](http://square.github.io/okhttp/)官方网站下载Okio以及OkHttp两个依赖库并复制到工程下的`libs`目录。

#### 设置跳过检查 droicoresdk.jar 里的 Native Library

进入 Eclipse Preference，将 Android -> Build 里的 `Force error when external jars contain native libraries` 取消打勾，如图示。

![SkipCheck](http://baastest.droi.cn/Uploads/DocFile/576a4cfbe65e7.png)

#### 设置 DroiQuery Annotation tool

1. 下载 library 并放工程目录下。(请勿放到工程目录下的 libs ，会有错误)[下载链接](https://github.com/DroiBaaS/DroiBaaS-SDK-Android/blob/master/com/droi/sdk/annotationprocessor/0.5.2898/annotationprocessor-0.5.2898.jar)

2. 在工程上按右键按 `Properties`，选择 `Java Compiler` -> `Annotation Processing`，把 `Enable project specific settings`，`Enable annotation processing`，`Enable processing in editor` 都打勾

![annotation](http://baastest.droi.cn/Uploads/DocFile/576a4d0b8ce0a.png)

3. 在 `Java Compiler` -> `Annotation Processing` -> `Factory Path` 里，将 `Enable project specific settings` 打勾，选择 `Add External JARs...`，选择工程目录下刚刚下载的 `annotationprocessor-0.5.2107.jar`

![annotationjar](http://baastest.droi.cn/Uploads/DocFile/576a4d153ae87.png)

#### 配置 AndroidManifest

1. 加入 DroiBaaS SDK 所需要的 Permissions

    ``` xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

    ```
    
2. 加入 DroiBaaS 开发平台产生的 metadata 以及渠道号 

    ``` xml
    <meta-data android:name="com.droi.sdk.application_id" android:value="[DroiBaaS Application ID]" />
    <meta-data android:name="com.droi.sdk.platform_key" android:value="[DroiBaaS ClientKey]" />
    <meta-data android:name="com.droi.sdk.channel_name" android:value="[ChannelName]" />

    ```
    
### 初始化

Core SDK安装完成后，需要调用初始化操作，在Application的`onCreate()`方法中调用，如果没有请创建，示例如下：  

``` java
package com.test.mydroibaasapplication;

import android.app.Application;
import com.droi.sdk.core.Core;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Core.initialize(this);
    }
}

```

如果没有定义自己的Application，开发者同时需要在AndroidManifest文件中为 Application 加入 `android:name=".MyApp"` 如下所示  

``` xml
<application
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:supportsRtl="true"
    android:theme="@style/AppTheme"
    android:name=".MyApp">

    <activity android:name=".MainActivity">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
</application>

```
## 第五步：安装完成后进行测试

安装完成DroiCoreSdk开发者打开应用后可以看到SDK运行Log，实时掌握DroiCoreSdk的安装状态。测试可以正确使用各个SDK接口，并且相关功能运行正常，表明SDK集成工作正确完成。