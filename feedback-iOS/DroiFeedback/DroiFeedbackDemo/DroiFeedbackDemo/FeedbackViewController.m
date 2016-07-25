//
//  FeedbackViewController.m
//  DroiFeedbackDemo
//
//  Created by Jon on 16/6/30.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "FeedbackViewController.h"
#import <DroiCoreSDK/DroiCoreSDK.h>
#import "DroiFeedback.h"
@interface FeedbackViewController ()

@property (weak, nonatomic) IBOutlet UILabel *appId;

@property (weak, nonatomic) IBOutlet UILabel *currentUserId;

@end

@implementation FeedbackViewController


- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.appId.text = [DroiCore getDroiAppId];
    self.currentUserId.text = [DroiFeedback getCurrentUserId];
}

- (void)viewDidLoad {
    [super viewDidLoad];

    

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)setUserId:(id)sender {
    
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"提示" message:@"请输入UserId" preferredStyle:UIAlertControllerStyleAlert];
   
    
    UIAlertAction *action = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        
        NSString *userId = [alert.textFields[0] text];
        [DroiFeedback setUserId:userId];
        self.currentUserId.text = [DroiFeedback getCurrentUserId];
    }];
  
    
    [alert addTextFieldWithConfigurationHandler:nil];
    [alert addAction:action];
    [self presentViewController:alert animated:YES completion:nil];
    
}

- (IBAction)setColor:(id)sender {
    
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"提示" message:@"选择色调" preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *redAction = [UIAlertAction actionWithTitle:@"Red" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [DroiFeedback setColor:[UIColor redColor]];
    }];
    
    UIAlertAction *yellowAction = [UIAlertAction actionWithTitle:@"Orange" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [DroiFeedback setColor:[UIColor orangeColor]];
    }];
    UIAlertAction *greenAction = [UIAlertAction actionWithTitle:@"Green" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [DroiFeedback setColor:[UIColor greenColor]];
    }];
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:nil];
    [alert addAction:redAction];
    [alert addAction:yellowAction];
    [alert addAction:greenAction];
    [alert addAction:cancelAction];
    [self presentViewController:alert animated:YES completion:nil];

}

- (IBAction)callFeedback:(UIButton *)sender {
    
    [DroiFeedback callFeedbackWithViewController:self];
}


@end
