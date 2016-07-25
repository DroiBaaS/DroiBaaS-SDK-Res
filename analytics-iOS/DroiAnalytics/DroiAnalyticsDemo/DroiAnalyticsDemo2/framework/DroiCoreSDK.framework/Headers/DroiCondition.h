/*
 * Copyright (c) 2016-present Shanghai Droi Technology Co., Ltd.
 * All rights reserved.
 */

#import <Foundation/Foundation.h>

/**
 *  The DroiCondition constant for 'less than'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_LT;

/**
 *  The DroiCondition constant for 'less than and equal'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_LT_OR_EQ;

/**
 *  The DroiCondition constant for 'equal'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_EQ;

/**
 *  The DroiCondition constant for 'not equal'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_NEQ;

/**
 *  The DroiCondition constant for 'greater than and equal'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_GT_OR_EQ;

/**
 *  The DroiCondition constant for 'greater than'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_GT;

/**
 *  The DroiCondition constant for 'IS NULL'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_ISNULL;

/**
 *  The DroiCondition constant for 'IS NOT NULL'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_ISNOTNULL;

/**
 *  The DroiCondition constant for 'CONTAINS'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_CONTAINS;

/**
 *  The DroiCondition constant for 'NOT CONTAINS'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_NOTCONTAINS;

/**
 *  The DroiCondition constant for 'starts with'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_STARTSWITH;

/**
 *  The DroiCondition constant for 'not starts with'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_NOTSTARTSWITH;

/**
 *  The DroiCondition constant for 'ends with'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_ENDSWITH;

/**
 *  The DroiCondition constant for 'not ends with'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_NOTENDSWITH;

/**
 *  The DroiCondition constant for 'IN'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_IN;

/**
 *  The DroiCondition constant for 'NOT IN'
 */
FOUNDATION_EXPORT NSString* const DroiCondition_NOTIN;

/**
 *  Provide condition for DroiQuery. Developer cloud use this class to create customize query conditions.
 */
@interface DroiCondition : NSObject
#pragma mark - Chain methods

/**
 *  Combine two DroiCondition object by 'OR' operator
 *
 *  @param arg The DroiCondition object
 *
 *  @return The new DroiCondition object
 */
- (DroiCondition*) or : (DroiCondition*) arg;

/**
 *  Combine two DroiCondition object by 'AND' operator
 *
 *  @param arg The DroiCondition object
 *
 *  @return The new DroiCondition object
 */
- (DroiCondition*) and : (DroiCondition*) arg;

#pragma mark - Instance methods
/**
 *  Create DroiCondition object
 *
 *  @param arg1 The key of condition to check
 *  @param type The DroiCondition constant.
 *  @param arg2 The value is for checking
 *
 *  @return The DroiCondition object
 */
+ (instancetype) cond : (NSString*) arg1 andType : (NSString*) type andArg2 : (id) arg2;

/**
 *  Create DroiCondition object for SELECT IN operator
 *
 *  @param arg1  The key of condition to check
 *  @param items The values that will match
 *
 *  @return The DroiCondition object
 */
+ (instancetype) selectIn : (NSString*) arg1 withItems : (NSArray*) items;


/**
 *  Create DroiCondition object for NOT SELECT IN operator
 *
 *  @param arg1  The key of condition to check
 *  @param items The values that will match
 *
 *  @return The DroiCondition object
 */
+ (instancetype) notSelectIn : (NSString*) arg1 withItems : (NSArray*) items;
@end
