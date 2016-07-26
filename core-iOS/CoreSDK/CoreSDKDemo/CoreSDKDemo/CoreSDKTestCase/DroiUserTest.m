//
//  DroiUserTest.m
//  CoreSDKDemo
//
//  Created by Jon on 16/7/21.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "DroiUserTest.h"
#import <DroiCoreSDK/DroiCoreSDK.h>
#import "DroiLog.h"

static NSString * const kDemoUsername = @"XiaoMing";
static NSString * const kDemoPassword = @"123456";
@implementation DroiUserTest


- (void)logUnLogin {
    
    DroiUser *user = [DroiUser getCurrentUser];
    if (user.isAnonymous) {
        DroiLog(@"当前为匿名用户登录，用户ID为:%@  请先运行登录示例",user.UserId);
    }
    else{
        DroiLog(@"当前无登录用户,请先运行登录示例");
    }
}

- (void)demoCurrentUser {
    
    DroiUser *user = [DroiUser getCurrentUser];
    if (!user.isAnonymous && user.isAuthorized) {
        DroiLog(@"当前用户 %@ , 用户名 %@", user, user.UserId);
    } else {
        [self logUnLogin];
    }
}

- (void)demoLogoutUser {
    DroiUser *user = [DroiUser getCurrentUser];
    if (!user.isAnonymous) {
    DroiError *error = [user logout];
        if (error.isOk) {
            DroiLog(@"用户登出:%@",user.UserId);
        }
        else{
            DroiLog(@"用户登出失败:%@",error.message);
        }
    }
    else {
        [self logUnLogin];
    }
}

- (void)demoDeleteCurrentUser {
    DroiUser *user = [DroiUser getCurrentUser];
    if (!user.isAnonymous) {
        [user deleteInBackground:^(BOOL result, DroiError *error) {
            if (result) {
                DroiLog(@"删除用户成功%@",user.UserId);
            }
            else{
                DroiLog(@"删除用户失败%@",error.message);
            }
        }];
        }
        else {
            [self logUnLogin];
    }
}

- (void)demoUsernameRegister {
    DroiUser *user = [DroiUser getCurrentUser];
    user.UserId = kDemoUsername;
    user.password = kDemoPassword;
    [user signUpInBackground:^(BOOL result, DroiError *error) {
        if (result) {
            DroiLog(@"用户注册成功 %@", user);
        }
        else{
            DroiLog(@"用户注册失败 %@", error.message);
        }
    }];
}

- (void)demoUsernameLogin {
    
    DroiError *error = nil;
    DroiUser *user = [DroiUser login:kDemoUsername password:kDemoPassword error:&error];
    if (error.isOk) {
        DroiLog(@"登录成功 %@", user);
    }
    else{
        DroiLog(@"登录失败 %@", error.message);
    }
}

- (void)demoOldPasswordUpdatePassword {
    DroiUser *user = [DroiUser getCurrentUser];
    if (!user.isAnonymous) {
        NSString *newPassword = @"111111";
    [user saveInBackground:^(BOOL result, DroiError *error) {
        
        if (result) {
            DroiLog(@"重置密码成功，新的密码为 %@",newPassword);
        }
        else{
            DroiLog(@"重置密码失败 %@",error.message);
        }
    }];
    }
    else {
        [self logUnLogin];
    }
}



@end
