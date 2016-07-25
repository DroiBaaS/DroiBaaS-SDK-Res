//
//  BlueViewController.m
//  DroiAnalyticsDemo
//
//  Created by liaojun on 16/5/3.
//  Copyright © 2016年 icewind. All rights reserved.
//

#import "BlueViewController.h"
#import <DroiAnalytics/DroiAnalytics.h>
@interface BlueViewController ()

@end

@implementation BlueViewController

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [DroiAnalytics beginLogPageView:@"BlueViewController"];
}

- (void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    [DroiAnalytics endLogPageView:@"BlueViewController"];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
