/*
 * Copyright (c) 2016-present Shanghai Droi Technology Co., Ltd.
 * All rights reserved.
 */


#import <Foundation/Foundation.h>

#ifndef DroiExpose
#define DroiExpose
#endif

#ifndef DroiReference
#define DroiReference
#endif

#ifndef DroiObjectName
#define DroiObjectName(className)
#endif

@class DroiError;
@class DroiPermission;

/**
 *  Callback method for background task
 *
 *  @param result YES if succeeded
 */
typedef void(^DroiObjectCallback)(BOOL result);

/**
 * The DroiObject is the basic data type that can be saved and retrieved from the droi cloud service.
 */
@interface DroiObject : NSObject

#pragma mark - Storage
/**
 *  Determine whether this DroiObject is from/to localStorage. Default value is NO (Cloud)
 */
@property BOOL localStorage;

/**
 *  Save this object to storage.
 *
 *  @return YES if saving successfully or NO otherwise
 */
- (BOOL) save;

/**
 *  Delete this object from storage.
 *
 *  @return YES if deleting successfully or NO otherwise.
 */
- (BOOL) delete;

/**
 *  Save this object to storage by a background thread.
 *
 *  @param callback The callback object `DroiObjectCallback` is used to receive save result.
 *
 *  @return taskId for calling the task.
 */
- (NSString*) saveInBackground : (DroiObjectCallback) callback;

/**
 *  Delete this object from storage by a background thread.
 *
 *  @param callback The callback object `DroiObjectCallback` is used to receive save result.
 *
 *  @return taskId for calling the task.
 */
- (NSString*) deleteInBackground : (DroiObjectCallback) callback;

/**
 *  Save this object eventually. This feature is only for cloud storage and would be sent to cloud if there is data connection available
 */
- (void) saveEventually;

/**
 *  Help to save a list of DroiObject to storage.
 *
 *  @param items A collection of DroiObject.
 *
 *  @return YES if saving successfully or NO otherwise
 */
+ (BOOL) saveAll : (NSArray*) items;

/**
 *  Help to save a list of DroiObject to storage by using a background thread.
 *
 *  @param items    A collection of DroiObject.
 *  @param callback The callback object `DroiObjectCallback` is used to receive save result.
 *
 *  @return taskId for calling the task.
 */
+ (NSString*) saveAllInBackground : (NSArray*) items andCallback : (DroiObjectCallback) callback;

/**
 *  Help to save a list of DroiObject to storage.
 *
 *  @param items A collection of DroiObject
 *
 *  @return YES if saving successfully or NO otherwise
 */
+ (BOOL) deleteAll : (NSArray*) items;

/**
 *  Help to save a list of DroiObject to storage by using a background thread.
 *
 *  @param items    A collection of DroiObject.
 *  @param callback The callback object `DroiObjectCallback` is used to receive save result.
 *
 *  @return taskId for calling the task.
 */
+ (NSString*) deleteAllInBackground : (NSArray*) items andCallback : (DroiObjectCallback) callback;

/**
 *  Try to cancel the task running for saveInBackground, deleteInBackground,
 *
 *  @param taskId An id which was scheduled for work.
 */
+ (void) cancelBackgroundTask : (NSString*) taskId;

#pragma mark - Permission
/**
 *  The data permission (DroiPermission) for DroiObject
 */
@property DroiPermission* permission;

#pragma mark - Dirty Flags
/**
 *  Check whether this object is changed
 *
 *  @return YES if this object is changed
 */
- (BOOL) isDirty;

/**
 *  Check whether this object data is changed
 *
 *  @return YES if this object is changed
 */
- (BOOL) isBodyDirty;

/**
 *  Check whether there is any referenced object is changed
 *
 *  @return YES if there is referenced object is changed
 */
- (BOOL) isReferencedObjectDirty;

#pragma mark - Dictionary operations
/**
 *  Add a key-value pair to DroiObject
 *
 *  @param key   The key.
 *  @param value Value may be numerical, @ref NSString, @ref DroiObject,
 *              @ref NSNumber or other @ref DroiObject class.
 *              Value may not be nil
 */
- (void) putKey:(NSString*) key andValue:(id) value;

/**
 *  Get all key name in key-value list
 *
 *  @return The key name of array list if there is key-value item.
 */
- (NSArray*) getKeys;

/**
 *  Get a value by specific key. In most cases it is more convenient to use a helper function such as `getString` or `getInt`
 *
 *  @param key The key
 *
 *  @return nil if there is no such key
 */
- (id) getValue:(NSString*) key;

/**
 *  Get a @ref NSString by key
 *
 *  @param key The key
 *
 *  @return return string if there is value with specific key; Otherwise is nil
 */
- (NSString*) getString:(NSString*) key;

/**
 *  Get a @ref NSData by key
 *
 *  @param key The key
 *
 *  @return return NSData if there is value with specific key
 */
- (NSData*) getData:(NSString*) key;

/**
 *  Get a @ref NSNumber by key
 *
 *  @param key The key
 *
 *  @return return NSNumber object
 */
- (NSNumber*) getNumber:(NSString*) key;

/**
 *  Get a @ref DroiObject by key
 *
 *  @param key The key
 *
 *  @return return DroiObject object
 */
- (DroiObject*) getDroiObject:(NSString*) key;

/**
 *  Get a boolean value by key
 *
 *  @param key The key
 *
 *  @return return boolean value
 */
- (BOOL) getBoolean:(NSString*) key;

/**
 *  Get integer value by key
 *
 *  @param key The key
 *
 *  @return return integer value
 */
- (int) getInt:(NSString*) key;

/**
 *  Get long integer value by key
 *
 *  @param key The key
 *
 *  @return return long integer value
 */
- (long) getLong:(NSString*) key;

/**
 *  Get double value by key
 *
 *  @param key The key
 *
 *  @return return double value
 */
- (double) getDouble:(NSString*) key;

#pragma Serialization/Deserialization
/**
 *  Serialize object to json string
 *
 *  @return return NSString json string
 */
- (NSString*) toJson;

/**
 *  Deserialized DroiObject from Json data
 *
 *  @param jsonString The Json data being deserialized
 *
 *  @return The deserialized DroiObject object
 */
+ (id) fromJson:(NSString*) jsonString;

/**
 *  Deserialized DroiObject from Json data
 *
 *  @param json The Json data being deserialized
 *
 *  @return The deserialized DroiObject object
 */
+ (id) fromJsonWithDictionary:(NSDictionary*) json;

/**
 *  Deserialize object of the specified type clazz from Json data which is a subclass of DroiObject
 *
 *  @param jsonString The Json data being deserialized
 *  @param clazz      The type of the Object to deserialize to
 *
 *  @return A deserialized object of the specified type clazz
 */
+ (id) fromJson:(NSString*) jsonString andClass:(Class) clazz;

/**
 *  Deserialize object of the specified type clazz from Json data (NSDictionary) which is a subclass of DroiObject
 *
 *  @param json  The Json data being deserialized
 *  @param clazz The type of the Object to deserialize to
 *
 *  @return A deserialized object of the specified type clazz
 */
+ (id) fromJsonWithDictionary:(NSDictionary*) json andClass:(Class) clazz;

#pragma mark - Creation
/**
 *  Create a new {@code DroiObject} with specified class name.
 *
 *  @param className the class name of created object.
 *
 *  @return DroiObject instance
 */
+ (DroiObject*) createWithClassName:(NSString*) className;

/**
 *  Creates a new object based upon a subclass type.
 *
 *  @param clazz The class of object to create.
 *
 *  @return A specific object instance
 */
+ (id) createWithClass:(Class) clazz;

/**
 *  Get custom class by className
 *
 *  @param className The name of custom class
 *
 *  @return The Class of custom class
 */
+ (Class) getCustomClass:(NSString*) className;

/**
 *  Check whether this class is DroiObject extension class
 *
 *  @param clazz The class object
 *
 *  @return YES if the class is DroiObject extension class; Otherwise is NO
 */
+ (BOOL) isDroiObjectExtensionByClass : (Class) clazz;

/**
 *  Check whether this class name is DroiObject extension class
 *
 *  @param className The class name
 *
 *  @return YES if the class is DroiObject extension class; Otherwise is NO
 */
+ (BOOL) isDroiObjectExtensionByClassName : (NSString*) className;

#pragma mark - Property
/**
 *  The unique Id of this DroiObject
 */
@property (readonly) NSString* objectId;

/**
 *  The creation time of this DroiObject. The timezone of this time is UTC+0
 */
@property (readonly) NSDate* creationTime;

/**
 *  The modified time of this DroiObject. The timezone of this time is UTC+0
 */
@property (readonly) NSDate* modifiedTime;

/**
 *  The name of class
 */
@property (readonly) NSString* className;
@end
