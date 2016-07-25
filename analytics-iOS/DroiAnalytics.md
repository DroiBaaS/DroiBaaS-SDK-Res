# 统计分析
## 简介
卓易统计(DroiAnalytics)是一项面向移动应用开发者的数据统计服务。开发者集成DroiAnalytics SDK之后，应用的使用数据会传输到DroiBaaS服务器。开发者通过DroiBaaS web后台可以查看数据的分析结果。开发者可以通过数据分析结果全方位了解用户行为，调整优化应用，为用户提供更好的产品体验。
## 安装

### 开发环境
- 操作系统：Mac OS X
- 开发工具：Xcode 6.x 及以上版本
- iOS版本：需要iOS 8.0及以上系统

### 使用CocoaPods方式集成（推荐）
DroiAnalytics SDK使用CocoaPods作为函数库的管理工具。我们推荐您使用CocoaPods这种方式来安装SDK，这样可以大大简化安装DroiAnalytics SDK的流程。如果您未安装CocoaPods，请参考《CocoaPods安装和使用教程》完成安装。CocoaPods安装完成后，在您的项目根目录下创建一个Podfile文件，并添加如下内容：

	pod  'DroiAnalytics'
在控制台中cd到Podfile文件所在目录，执行如下命令完成安装。

	pod  install

### 手动安装
#### 1. 安装DroiCoreSDK
由于DroiAnalytics SDK依赖于DroiCore SDK，所以请在安装DroiAnalytics SDK之前仔细阅读[快速入门](http://baastest.droi.cn/Index/docStart.html) ，完成DroiCore SDK的安装。
#### 2. 导入SDK到应用程序项目

将SDK包解压，在XCode中选择”Add files to 'Your project name’…”，将解压后的DroiAnalyticsLib文件夹中的`DroiAnalytics.framework`添加到你的工程目录中。
#### 3. 添加必要的框架
点击`Build Phases`选项，在`Link Binary With Libraries`中添加以下内容：
- Foundation.framework
- UIKit.framework
- CoreLocation.framework
- CoreTelephony.framework

## 使用 
### 初始化SDK
在AppDelegate.m中初始化DroiAnalytics SDK:

	- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOption{
	
    	[DroiAnalytics startAnalytics];
    	return YES;
	}
	

### 数据上报策略
DroiAnalytics SDK 为开发者提供了两种数据上报策略：

- 即时上报：每当有一条新的统计数据产生，就会激发一次上报（不考虑网络环境）。如果应用程序此时处在不联网状态，那么消息将会缓存在本地，下次再尝试发送。

- 非即时上报：开发者可以指定从半小时到一天的任意时间间隔上报数据记录，也可指定只在WIFI环境下才上报。如果上报数据时应用处在非指定的网络环境下（无网或非WIFI网络），那么消息将会缓存在本地，下次再尝试发送。

DroiAnalytics SDK默认为即时上报(实际发生间隔约为3min)，可以通过SDK预留的接口修改发送策略。

	//设置为只在WIFI网络下，半小时的时间间隔上报数据
 	[DroiAnalytics setScheduleConfigTime:30 onlyInWifi:YES];
**注意：如果需要修改发生策略，请在初始化SDK之后立即调用才能正确设置**

### 页面统计功能
DroiAnalytics SDK 提供了页面统计的功能，开发者可以在需要统计的页面中调用配对方法才能准确的统计页面使用情况。页面统计集成正确，才能够获取正确的页面访问路径、访问深度（PV）的数据。在需要统计页面访问情况的页面中配对调用如下方法：

	- (void)viewDidAppear:(BOOL)animated{
    	[super viewDidAppear:animated];
    	[DroiAnalytics beginLogPageView:@“pageName”];
    }

	- (void)viewDidDisappear:(BOOL)animated{
    	[super viewDidDisappear:animated];
    	[DroiAnalytics endLogPageView:@"pageName"];
	}
**注意：pageName为页面名称，可自定义，但在同一个view下应保持一致，否则无法准确统计页面访问记录。**
### 自定义事件统计
DroiAnalytics SDK提供了自定义事件统计功能。开发者可结合应用的业务逻辑，添加自定义参数的统计。
#### 1. 计数事件
计数类型事件通常用来描述一个事件累计发生的次数。适用场景如按钮点击、界面进入、用户输入等。统计后台会对这类时间做总发生次数、总覆盖用户数等统计计算。
例如，统计视频应用中某个视频的收藏次数，可以在收藏的方法中调用：

	//attributes为附带参数，可自定义也可为nil
	NSDictionary *att = @{@"name":@"Jackass",@"type":@"funny"};
	[DroiAnalytics event:@"collect" attributes:att];

#### 2. 计算事件
计算类型事件通常描述一个带数值的事件的发生,适用的场景如用户消费事件，附带的数值是每次消费的金额;下载文件事件,附带的数值是每次下载消耗的时长等。统计后台会对这类事件做累加、分布、按次平均、按人平均等统计计算。
例如，某电商应用书籍类消费金额统计：

	//attributes为附带参数，可自定义也可为nil
	NSDictionary *att = @{@"name":@"A Brief History Of Time",@"type":@"book"};
	//num 代表此次事件的数值
	[DroiAnalytics event:@"buy" attributes:att num:99 ];

### 错误统计
DroiAnalytics SDK会收集应用的崩溃信息，并且在下次应用打开时上传至服务器。此功能默认为开启状态，不需要开发者手动调用。如果开发者自己实现了错误信息统计，可以调用以下方法关闭错误统计功能：

```
[DroiAnalytics setCrashReportEnabled:NO];
```
### 地理位置统计
DroiAnalytics SDK提供给开发者用于统计app用户地理位置信息的功能，开发者需要调用以下接口上传地理位置信息。

```
//latitude 为纬度 longitude 为经度
[DroiAnalytics setLatitude:88.0 longitude:88.0];
```
也可调用如下接口，直接上传地理位置的CLLocation对象

	+ (void)setLocation:(CLLocation *)location;

app用户的地理位置信息需要开发者自己调用苹果的地理位置信息API获取，以下为一段示例代码：

```
#import "ViewController.h"
#import <DroiAnalytics.h>
#import <CoreLocation/CoreLocation.h>

@interface ViewController ()<CLLocationManagerDelegate>
@property (nonatomic ,strong) CLLocationManager * locationManager;
@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // 判断定位操作是否被允许
    self.locationManager =[[CLLocationManager alloc]init];
    if (![CLLocationManager locationServicesEnabled]) {
        NSLog(@"定位服务当前可能尚未打开，请设置打开！");
        return;
    }
    //如果没有授权则请求用户授权
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0)
    {
        if ([CLLocationManager authorizationStatus]==kCLAuthorizationStatusNotDetermined){
            [self.locationManager requestWhenInUseAuthorization];
        }
    }
    //设置代理
    self.locationManager.delegate=self;
    //设置定位精度
    self.locationManager.desiredAccuracy=kCLLocationAccuracyNearestTenMeters;
    //定位频率,每隔多少米定位一次
    CLLocationDistance distance = 10.0;
    self.locationManager.distanceFilter=distance;
    //启动跟踪定位
    [self.locationManager startUpdatingLocation];
}

#pragma mark - CoreLocation Delegate
-(void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    //此处locations存储了持续更新的位置坐标值，取最后一个值为最新位置，如果不想让其持续更新位置，则在此方法中获取到一个值之后让locationManager stopUpdatingLocation
    CLLocation *currentLocation = [locations lastObject];
    
    //通过DroiAnalysis SDK上报地理位置信息
    [DroiAnalytics setLocation:currentLocation];
    
    //系统会一直更新数据，直到选择停止更新，因为我们只需要获得一次经纬度即可，所以获取之后就停止更新
    [manager stopUpdatingLocation];
}


- (void)locationManager:(CLLocationManager *)manager
       didFailWithError:(NSError *)error {
        //获取地理位置错误处理
        NSLog(@"An error occurred = %@", error);
}
```
### Log开关功能
DroiAnalytics SDK 默认开启Log功能，如果开发者需要关闭Log可以手动调用以下方法关闭Log：

```
[DroiAnalytics setLogEnabled:NO];
```