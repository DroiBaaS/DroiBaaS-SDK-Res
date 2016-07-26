//
//  ListViewController.m
//  CoreSDKDemo
//
//  Created by Jon on 16/7/22.
//  Copyright © 2016年 Droi. All rights reserved.
//

#import "ListViewController.h"
#import "DroiDemoMethodModel.h"
#import "DroiObjectTest.h"
#import "DemoRunVC.h"
@interface ListViewController ()<UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic , strong) NSArray *dataSource;

@end

@implementation ListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.title = [[self.demo class] description];
    self.dataSource = [self.demo allDemoMethod];
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
    DroiDemoMethodModel *model = self.dataSource[indexPath.row];
    cell.detailTextLabel.text = model.detail;
    cell.textLabel.text = model.name;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    DroiDemoMethodModel *methodModel = self.dataSource[indexPath.row];
    
    DemoRunVC *demoRunVC = [[DemoRunVC alloc] init];
    demoRunVC.methodModel = methodModel;
    demoRunVC.demo = self.demo;
    [self.navigationController pushViewController:demoRunVC animated:YES];
}
@end
