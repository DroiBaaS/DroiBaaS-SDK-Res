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
#import "DroiAnalytics.h"
@interface RootViewController ()
@property (weak, nonatomic) IBOutlet UILabel *appIdLabel;
@property (weak, nonatomic) IBOutlet UILabel *channelLabel;
@property (weak, nonatomic) IBOutlet UILabel *didLabel;

@property (weak, nonatomic) IBOutlet UITextField *key1;
@property (weak, nonatomic) IBOutlet UITextField *value1;

@property (weak, nonatomic) IBOutlet UITextField *key2;
@property (weak, nonatomic) IBOutlet UITextField *value2;
@property (weak, nonatomic) IBOutlet UITextField *numberTF;
@end

@implementation RootViewController

- (void)viewDidLoad {
    [super viewDidLoad];
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
@end
