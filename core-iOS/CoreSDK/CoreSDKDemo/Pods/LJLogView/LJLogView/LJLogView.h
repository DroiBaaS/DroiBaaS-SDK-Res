//
//  LJLogView.h
//  VirgoPushDemo
//
//  Created by Jon on 16/2/17.
//  Copyright © 2016年 liaojun. All rights reserved.
//

#import <UIKit/UIKit.h>

#define LJLog(...) [[NSNotificationCenter defaultCenter] postNotificationName:LJLogNotification object:nil userInfo:[NSDictionary dictionaryWithObject:[NSString stringWithFormat:__VA_ARGS__] forKey:@"info"]]

static NSString *LJLogNotification = @"LJLogNotification";

@interface LJLogView : UIView

@end
