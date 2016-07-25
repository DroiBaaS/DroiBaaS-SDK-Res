//
//  DroiOAuth.h
//  DroiOAuth
//
//  Created by Jon on 16/4/7.
//  Copyright © 2016年 icewind. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

extern NSString *const kDroiOAuthLoginSuccess;

typedef void (^DroiOAuthResultBlock)(id object,NSError *error);

typedef NS_ENUM(NSUInteger, DroiOAuthLanguage) {
    DroiOAuthLanguageZH = 0,
    DroiOAuthLanguageEN
};

typedef NS_OPTIONS(NSUInteger, DroiOAuthScope) {
    DroiOAuthScopeUserInfo        = 1 << 0,
    DroiOAuthScopeUserPhoneNumber = 1 << 1,
    DroiOAuthScopeUserEmail       = 1 << 2
};
@interface DroiOAuth : NSObject

/**登录接口
 *  @param viewController 调用接口的viewController
 *  @param callback       回调block
 */
+ (void)loginWithViewController:(UIViewController *)viewController Callback:(DroiOAuthResultBlock)callback;


+ (void)getUserInfoWithToken:(NSDictionary *)token Scope:(DroiOAuthScope)scope Callback:(DroiOAuthResultBlock)callback;

+ (void)getUserInfoWithToken:(NSDictionary *)token Callback:(DroiOAuthResultBlock)callback;


+ (void)setLanguage:(DroiOAuthLanguage)language;


+ (void)setLogEnabled:(BOOL)enabled;

@end
