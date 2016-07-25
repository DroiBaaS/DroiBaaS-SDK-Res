//
//  DroiPushViewController.m
//  DroiPushDemo
//
//  Created by Jon on 16/7/13.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "DroiPushViewController.h"
#import <DroiPush/DroiPush.h>
@interface DroiPushViewController ()
@property (weak, nonatomic) IBOutlet UILabel *appIdLabel;
@property (weak, nonatomic) IBOutlet UILabel *channelLabel;
@property (weak, nonatomic) IBOutlet UILabel *cidLabel;
@property (weak, nonatomic) IBOutlet UILabel *secretLabel;
@property (weak, nonatomic) IBOutlet UITextField *tag1TF;
@property (weak, nonatomic) IBOutlet UITextField *tag2TF;
@property (weak, nonatomic) IBOutlet UITextField *badgeTF;
@property (weak, nonatomic) IBOutlet UILabel *infoLabel;

@end

@implementation DroiPushViewController

- (void)viewDidLoad {
    
    [super viewDidLoad];
    
    self.appIdLabel.text = [DroiPush getAppId];
    self.channelLabel.text = [DroiPush getAppChannel];
    self.secretLabel.text = [DroiPush getAppSecret];
    self.cidLabel.text = [DroiPush getClientId];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (IBAction)setTag:(id)sender {
    
    NSString *tag1 = self.tag1TF.text;
    NSString *tag2 = self.tag2TF.text;
    
    //设置标签
    NSSet *set = [NSSet setWithObjects:tag1,tag2, nil];
    [DroiPush setTags:set];
}


- (IBAction)clearTag:(id)sender {
    
    //清空标签
    [DroiPush resetTags];
    
}
- (IBAction)setBadge:(id)sender {
    
    NSInteger badge = [self.badgeTF.text integerValue];
    
    //设置badge
    [DroiPush setBadge:badge];
    
}

- (void)LogString:(NSString *)string{
    
    self.infoLabel.text = string;
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    
    [self.view endEditing:YES];
}

@end
