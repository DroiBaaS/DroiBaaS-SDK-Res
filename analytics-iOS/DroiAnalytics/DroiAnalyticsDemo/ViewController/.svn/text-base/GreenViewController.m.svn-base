//
//  GreenViewController.m
//  DroiAnalyticsSDK
//
//  Created by Jon on 16/8/4.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "GreenViewController.h"
#import "DroiAnalytics.h"
@interface GreenViewController ()

@end

@implementation GreenViewController

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [DroiAnalytics beginLogPageView:@"GreenViewController"];
}

- (void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    [DroiAnalytics endLogPageView:@"GreenViewController"];
}
- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor greenColor];
    self.title = @"Green";

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
