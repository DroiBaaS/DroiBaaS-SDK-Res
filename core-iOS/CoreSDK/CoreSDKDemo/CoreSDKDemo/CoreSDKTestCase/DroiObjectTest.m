//
//  DroiObjectTest.m
//  CoreSDKDemo
//
//  Created by Jon on 16/7/21.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "DroiObjectTest.h"
#import <DroiCoreSDK/DroiCoreSDK.h>
#import "Student.h"
#import "DroiLog.h"
typedef void (^StudentBlock) (Student *student);

typedef void (^DroiCallBackBlock) (id object);

@implementation DroiObjectTest

#pragma mark - 简单的增删改查

- (void)demoCreateObject {
    Student *student = [[Student alloc] init];
    student.name = @"Jon";
    student.age = 18;
    student.hobbies = @[@"football",@"swim"];
    [student saveInBackground:^(BOOL result, DroiError *error) {
        if (result) {
            DroiLog(@"创建成功!");
        }
        else{
            DroiLog(@"创建失败%@",error.message);
        }
    }];
    
}

- (void)demoUpdateObject {
    //我们先创建一个Object 才可以更新
    Student *student = [[Student alloc] init];
    student.name = @"Allen";
    
    //Object也可以用同步方法保存
    DroiError *error = [student save];
    
    if (error) {
        DroiLog(@"创建student成功 ,name:%@",student.name);

    }
    else{
        DroiLog(@"创建student失败:%@",error.message);

    }
    //更新操作
    student.name = @"Jack";
    [student saveInBackground:^(BOOL result, DroiError *error) {
        if (result) {
            DroiLog(@"更新student成功! name:%@",student.name);
        }
        else{
            DroiLog(@"更新失败%@",error.message);
        }
    }];
}

- (void)demoDeleteObject {
    
    Student *student = [[Student alloc] init];
    student.name = @"Mike";
    [student save];
    //开始删除操作
    [student deleteInBackground:^(BOOL result, DroiError *error) {
        if (result) {
            DroiLog(@"删除student成功!");
        }
        else{
            DroiLog(@"删除student失败%@",error.message);
        }
    }];
}

- (void)demoGetObject {
    
    DroiQuery *query = [[DroiQuery create] queryByName:@"Student"];
    query = [query whereStatement:@"name" opType:DroiCondition_EQ arg2:@"Jon"];
    
    [query runQueryInBackground:^(NSArray *result, DroiError *err) {
        if (err.isOk) {
            DroiLog(@"查询student成功!%@",result);
        }
        else{
            DroiLog(@"查询student失败%@",err.message);
        }
    }];
}

#pragma mark - 复杂一点的数据操作

- (void)demoCreateObjectAndFile {
    Student *student = [[Student alloc] init];
    student.name = @"Peipei";
    NSString *filePath = [[NSBundle mainBundle] pathForResource:@"avatar.png" ofType:nil];
    DroiFile *avatar = [DroiFile fileWithFileName:filePath];
    student.avatar = avatar;
    [student saveInBackground:^(BOOL result, DroiError *error) {
        if (result) {
            DroiLog(@"创建文件和对象成功!%@",student);
        }
        else{
            DroiLog(@"创建失败%@",error.message);
        }
    }];
}

- (void)demoGetAllKeys {
    
    [self createStudentForDemo:^(Student *student) {
        DroiLog(@"对象的所有key为：%@", [student getKeys]);
    }];
}



#pragma mark - 批量操作

- (void)createStudentsForDemo:(DroiCallBackBlock)block {
    NSMutableArray *students = [NSMutableArray array];
    for (int i = 10; i < 20; i++) {
        Student *student = [[Student alloc] init];
        student.age = i;
        student.name = [NSString stringWithFormat:@"name%d",i];
        [students addObject:student];
    }
    
    [DroiObject saveAllInBackground:students andCallback:^(BOOL result, DroiError *error) {
        if (result) {
            block(students);
        }
    }];
}

- (void)demoBatchCreate {
    
    NSMutableArray *students = [NSMutableArray array];
    for (int i = 10; i < 20; i++) {
        Student *student = [Student new];
        student.age = i;
        student.name = [NSString stringWithFormat:@"name%d",i];
        [students addObject:student];
    }
    [DroiObject saveAllInBackground:students andCallback:^(BOOL result, DroiError *error) {
        if (result) {
            DroiLog(@"批量创建了10个学生！他们是：%@", students);
        }
        else{
            DroiLog(@"批量创建失败%@",error.message);
        }
    }];
}

- (void)demoBatchFetch {
    // 先保存10个学生，示例用
    [self createStudentsForDemo:^(id object) {
        
            NSArray *studens = object;
            NSMutableArray *fetchStudentIds = [NSMutableArray array];
            for (Student *student in studens) {
                //保存ObjectId
                [fetchStudentIds addObject:student.objectId];
            }

        DroiQuery *query = [[DroiQuery create] queryByName:@"Student"];
        query = [query whereStatement:[DroiCondition selectIn:@"_Id" withItems:fetchStudentIds]];
        [query runQueryInBackground:^(NSArray *result, DroiError *err) {
            if (err.isOk) {
                DroiLog(@"批量获取了学生数据; %@",result);
            }
            else{
                DroiLog(@"批量获取失败: %@",err.message);
            }
        }];
    }];
}
     
- (void)demoBatchUpdate {
    
    // 先保存10个学生，示例用
    [self createStudentsForDemo:^(id object) {
        
        NSArray *studens = object;
            for (Student *student in studens) {
                // 构造对象，无数据
                student.name = @"Mike";
            }
        [DroiObject saveAllInBackground:studens andCallback:^(BOOL result, DroiError *error) {
            if (result) {
                DroiLog(@"批量更新了学生数据 :%@",studens);
            }
            else{
                DroiLog(@"批量更新失败 :%@",error.message);
            }
        }];
    }];
}

- (void)demoBatchDelete {
    
    [self createStudentsForDemo:^(id object) {
    NSArray *studens = object;
    [DroiObject deleteAllInBackground:studens andCallback:^(BOOL result, DroiError *error) {
        if (result) {
            DroiLog(@"批量删除了学生数据 :%@",studens);
        }
        else{
            DroiLog(@"批量删除失败 :%@",error.message);
        }
        }];
    }];
}

- (void)createStudentForDemo:(StudentBlock)block {
    Student *student = [[Student alloc] init];
    student.name = @"Skyer";
    student.age = 18;
    student.hobbies = @[ @"swimming", @"running" ];
    [student saveInBackground:^(BOOL result, DroiError *error) {
        if (result) {
            block(student);
        }
    }];
}

@end
