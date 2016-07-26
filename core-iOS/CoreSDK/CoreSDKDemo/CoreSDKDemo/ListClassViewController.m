//
//  ListClassViewController.m
//  CoreSDKDemo
//
//  Created by Jon on 16/7/22.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "ListClassViewController.h"
#import "DroiDemo.h"
#import "ListViewController.h"
@interface ListClassViewController ()<UITableViewDataSource, UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@end

@implementation ListClassViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    [self.tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"Cell"];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return self.dataSource.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"Cell" forIndexPath:indexPath];
    NSString *className = self.dataSource[indexPath.row];
    cell.textLabel.text = className;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
   NSString *className = self.dataSource[indexPath.row];
     DroiDemo *demo= [[NSClassFromString(className) alloc] init];
    ListViewController *listVC = [[ListViewController alloc] init];
    listVC.demo = demo;
    [self.navigationController pushViewController:listVC animated:YES];
}
@end
