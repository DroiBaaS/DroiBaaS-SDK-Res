//
//  DroiFeedback.h
//  DroiFeedbackDemo
//
//  Created by Jon on 16/6/27.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@interface DroiFeedback : NSObject

+ (void)callFeedbackWithViewController:(UIViewController *)viewController;

+ (void)setUserId:(NSString *)userId;

+ (void)setColor:(UIColor *)color;

+ (NSString *)getCurrentUserId;
@end
