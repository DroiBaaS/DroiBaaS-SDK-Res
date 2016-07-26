/*
 * Copyright (c) 2016-present Shanghai Droi Technology Co., Ltd.
 * All rights reserved.
 */

#import "DroiObject.h"

DroiObjectName(@"_Group_User_Relation")
@interface DroiGroupRelation : DroiObject
DroiExpose
@property NSString* MemberUserObjectId;

DroiExpose
@property NSString* MemberGroupObjectId;

DroiExpose
@property NSString* GroupObjectId;
@end
