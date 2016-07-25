# 第三方授权  

## 简介

卓易第三方登录(DroiOAuth) SDK是依托卓易市场、FreeMeOS庞大用户群体和海量优势资源，使得广大合作伙伴和第三方开发者接入卓易帐号功能后能够轻松享有卓易产品中的海量用户资源。用户可以方便快捷地使用卓易帐号登录开发者网站或者移动应用，同时为用户带来统一、友好的登录体验。
 
## 安装  

### 开发环境  
* 操作系统：Mac OS X等
* 开发工具：Xcode 6.x 及以上版本
* iOS版本：需要iOS 8.0及以上系统  

### 使用CocoaPods方式集成（推荐）

DroiOAuth SDK使用CocoaPods作为函数库的管理工具。我们推荐您使用CocoaPods这种方式来安装SDK，这样可以大大简化安装DroiOAuth SDK的流程。如果您未安装CocoaPods，请参考《CocoaPods安装和使用教程》完成安装。CocoaPods安装完成后，在您的项目根目录下创建一个Podfile文件，并添加如下内容：

	pod  'DroiOAuth'
在控制台中cd到Podfile文件所在目录，执行如下命令完成安装。

	pod  install
	
###  手动集成
#### 1. 安装DroiCore SDK

由于DroiOAuth SDK依赖于DroiCore SDK，所以请在安装DroiOAuth SDK之前仔细阅读[快速入门](http://baastest.droi.cn/Index/docStart.html) ，完成DroiCore SDK的安装。

#### 2. 导入SDK到应用程序项目

将SDK包解压，在XCode中选择”Add files to 'Your project name’…”，将解压后的DroiOAuthLib文件夹中的`DroiOAuth.framework`添加到你的工程目录中。。

#### 3. 添加必要的框架
点击Build Phases选项，在Link Binary With Libraries中添加以下内容：

* Foundation.framework
* UIKit.framework
* JavaScriptCore.framework

#### 4. 将DroiOAuth.framework加入到Copy Bundle Resources中
点击Build Phases选项，在Copy Bundle Resources中添加DroiOAuth.framework，如图所示：

![此处输入图片的描述][1]

## 使用  

### 登录授权获取token

在适合的页面调用登录接口后，会自动跳转到登录页面，登录完成后会在callback回调用户的token信息，开发者可以使用token获取相应的用户信息

示例：在当前页面中调用登录接口获取token

```
[DroiOAuth loginWithViewController:self Callback:^(id object, NSError *error) {
	if (error == nil) {
		NSDictionary *token = object;
		NSLog(@"token:%@",token);
	}
	else{
		NSLog(@"error:%@",error);
	}
}];
```   
### 获取用户相关信息

如果token正确，填入不同的scope可以获取到用户的不同信息，scope可以有以下三种：

* DroiOAuthScopeUserInfo：用户基础信息，包括用户昵称，性别等
* DroiOAuthScopeUserPhoneNumber：用户手机号信息
* DroiOAuthScopeUserEmail：用户邮箱信息

示例：根据调用登录接口获取到的tokenData数据获取用户基础信息、用户手机信息和用户邮箱信息

```
DroiOAuthScope scope = DroiOAuthScopeUserInfo|DroiOAuthScopeUserPhoneNumber|DroiOAuthScopeUserEmail;
[DroiOAuth getUserInfoWithToken:token Scope:scope Callback:^(id object, NSError *error) {
	if (error == nil) {        
		NSDictionary *userInfo = object;
		NSLog(@"userInfo:%@",userInfo);
	}
	else{
		NSLog(@"error:%@",error);
	}
}];
```

### 设置语言
DroiOAuth SDK 目前支持中文和英文两种语言，开发者可以调用以下接口更改登录页面的语言：

* DroiOAuthLanguageZH：中文
* DroiOAuthLanguageEN：英文

```
[DroiOAuth setLanguage:DroiOAuthLanguageEN];//设置语言为英文
```
### 设置Log开关
开发者可以根据需求开关DroiOAuth SDK内部的Log，建议在发布版本的时候关闭Log功能，节省资源。

```
[DroiOAuth setLogEnabled:NO];//关闭log
```
[1]: http://baastest.droi.cn/Uploads/DocFile/57692b2a0f2b6.png