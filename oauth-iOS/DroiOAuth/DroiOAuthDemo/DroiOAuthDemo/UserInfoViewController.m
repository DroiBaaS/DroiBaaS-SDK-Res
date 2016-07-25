//
//  UserInfoViewController.m
//  DroiAccountDemo
//
//  Created by Jon on 16/6/16.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "UserInfoViewController.h"
#import "DroiOAuth.h"
@interface UserInfoViewController ()
@property (weak, nonatomic) IBOutlet UILabel *userInfo;


@property (weak, nonatomic) IBOutlet UISwitch *phoneNumberSwitch;
@property (weak, nonatomic) IBOutlet UISwitch *emailSwitch;
@property (weak, nonatomic) IBOutlet UISwitch *userInfoSwitch;
@end

@implementation UserInfoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)SegChange:(UISegmentedControl *)sender {
    
    NSInteger selectedSegmentIndex = sender.selectedSegmentIndex;
    if(selectedSegmentIndex == 0){
        [DroiOAuth setLanguage:DroiOAuthLanguageZH];
    }
    else{
        [DroiOAuth setLanguage:DroiOAuthLanguageEN];
    }
    
}
- (IBAction)login:(id)sender {
    
    [DroiOAuth loginWithViewController:self Callback:^(id object, NSError *error) {
        
        NSLog(@"%@",object);
        NSLog(@"_++++++++++++++++++++++++++++%@",[NSThread currentThread]);
        [DroiOAuth getUserInfoWithToken:object Scope:[self getScope] Callback:^(id object, NSError *error) {
            
            NSLog(@"%@",object);
            NSLog(@"------------------------%@`",[NSThread currentThread]);
            self.userInfo.text = [object description];
        }];
    }];
}

- (DroiOAuthScope )getScope{
    DroiOAuthScope scope;
    if (self.userInfoSwitch.on) {
        scope = scope|DroiOAuthScopeUserInfo;
    }
    if (self.phoneNumberSwitch.on) {
        scope = scope|DroiOAuthScopeUserPhoneNumber;
    }
    if (self.emailSwitch.on) {
        scope = scope|DroiOAuthScopeUserEmail;
    }
    return scope;
}


@end
