/*
 * Copyright (c) 2016-present Shanghai Droi Technology Co., Ltd.
 * All rights reserved.
 */
#import <Foundation/Foundation.h>
#import "DroiError.h"

@interface DroiPreference : NSObject
@property (readonly) BOOL isRefreshing;
@property (readonly) BOOL isReady;

+ (instancetype) instance;
- (void) refresh;
- (void) refreshWithCallback:(void(^)(BOOL, DroiError*)) callback;

- (id) valueForKey:(NSString*) key;
@end
