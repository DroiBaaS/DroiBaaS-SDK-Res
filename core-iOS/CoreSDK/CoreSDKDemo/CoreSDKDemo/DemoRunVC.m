//
//  DemoRunVC.m
//  CoreSDKDemo
//
//  Created by Jon on 16/7/22.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "DemoRunVC.h"
#import "DroiDemo.h"
@interface DemoRunVC ()
@property(nonatomic,retain)UIBarButtonItem *runBtn;
@end

@implementation DemoRunVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.runBtn = [[UIBarButtonItem alloc] initWithTitle:@"运行" style:UIBarButtonItemStylePlain target:self action:@selector(run)];
    self.navigationItem.rightBarButtonItem = self.runBtn;
}


//-(void)onFinish{
//    self.runBtn.title=@"再次运行";
//    self.navigationItem.rightBarButtonItem = self.runBtn;
//}

-(void)run{
//    UIActivityIndicatorView *av = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
//    [av startAnimating];
//    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:av];
    SEL selector = NSSelectorFromString(self.methodModel.method);
    ((void (*)(id, SEL))[self.demo methodForSelector:selector])(self.demo, selector);
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


@end
