//
//  AppDelegate.m
//  CoreSDKDemo
//
//  Created by Jon on 16/7/12.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "AppDelegate.h"
#import <DroiCoreSDK/DroiCoreSDK.h>
#import "DroiObjectTest.h"
#import "DroiDemoMethodModel.h"
#import "DroiDemo.h"
#import "ListClassViewController.h"
#import "Student.h"
@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {

    [DroiCore initializeCore];
    [DroiObject registerCustomClass:[Student class]];
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    self.window.backgroundColor = [UIColor whiteColor];
    self.window.rootViewController = [self rootViewController];
    [self.window makeKeyAndVisible];
    return YES;
}

- (UIViewController *)rootViewController{
    
    ListClassViewController *listClassVC = [[ListClassViewController alloc] init];
    NSArray *array = @[@"DroiObjectTest",@"DroiUserTest",@"DroiFileTest",@"DroiQueryTest"];
    listClassVC.dataSource = array;
    UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:listClassVC];
    return nav;
}

- (void)applicationWillResignActive:(UIApplication *)application {
   }

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
