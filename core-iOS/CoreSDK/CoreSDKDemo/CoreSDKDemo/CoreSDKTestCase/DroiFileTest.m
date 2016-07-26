//
//  DroiFileTest.m
//  CoreSDKDemo
//
//  Created by Jon on 16/7/22.
//  Copyright © 2016年 Droi. All rights reserved.
//
/*
 1. fileWithFileName 接口有歧义 改为 fileWithFilePath
 2. file 没有 name 的 getter 方法
 3. file getUrl 每次都需要网络请求 能否file查询下来的时候把Url也附带上 再添加一个接口用来手动更新Url
 */


#import "DroiFileTest.h"
#import "DroiLog.h"
@implementation DroiFileTest

- (void)demoCreateFile {
    //获取要保存的数据
    NSData *data = [NSData dataWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"avatar" ofType:@"png"]];
    //用数据创建文件
    DroiFile *file = [DroiFile fileWithData:data name:@"头像"];
    
    //保存文件
    [file saveInBackground:^(BOOL result, DroiError *error) {
        if (result) {
            DroiLog(@"文件上传成功![%@] %@",file.objectId,[file getUrl]);
        }
        else{
            DroiLog(@"文件上传失败! %@",error.message);
        }
    } progressCallback:^(long current, long max) {
        DroiLog(@"文件上传中 %f",current * 1.0 / max );
    }];
}

- (void)demoFromPathCreateFile {
    //从本地文件路径创建文件
    NSString *filePath = [[NSBundle mainBundle] pathForResource:@"avatar" ofType:@"png"];
    //用数据创建文件
    DroiFile *file = [DroiFile fileWithFileName:filePath];
    //保存文件
    [file saveInBackground:^(BOOL result, DroiError *error) {
        if (result) {
            DroiLog(@"文件上传成功![%@] %@",file.objectId,[file getUrl]);
        }
        else{
            DroiLog(@"文件上传失败! %@",error.message);
        }
    } progressCallback:^(long current, long max) {
        DroiLog(@"文件上传中 %f",current * 1.0 / max );
    }];

}

- (void)demoDeleteFile {
    
    //需要先得到一个File, 可以是从Cloud数据中返回的, 这里直接创建了一个文件 然后删除它
    NSString *filePath = [[NSBundle mainBundle] pathForResource:@"avatar" ofType:@"png"];
    //用数据创建文件
    DroiFile *file = [DroiFile fileWithFileName:filePath];
    [file save];
    //删除文件
    [file deleteInBackground:^(BOOL result, DroiError *error) {
        if (result) {
            DroiLog(@"文件[%@] 已经删除", file.objectId);
        } else {
            DroiLog(@"文件删除失败 %@", error.message);
        }
    }];
}


@end
