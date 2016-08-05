//
//  BlueViewController.m
//  DroiAnalyticsSDK
//
//  Created by Jon on 16/8/4.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "BlueViewController.h"
#import "DroiAnalytics.h"
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
    self.view.backgroundColor = [UIColor blueColor];
    self.title = @"Blue";
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
