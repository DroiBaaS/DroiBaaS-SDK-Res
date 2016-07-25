//
//  ViewController.m
//  DroiAnalyticsDemo2
//
//  Created by liaojun on 16/5/19.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "ViewController.h"
#import "RedViewController.h"
#import <DroiCoreSDK/DroiCoreSDK.h>
#import <DroiAnalytics/DroiAnalytics.h>
@interface ViewController ()
@property (weak, nonatomic) IBOutlet UILabel *appIdLabel;
@property (weak, nonatomic) IBOutlet UILabel *channelLabel;
@property (weak, nonatomic) IBOutlet UITextField *key1;
@property (weak, nonatomic) IBOutlet UITextField *value1;
@property (weak, nonatomic) IBOutlet UITextField *key2;
@property (weak, nonatomic) IBOutlet UITextField *value2;
@property (weak, nonatomic) IBOutlet UITextField *number;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.appIdLabel.text = [DroiCore getDroiAppId];
    self.channelLabel.text = [DroiCore getChannelName];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}
- (IBAction)countEvent:(id)sender {
    
    NSMutableDictionary *att = [[NSMutableDictionary alloc] init];
    if (self.key1.text != nil && self.value1.text != nil) {
        [att setObject:self.value1.text forKey:self.key1.text];
    }
    [DroiAnalytics event:@"计数事件" attributes:att];
}
- (IBAction)valueEvent:(id)sender {
    
    NSMutableDictionary *att = [[NSMutableDictionary alloc] init];
    if (self.key2.text != nil && self.value2.text != nil) {
        [att setObject:self.value2.text forKey:self.key2.text];
    }
    NSInteger num = [self.number.text integerValue];
    [DroiAnalytics event:@"计算事件" attributes:att num:num];
}

- (IBAction)pageInfo:(id)sender {
    
    RedViewController *redVC = [[RedViewController alloc] init];
    [self.navigationController pushViewController:redVC animated:YES];
}
- (IBAction)crash:(id)sender {
    
    NSArray *array = @[@1,@2];
    NSNumber *num = array[2];
    NSLog(@"%@",num);
}
@end
