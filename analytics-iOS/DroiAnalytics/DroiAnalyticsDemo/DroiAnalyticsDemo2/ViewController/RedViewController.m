//
//  RedViewController.m
//  DroiAnalyticsDemo2
//
//  Created by liaojun on 16/5/19.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "RedViewController.h"
#import "BlueViewController.h"
#import "GreenViewController.h"
#import <DroiAnalytics/DroiAnalytics.h>
@interface RedViewController ()

@end

@implementation RedViewController

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [DroiAnalytics beginLogPageView:@"RedViewController"];
}

- (void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    [DroiAnalytics endLogPageView:@"RedViewController"];
}
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)toBlue:(id)sender {
    
    BlueViewController *blueVC = [[BlueViewController alloc] init];
    [self.navigationController pushViewController:blueVC animated:YES];
}
- (IBAction)toGreen:(id)sender {
    
    GreenViewController *greenVC = [[GreenViewController alloc] init];
    [self.navigationController pushViewController:greenVC animated:YES];
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
