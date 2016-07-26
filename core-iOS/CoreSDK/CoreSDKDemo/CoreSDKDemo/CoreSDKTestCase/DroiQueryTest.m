//
//  DroiQueryTest.m
//  CoreSDKDemo
//
//  Created by Jon on 16/7/22.
//  Copyright © 2016年 Droi. All rights reserved.
//
/*
 1. [query runQueryInBackground:^(NSArray *result, DroiError *err){}];err 改成error

 */
#import "DroiQueryTest.h"
#import "Student.h"
#import "DroiLog.h"
@implementation DroiQueryTest


- (void)demoByClassNameQuery {
    
    DroiQuery *query = [[DroiQuery create] queryByClass:[Student class]];
    //设置查询返回数
    query = [query limit:2];
    [query runQueryInBackground:^(NSArray *result, DroiError *err) {
        
        if (err.isOk) {
            DroiLog(@"查询结果:%@",result);
        }
        else{
            DroiLog(@"查询失败:%@",err.message);
        }
    }];
}

- (void)demoAndQuery_ {
    
    //名字不为Jon
    DroiCondition *cond1 = [DroiCondition neq:@"name" andArg2:@"Jon"];
    //名字以J开头
    DroiCondition *cond2 = [DroiCondition startsWith:@"name" andArg2:@"J"];
    DroiQuery *query = [[DroiQuery create] queryByClass:[Student class]];
    
    //名字不为Jon 且 名字以J开头
    query = [query whereStatement:[cond1 and:cond2]];
    [query runQueryInBackground:^(NSArray *result, DroiError *err) {
        if (err.isOk) {
            DroiLog(@"查询结果:%@",result);
        }
        else{
            DroiLog(@"查询失败:%@",err.message);
      }
    }];

}

- (void)demoOrQuery_ {
    
    //名字以M开头
    DroiCondition *cond1 = [DroiCondition startsWith:@"name" andArg2:@"M"];
    //名字以J开头
    DroiCondition *cond2 = [DroiCondition startsWith:@"name" andArg2:@"J"];
    DroiQuery *query = [[DroiQuery create] queryByClass:[Student class]];
    
    //名字以M开头 且 名字以J开头
    query = [query whereStatement:[cond1 or:cond2]];
    [query runQueryInBackground:^(NSArray *result, DroiError *err) {
        if (err.isOk) {
            DroiLog(@"查询结果:%@",result);
        }
        else{
            DroiLog(@"查询失败:%@",err.message);
        }
    }];
}

- (void)demoByKeyOrder {
    
    DroiQuery *query = [[DroiQuery create] queryByClass:[Student class]];
    //添加排序
    query = [query orderBy:@"name" ascending:YES];
    
    [query runQueryInBackground:^(NSArray *result, DroiError *err) {
        if (err.isOk) {
            DroiLog(@"查询结果 以名字升序排序:%@",result);
        }
        else{
            DroiLog(@"查询失败:%@",err.message);
        }
    }];
}

- (void)demoContainedIn {
    
    DroiQuery *query = [[DroiQuery create] queryByClass:[Student class]];
    DroiCondition *cond = [DroiCondition selectIn:@"name" withItems:@[@"Jon",@"Skyer"]];
    
    query = [query whereStatement:cond];
    
    [query runQueryInBackground:^(NSArray *result, DroiError *err) {
        
        if (err.isOk) {
            DroiLog(@"查询结果 %@",result);
        }
        else{
            DroiLog(@"查询失败:%@",err.message);
        }
    }];
    
}
@end
