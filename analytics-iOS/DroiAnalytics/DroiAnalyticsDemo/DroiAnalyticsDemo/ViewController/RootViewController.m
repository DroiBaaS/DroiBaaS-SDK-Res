//
//  RootViewController.m
//  DroiAnalysisDemo
//
//  Created by Jon on 16/8/2.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "RootViewController.h"
#import "RedViewController.h"
#import <DroiCoreSDK/DroiCoreSDK.h>
#import <DroiAnalytics/DroiAnalytics.h>
@interface RootViewController ()<CLLocationManagerDelegate>
@property (weak, nonatomic) IBOutlet UILabel *appIdLabel;
@property (weak, nonatomic) IBOutlet UILabel *channelLabel;
@property (weak, nonatomic) IBOutlet UILabel *didLabel;

@property (weak, nonatomic) IBOutlet UITextField *key1;
@property (weak, nonatomic) IBOutlet UITextField *value1;

@property (weak, nonatomic) IBOutlet UITextField *key2;
@property (weak, nonatomic) IBOutlet UITextField *value2;
@property (weak, nonatomic) IBOutlet UITextField *numberTF;

@property (nonatomic ,strong) CLLocationManager * locationManager;

@end

@implementation RootViewController

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
    self.appIdLabel.text = [DroiCore getDroiAppId];
    self.channelLabel.text = [DroiCore getChannelName];
    self.didLabel.text = [DroiCore getDroiDeviceId];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}
- (IBAction)toPageAnalytics:(id)sender {
    
    RedViewController *redVC = [[RedViewController alloc] init];
    UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:redVC];
    [self presentViewController:nav animated:YES completion:nil];
    
}

- (IBAction)countEvent:(id)sender {
    
    NSMutableDictionary *att = [[NSMutableDictionary alloc] init];
    if (self.key1.text != nil && self.value1.text != nil) {
        [att setObject:self.value1.text forKey:self.key1.text];
    }
    [DroiAnalytics event:@"计数事件" attributes:att];
    self.key1.text = nil;
    self.value1.text = nil;
}

- (IBAction)valueEvent:(id)sender {
    
    NSMutableDictionary *att = [[NSMutableDictionary alloc] init];
    if (self.key2.text != nil && self.value2.text != nil) {
        [att setObject:self.value2.text forKey:self.key2.text];
    }
    NSInteger num = [self.numberTF.text integerValue];
    [DroiAnalytics event:@"计算事件" attributes:att num:num];
    self.key2.text = nil;
    self.value2.text = nil;
    self.numberTF.text = nil;
}

- (IBAction)crash:(id)sender {
    
    NSArray *array = @[@1,@2];
    NSNumber *num = array[2];
    NSLog(@"%@",num);
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    
    [self.view endEditing:YES];
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
@end
