# 快速入门-iOS

## DroiBaaS快速入门流程示意图 

![](http://baastest.droi.cn/Uploads/DocFile/57870a0bc708d.jpg)

## 第一步：注册成为DroiBaaS开发者

打开[DroiBaaS官网](http://baastest.droi.cn/)，点击右上角的注册按钮，填入邮箱或者手机号注册成为DroiBaaS的开发者。同时也支持新浪微博和QQ的快捷登录方式。注册成为开发者并且登录后，请完善开发者详细信息，以便更好的沟通以及问题的解决。

![](http://baastest.droi.cn/Uploads/DocFile/57870a6c795f3.png)

**友情提醒：**如果是企业开发者，建议在注册账号时使用企业邮箱，尽量避免使用个人邮箱，防止由于个人离职带来的问题。

## 第二步：创建应用，获取应用信息

登录进入管理后台——应用管理中心——创建应用，填写好应用的简单信息后在应用秘钥界面就能查看到集成SDK所需要的各种Key。

![](http://baastest.droi.cn/Uploads/DocFile/57870b46d16b5.png)

创建应用完成后请进入应用详情界面完善应用的详细信息——iOS的BundleId，应用图标以及应用描述。

![](http://baastest.droi.cn/Uploads/DocFile/57870b7553163.png)

## 第三步：下载Core SDK

打开[DroiBaaS官网](http://baastest.droi.cn/)，点击上方导航栏的下载按钮，就可以下载到所有的SDK，其中DroiCoreSDK是DroiBaaS的基础SDK，安装其他SDK之前都需要先安装DroiCoreSdk。

![](http://baastest.droi.cn/Uploads/DocFile/57870bbd6db9a.png)
## 第四步：安装Core SDK
下载好SDK后开始集成，第二步获取到的一些应用信息需要配置在代码中


### 开发环境
- 操作系统：Mac OS X
- 开发工具：Xcode 6.x 及以上版本
- iOS版本：需要iOS 8.0及以上系统

### 添加https支持
由于iOS9改用更安全的https，为了能够在iOS9中正常使用DroiAnalytics SDK，请在"Info.plist"中添加如下配置，否则影响SDK的使用。

```
<key>NSAppTransportSecurity</key>
<dict>
<key>NSAllowsArbitraryLoads</key>
<true/>
</dict>
```	
配置完成后Info.plist应该如下图所示：

![此处输入图片的描述][1]
 
### 使用CocoaPods方式安装（推荐）
DroiCore SDK 使用了CocoaPods作为链接库的管理工具。我们推荐您使用CocoaPods的方式安装SDK，如此以来可以大大简化DroiCore SDK的安装流程

在项目目录下的Podfile文件中添加以下内容：

	pod 'DroiCoreSDK'	# DroiCoreSDK核心元件	
	
		# 执行CoreSDK安裝script file
		post_install do |installer|
			require './Pods/DroiCoreSDK/scripts/postInstall.rb'
			DroiCoreParser.installParser()
		end
	end
在控制台中cd到Podfile文件所在目录，执行如下命令完成安装。

	pod  install
	
DroiCore安装完成后在`Build Phases`选项下新增一个**Run Script Phase**，作为代码的前置处理之用。	
### 手动安装

#### 1. 导入SDK到应用程序项目

将SDK包解压将解压后的DroiCoreSDK文件夹复制到你的工程的根目录中。目录结构如下图所示:

![此处输入图片的描述][2]

#### 2. 添加framework到项目
将DroiCoreSDK文件夹中的DroiCoreSDK.framework添加到项目中。如下图所示:

![此处输入图片的描述][3]

#### 3. 点击`General`选项,添加DroiCore.framework至`Embedded Binaries`中
由于DroiCore SDK使用的动态链接库形式，所以需要将DroiCore.framework添加至`Embedded Binaries`中，如下图所示：

![此处输入图片的描述][4]

#### 4. 添加Run Script Phase
点击`Build Phases`选项，新增一个Run Script Phase，并移动至Compile Sources之前，添加完成之后添加命令`"${PROJECT_DIR}/DroiCoreSDK/scripts/baas_cli" parse "${SRCROOT}"`至Shell之中，如下图所示：

![此处输入图片的描述][5]

#### 5. 添加必要的框架
点击`Build Phases`选项，在`Link Binary With Libraries`中添加以下内容：

- libsqlite3.tbd
- libz.tbd
- libobjc.tbd
- SystemConfiguration.framework

#### 6. 复制解压后产生的前置处理文件至项目目录中。
将DroiCore SDK文件夹中sources目录下的`DroiBaaS.plist`以及`DroiBaaS.h/.m (Objective-C)`或`DroiBaaSForSwift.swift (Swift)`加入项目之中，如下图，并执行一次Build的动作以更新产生前置处理文件

![此处输入图片的描述][6]

### 初始化SDK
#### 1. 在Info.plist中添加Key
在 Info.plist 加入`DROI_APP_ID`值为开放平台上创建应用时产生的`Application ID`，再加入`DROI_PLATFORM_ID`值为开放平台上创建应用时产生的`ClientKey`。如果需要设置渠道，请加入`DROI_CHANNEL_NAME`值为需要渠道名称（如果不设置的话，默认渠道为App Store）。如果需要使用DroiPush的相关功能，需要额外添加`DROI_APP_SECRET`值为开放平台上创建应用时产生的`SecretKey`。如下图所示：

![此处输入图片的描述][7]


#### 2. 在AppDelegate中初始化SDK
***Objc***

	#import <DroiCoreSDK/DroiCoreSDK.h>
	- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOption{
	
    	//初始化DroiCore SDK
    	[DroiCore initializeCore];
    	return YES;
	}

***swift***

	 func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool {

        // 初始化DroiCore SDK
        DroiCore.initializeCore()
        return true
    }
    
    
## 第五步：安装完成后进行测试

安装完成DroiCoreSdk开发者打开应用后可以看到SDK运行Log，实时掌握DroiCoreSdk的安装状态。测试可以正确使用各个SDK接口，并且相关功能运行正常，表明SDK集成工作正确完成。


  [1]: http://baastest.droi.cn/Uploads/DocFile/5767a5524636e.jpeg
  [2]: http://baastest.droi.cn/Uploads/DocFile/5767a599a8b1b.png
  [3]: http://baastest.droi.cn/Uploads/DocFile/5767a5dedf743.jpeg
  [4]: http://baastest.droi.cn/Uploads/DocFile/5767a6249256b.png
  [5]: http://baastest.droi.cn/Uploads/DocFile/5767a629ef2a8.png
  [6]: http://baastest.droi.cn/Uploads/DocFile/5767a61f188c1.jpeg
  [7]: http://baastest.droi.cn/Uploads/DocFile/5767a61a810f3.jpeg