//
//  Student.h
//  CoreSDKDemo
//
//  Created by Jon on 16/7/21.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import <DroiCoreSDK/DroiCoreSDK.h>

@interface Student : DroiObject

DroiExpose
@property (copy, nonatomic)NSString *name;

DroiExpose
@property (assign, nonatomic)NSUInteger age;

DroiExpose
@property (strong, nonatomic)NSArray *hobbies;

DroiReference
@property (nonatomic , strong) DroiFile *avatar;


@end
