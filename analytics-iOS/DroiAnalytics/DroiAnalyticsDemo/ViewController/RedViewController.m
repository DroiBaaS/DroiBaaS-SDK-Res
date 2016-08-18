//
//  RedViewController.m
//  DroiAnalyticsSDK
//
//  Created by Jon on 16/8/4.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "RedViewController.h"
#import "BlueViewController.h"
#import "GreenViewController.h"
#import "DroiAnalytics.h"
@implementation RedViewController

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [DroiAnalytics beginLogPageView:@"RedViewController"];
}

- (void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    [DroiAnalytics endLogPageView:@"RedViewController"];
}

- (void)viewDidLoad{
    [super viewDidLoad];
    [self setupNav];
}

- (IBAction)toGreen:(id)sender {
    
    GreenViewController *greenVC = [[GreenViewController alloc] init];
    [self.navigationController pushViewController:greenVC animated:YES];
}

- (IBAction)toBlue:(id)sender {
    
    BlueViewController *blueVC = [[BlueViewController alloc] init];
    [self.navigationController pushViewController:blueVC animated:YES];

}

- (void)back{
    
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)setupNav{
    
    self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"back" style:UIBarButtonItemStylePlain target:self action:@selector(back)];
    self.title = @"页面统计";
}
@end
