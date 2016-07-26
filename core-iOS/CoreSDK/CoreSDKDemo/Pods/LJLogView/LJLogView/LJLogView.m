//
//  LJLogView.m
//  VirgoPushDemo
//
//  Created by Jon on 16/2/17.
//  Copyright © 2016年 liaojun. All rights reserved.
//

#import "LJLogView.h"

@interface LJLogView ()<UITableViewDataSource,UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (strong, nonatomic)NSMutableArray *dataSource;
@end

@implementation LJLogView

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (id)initWithCoder:(NSCoder *)aDecoder {
    if (self = [super initWithCoder:aDecoder])
    {
        UIView *containerView = [[[UINib nibWithNibName:@"LJLogView" bundle:nil] instantiateWithOwner:self options:nil] objectAtIndex:0];
        CGRect newFrame = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height);
        containerView.frame = newFrame;
        [self addSubview:containerView];
        [self.tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"cell"];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(addLog:) name:LJLogNotification object:nil];
    }
    return self;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [self.dataSource count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell" forIndexPath:indexPath];
    cell.contentView.backgroundColor = [UIColor blackColor];
    cell.textLabel.textColor = [UIColor lightGrayColor];
    cell.textLabel.backgroundColor = [UIColor blackColor];
    cell.textLabel.font = [UIFont systemFontOfSize:12];
    cell.textLabel.numberOfLines = 0;
    
    cell.textLabel.text = self.dataSource[indexPath.row];
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *infoStr = self.dataSource[indexPath.row];
    
    return [self hightOfStr:infoStr font:[UIFont systemFontOfSize:12]]+10;
}

- (CGFloat)hightOfStr:(NSString *)str font:(UIFont *)font
{
    NSDictionary *attribute = @{NSFontAttributeName: font};
    CGSize retSize = [str boundingRectWithSize:CGSizeMake(self.frame.size.width - 16, 0)
                                             options:
                      NSStringDrawingTruncatesLastVisibleLine |
                      NSStringDrawingUsesLineFragmentOrigin |
                      NSStringDrawingUsesFontLeading
                                          attributes:attribute
                                             context:nil].size;
    
    return retSize.height;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *infoStr = self.dataSource[indexPath.row];
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    pasteboard.string = infoStr;
   
    
    
}

- (IBAction)clearLog:(id)sender
{
    [self.dataSource removeAllObjects];
    [self.tableView reloadData];
}

- (void)addLog:(NSNotification *)notification
{
    //因为通知来自不同的线程,如果不做处理的话,会有多个线程同时运行addLog方法,这样的话reload和scroll就会出现问题,因为当reload的时候dataSource可能已经变了,所以这里做了一下处理,让这些所有的操作都回到主线程执行
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString *infoStr = notification.userInfo[@"info"];
        [self.dataSource addObject:infoStr];
        [self.tableView reloadData];
         [self.tableView scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:self.dataSource.count - 1 inSection:0] atScrollPosition:UITableViewScrollPositionBottom animated:YES];
    });
   
}

- (NSMutableArray *)dataSource
{
    if (_dataSource == nil) {
        _dataSource = [[NSMutableArray alloc] init];
    }
    return _dataSource;
}


@end
