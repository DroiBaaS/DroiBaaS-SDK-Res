# DroiPush产品与集成说明  

## 简介
### 什么是卓易推送
卓易推送(DroiPush)是面向开发者提供的一种消息推送服务模块，应用开发者可以通过开放的API或者DroiBaaS管理后台向集成SDK的应用推送消息。
### 卓易推送优势
- 降低开发成本  
开发者只需简单集成卓易推送SDK，就可在短期内使应用具有推送的能力，免去了开发者额外投入大量人力、服务器资源。

- 丰富的展现样式  
提供了多种默认消息展现样式，包含有普通的通知、图片通知以及大图通知，同时还支持自定义消息。

- 节省耗电与网络流量  
使用UDP协议保证大量终端的在线连接与控制，在保证消息传递的前提下降低网络流量和耗电量。

### 卓易推送工作原理

![cmd-markdown-logo](http://baastest.droi.cn/Uploads/DocFile/57636dc5c8116.png)

## 安装

### 安装SDK  
#### 快速入门
由于卓易推送SDK基于卓易CoreSDK，所以请在安装卓易推送SDK之前仔细阅读[快速入门](http://baastest.droi.cn/Index/docStart.html)，并确保已经完成快速入门的所有步骤 。

#### Android Studio环境安装  

1. 配置依赖关系  
Android Studio环境下只需要在Project的`build.gradle`中添加如下依赖：
	dependencies {
		/*其他依赖 */
		compile 'com.droi.sdk:push:+'
		compile 'com.android.support:support-v4:23.3.0'
	}

2. 配置Secret  
此外，需要在 AndroidManifest.xml 中配置secret，如下所示    
  
        <!--推送消息加密秘钥，应用创建后由平台自动生成，以下内容为示例-->
	    <meta-data android:name="com.droi.sdk.secret_key" android:value="你的secret" />  

**注意：  
应用工程的build.gradle中需要配置应用包名字段`applicationId`，否则会影响推送；** 


#### Eclipse环境安装  

1. 安装步骤
[下载卓易推送SDK](http://baastest.droi.cn/Index/download.html)，解压后将 `droipushsdk.jar` 包导入到工程的 `libs` 目录下；右键工程根目录，选择`Properties` -> `Java Build Path` -> `Libraries`，然后点击`Add External JARs...` 选择指向jar的路径，点击OK，即导入成功。**（ADT17及以上不需要手动导入）**  
将`res`文件夹直接复制到工程目录下，和工程本身`res`目录合并。请不要随意删除其中的文件**（`res`文件均以dp开头）**。

2. 组件配置
在 AndroidManifest.xml 中进行必要的配置  

        <!--推送消息加密秘钥，应用创建后由平台自动生成，以下内容为示例-->
	    <meta-data android:name="com.droi.sdk.secret_key" android:value="你的secret" />

		<!--注册推送服务-->
		<service
	    	android:name="com.droi.sdk.push.DroiPushService"
	    	android:exported="true" >
			<!--当前使用推送服务的版本号-->
			<meta-data
                android:name="SERVICE_VERSION"
                android:value="1.0.1" >
            </meta-data>
	    	<intent-filter>
	        	<action android:name="com.droi.sdk.push.action.START" />
	    	</intent-filter>
		</service>
		<!--注册推送广播接收器组件-->
		<receiver android:name="com.droi.sdk.push.TickAlarmReceiver" ></receiver>
		<receiver android:name="com.droi.sdk.push.DroiPushReceiver" >
	    	<intent-filter>
	        	<action android:name="android.intent.action.PACKAGE_REMOVED" />
	        	<action android:name="android.intent.action.PACKAGE_ADDED" />
	        	<action android:name="android.intent.action.PACKAGE_REPLACED" />
	        	<data android:scheme="package" />
	    	</intent-filter>
	    	<intent-filter>
	        	<action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
	        	<action android:name="android.intent.action.PHONE_STATE" />
	        	<action android:name="android.intent.action.USER_PRESENT" />
	        	<action android:name="android.intent.action.USER_CLEARNOTIFY" />
	        	<action android:name="com.droi.sdk.push.action.DATA" />
	        	<!-- 请使用当前应用的包名作为前缀 -->
	        	<action android:name="你的应用包名.Action.START" />
	    	</intent-filter>
		</receiver>
		<!--注册推送弹出框Activity组件-->
		<activity
	    	android:name="com.droi.sdk.push.DroiPushActivity"
	    	android:configChanges="orientation|keyboardHidden|screenSize"
	    	android:exported="true"
	   		android:label="@string/app_name"
	    	android:screenOrientation="portrait"
	    	android:theme="@style/DroiDialogStyle" >
	    	<intent-filter>
	        	<action android:name="com.droi.sdk.push.action.SHOW_DETAIL" />
	    	</intent-filter>
		</activity>

    **注意：  
    DroiPushService需要配置服务的版本号字段SERVICE_VERSION，否则可能会影响推送效果；** 

3. 权限配置  

    	<!-- permission used in push sdk -->
    	<uses-permission android:name="android.permission.WAKE_LOCK" />
    	<uses-permission android:name="android.permission.VIBRATE" />
    	<uses-permission android:name="android.permission.FLASHLIGHT" />
    	<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    	<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

## 使用
1. 初始化  
在Application的`onCreate()`方法中调用：  

		DroiPush.initialize(context)；  
		
2. 获取设备与应用ID、渠道
SDK提供接口获取设备ID(deviceId)、应用ID(appId)、渠道号(channel)以及密码(secret)信息。其中，deviceId首次生成必须联网，appId、channel以及secret通过读取AndroidManifest配置内容返回相应数值。

	- 获取deviceId  
	
		    String deviceId = DroiPush.getDeviceId(context);

	- 获取appId  
	
		    String appId = DroiPush.getAppId(context);

	- 获取channel  
	
		    String channel = DroiPush.getChannel(context);

	- 获取secret  
	
		    String secret = DroiPush.getSecret(context);

    **注意：  
	deviceId首次获取需要联网，无网络情况下返回null；  
	channel必须在AndroidManifest中进行配置，否则默认返回"UNKNOWN_CHANNEL"；  
    appId与secret必须在AndroidManifest中配置，否则返回null；** 

3. 设置标签  
   提供接口设置应用标签，推送时可以选定推送目标为对应标签，推送服务器则向该标签的分组用户推送消息，标签参数tags为`String[]`。addTag方法参数isCovered表示是否使用tags覆盖已有的标签，true表示覆盖，false则并入已有的标签当中。

	- 添加标签(覆盖)  
	
		    DroiPush.addTag(context, tag, true);

	- 添加标签(合并)  
	
		    DroiPush.addTag(context, tag, false);

	- 删除标签

			DroiPush.removeTag(context, tag);
			
	- 删除所有标签

			DroiPush.removeAllTag(context);	
			
	- 获取所有标签

			DroiPush.getTags(context);	

	**注意：  
	应用tag数量最多限制为1024个，每个长度最多为256字符；  
    tag内容不能加入URLEncode等其他的变换处理，建议使用原生字符串；**    

4. 设置静默时间  
   静默时间指的每天对应的时间段内，不展示推送消息；比如为了防止骚扰到用户，可以把每天0:00到6:00设置为静默时间。 
	- 设置静默时间  

			DroiPush.setSilentTime(context, 0, 0, 6, 0);

	- 获取静默时间  

			int[] time = DroiPush.getSilentTime(context);
			
	- 清除静默时间  

			DroiPush.clearSilentTime(context);

5. 启用/禁用推送  
禁用后，推送功能将不可用，请谨慎调用。

	- 启用/禁用推送

    		DroiPush.setPushEnabled(context, true);

	- 获取推送开关状态

			boolean isEnabled = DroiPush.getIsPushEnabled(context);

6. 透传消息处理  
DroiMessageHandler为消息展示定制类，提供透传消息处理接口`onHandleCustomMessage`，以下为示例代码：

        DroiPush.setMessageHandler(new DroiMessageHandler() {
            @Override
            public void onHandleCustomMessage(final Context context, final String message) {
                // TODO Auto-generated method stub
                //message为透传消息内容，应用获取后自行处理
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Demo Custom Msg:" + message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

## FAQ 
### 卓易推送支持哪些消息类型？   
卓易推送支持通知栏消息和透传消息，具体介绍如下：  

- 通知栏消息  
 在通知栏展示，用户点击通知的后续动作可以为打开应用、打开网页以及下载应用。展现形式主要分为普通通知消息、图片通知消息和大图通知消息。  
	+ 普通通知消息  
普通通知消息如下所示，主要包括通知标题、通知内容摘要以及通知的图标  
![cmd-markdown-logo](http://baastest.droi.cn/Uploads/DocFile/57636d5beb711.png)

	+ 图片通知消息  
图片通知消息在通知栏中展示一张选定的图片  
![cmd-markdown-logo](http://baastest.droi.cn/Uploads/DocFile/57636e18a5f25.png)

	+ 大图通知消息  
大图通知消息将会在通知栏中展示一张选定的大图片，展示的效果更好，更直观  
![cmd-markdown-logo](http://baastest.droi.cn/Uploads/DocFile/57636e8e44111.png)

	**注意：某些机型可能不支持大图通知展现样式，会以普通消息样式展示。**

- 透传消息  
除了通知栏消息外，卓易推送还支持透传方式传递消息。该方式直接将接收到的消息原封不动地传递给客户端应用，由应用决定如何处理接收到的消息。

	**注意：某些机型可能导致应用无法在后台启动，此时透传消息将无法到达目标应用**

### 卓易推送支持哪些消息控制类型？  

- 即时消息  
推送消息到达推送目标后立即展示，展示后失效；

- 定时消息  
推送消息到达目标后在规定时间段内展示，展示之后失效；

- 循环消息  
推送目标接收到循环消息后间隔指定的时间循环展示；

### 卓易推送支持哪些通知提醒动作？
 
支持通知展示时点亮呼吸灯、响铃和振动。

### 什么是离线时间？  
离线时间也可理解为推送消息的有效期，推送目标只有在有效期之内上线才会收到推送消息，超出有效期消息就会被丢弃处理。

### 什么是单播，组播和广播？

- 单播目标  
推送平台可以通过deviceId指定单播消息的目标；

- 组播目标  
提供接口给应用打标签，推送时可以选定推送目标为对应标签，推送服务器则向该标签的分组用户推送消息；

- 群播目标  
推送服务器向该应用的所有用户推送消息；  

### 管理后台统计推送消息的哪些指标？
开发者可以在推送平台上查看用用推送统计数据，对推送的信息进行分析。以下是推送数据统计的指标：

- 已推送消息数：单播消息、组播消息和群发消息数；

- 送达消息数：已经接收到消息的数量；

- 通知展示数量：展示在通知栏消息的数量；

- 通知点击数：通知栏中通知点击打开的数量；
