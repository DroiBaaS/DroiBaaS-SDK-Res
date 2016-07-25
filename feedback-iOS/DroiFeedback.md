# 意见反馈  

## 简介  
意见反馈可以帮助开发者在应用内收集用户问题反馈和意见建议，开发者可以及时回复用户的反馈，加强与用户之间的沟通，及时了解用户的需求，有利于提升产品质量。  

## 安装  

### 快速入门  
由于DroiFeedback SDK基于DroiCore SDK，所以请在安装DroiFeedback SDK之前仔细阅读[快速入门](http://baastest.droi.cn/Index/docStart.html)。


### 使用CocoaPods方式安装（推荐）

DroiFeedback SDK使用CocoaPods作为函数库的管理工具。我们推荐您使用CocoaPods这种方式来安装SDK，这样可以大大简化安装DroiFeedback SDK的流程。如果您未安装CocoaPods，请参考《CocoaPods安装和使用教程》完成安装。CocoaPods安装完成后，在您的项目根目录下创建一个Podfile文件，并添加如下内容：

	pod  'DroiFeedback'
在控制台中cd到Podfile文件所在目录，执行如下命令完成安装。

	pod  install
	
### 手动安装
#### 1. 安装DroiCore SDK
由于DroiFeedback SDK依赖于DroiCore SDK，所以请在安装DroiFeedback SDK之前仔细阅读[快速入门](http://baastest.droi.cn/Index/docStart.html) ，完成DroiCore SDK的安装。

#### 2. 导入SDK到应用程序项目

将SDK包解压，在XCode中选择”Add files to 'Your project name’…”，将解压后文件夹中的`DroiFeedback.framework`添加到你的工程目录中。

#### 3. 添加必要的框架
点击`Build Phases`选项，在`Link Binary With Libraries`中添加以下内容：
* Foundation.framework
* UIKit.framework
	
## 使用  
 
### 1.调用反馈页面  
在需要调用反馈的ViewController中调用如下代码：  

    [DroiFeedback callFeedbackWithViewController:self];
  
### 2.设置userId  

	[DroiFeedback setUserId:userId];
 
 ***注意：***  
在没有设置userId时，会生成随机匿名userId。请在呼叫反馈页面前确保都调用`setUserId`方法。该接口保证用户在其他设备上登录时同样能够获取到该用户对应的反馈，如果不设置此userid，反馈默认对应到匿名用户，用户清除应用数据会丢失匿名用户ID，导致反馈无法对应。如果使用了DroiBaaS提供的DroiUser登录，DroiFeedback会自动设置为当前登录的userId。
### 3.设置主色调

	[DroiFeedback setColor:[UIColor redColor]];


### 应用内页面  
用户可以在此页面进行反馈提交以及回复查看。  
<img src="http://i.imgur.com/1EjjuKa.png" width="300"> <img src="http://i.imgur.com/jKulYDC.png" width="300">  

### 后台页面  
在后台页面可以查看用户反馈量、今日回复量和总反馈量。
在反馈列表中，可以选中一条反馈进行回复。回复后，反馈用户可以在反馈回复页面看到回复。