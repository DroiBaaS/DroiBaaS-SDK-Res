/*
 * Copyright (c) 2016-present Shanghai Droi Technology Co., Ltd.
 * All rights reserved.
 */

#import "DroiObject.h"
#import "DroiError.h"

/**
 *  Callback method for background task
 *
 *  @param result The callback result. This data type should be DroiObject extension
 *  @param error Error details.
 */
typedef void(^DroiCloudCallback)(id result, DroiError* error);

/**
 *  The DroiCloud class is used to call Droi Cloud Service. Developer would use this class to call specific cloud service.
 */
@interface DroiCloud : NSObject
/**
 *  Calls a cloud service by specific name with parameters
 *
 *  @param name      Service name
 *  @param parameter Parameter object. Must be inherited from `DroiObject`
 *  @param clazz     Output object class type. Must be inherited from `DroiObject`
 *  @param error     Pass DroiError to retrieve error details, or pass nil to ignore.
 *
 *  @return Task name. 
 */
+ (id) callCloudService : (NSString*) name parameter : (DroiObject*) parameter  andClassType : (Class) clazz error:(DroiError**) error;

/**
 *  Calls a cloud service by specific name with parameters in background thread
 *
 *  @param name      Service name
 *  @param parameter Parameter object. Must be inherited from `DroiObject`
 *  @param callback  The procedure block for callback
 *  @param clazz     Output object class type. Must be inherited from `DroiObject`
 *
 *  @return The taskId of background task
 */
+ (NSString*) callCloudServiceInBackground : (NSString*) name parameter : (DroiObject*) parameter andCallback : (DroiCloudCallback) callback withClassType : (Class) clazz;
@end
