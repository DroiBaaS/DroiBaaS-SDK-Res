# 用户反馈

## 简介
用户反馈可以帮助开发者在应用内收集用户问题反馈和意见建议，开发者可以及时回复用户的反馈，加强与用户之间的沟通，及时了解用户的需求，有利于提升产品质量。

## 安装
### 快速入门
由于卓易用户反馈SDK基于卓易CoreSDK，所以请在安装卓易用户反馈SDK之前仔细阅读[快速入门](http://baastest.droi.cn/Index/docStart.html)，并确保已经完成快速入门的所有步骤。

### Eclipse 安装SDK
1. 下载[SDK压缩包]()，解压后将 `droifeedbacksdk.jar` 包导入到工程的 `libs` 目录下；右键工程根目录，选择`Properties` -> `Java Build Path` -> `Libraries`，然后点击`Add External JARs...` 选择指向jar的路径，点击OK，即导入成功。**（ADT17及以上不需要手动导入）**  
将`res`文件夹直接复制到工程目录下，和工程本身`res`目录合并。请不要随意删除其中的文件**（`res`文件都以`droi`开头）**。

2. 在 AndroidManifest 中配置组件 
    ``` xml
    <manifest...>
    <application...>
        <!--注册反馈页面-->
        <activity
            android:name="com.droi.sdk.feedback.DroiFeedbackActivity"
            android:label="@string/app_name"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
            </intent-filter>
        </activity>
    </application>
    </manifest>
    ```

### Android Studio 安装SDK
Android Studio环境下只需要在Project的`build.gradle`中添加如下依赖：
``` groovy
dependencies {
    /*其他依赖 */
    compile 'com.droi.sdk:feedback:1.0.+'
    compile 'com.android.support:support-v4:23.3.0'
}
```

## 使用
### 初始化
在Application的`onCreate()`方法中初始化。
``` java
DroiFeedback.initialize(this);
```

### 发送反馈
1. 调用反馈页面  
	可以调用我们提供的activity页面。  
    ``` java
    DroiFeedback.callFeedback(Context context);
    ```

2. 设置 userId
    ``` java
    DroiFeedback.setUserId(String userId);
    ```   
	***注意：***
	* userId用于标识用户，您可以传入您自有账号系统的用户唯一标识，该接口保证用户在其他设备上登录时同样能够获取到该用户对应的反馈。请确保每次调用callFeedback接口之前，调用该接口。  
	* 如果您使用了Droi CoreSDK中的DroiUser账号系统，无需手动调用该接口，反馈SDK会自动添加。  
	* 如果您没有设置userId，会使用Droi CoreSDK中DroiUser的匿名userId。

3. 自定义部分UI颜色

    1)设置 titleBar 的颜色
    ``` java
    DroiFeedback.setTitleBarColor(int titleBarColor);
    ```
    
    2)设置 sendButton 正常显示状态和点击状态下的颜色
    ``` java
    DroiFeedback.setSendButtonColor(int pressedColor, int normalColor);
    ```
    
    ***注意：***  
    设置的颜色是包含透明度的颜色，详情请查看`android.graphics.Color`类。除了用`Color`类来设置颜色，你可以直接用4位16进制数来表示颜色，例如`0xFF34b1e7`。
`Color`类你可能需要用到的方法用:
   * `Color.argb(int alpha, int red, int green, int blue)`
   * `Color.rgb(int red, int green, int blue)`,这时alpha隐式设为255（完全不透明）
   * `Color.parseColor(String colorString)`,colorString可以是RGB(#RRGGBB),ARGB(#AARRGGBB)或者颜色名称，具体请查看android文档

### 后台页面
在后台页面可以查看用户反馈量、今日回复量和总反馈量。  
在反馈列表中，可以选中一条反馈进行回复。回复后，反馈用户可以在反馈回复页面看到回复。  
![](http://baastest.droi.cn/Uploads/DocFile/5767a3458345f.png)