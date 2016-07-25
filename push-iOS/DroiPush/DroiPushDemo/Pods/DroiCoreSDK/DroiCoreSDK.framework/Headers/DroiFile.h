/*
 * Copyright (c) 2016-present Shanghai Droi Technology Co., Ltd.
 * All rights reserved.
 */

#import <Foundation/Foundation.h>
#import "DroiObject.h"
#import "DroiError.h"

#define MAX_AVAILABLE_FILE_SIZE 14680064

/**
 *  Callback definition for `getInBackground:` or `getInBackground:progressCallback:`
 *
 *  @param data Get file result
 *  @param error Error details.
 */
typedef void(^DroiGetFileCallback)(NSData* data, DroiError* error);

/**
 *  Progressive callback for `getInBackground:progressCallback:`
 *
 *  @param current current uploaded size
 *  @param max     file size
 */
typedef void(^DroiFileProgressCallback)(long current, long max);

/**
 *  getUrlInBackground callback.
 *
 *  @param url File url.
 */
typedef void(^DroiFileGetUrlCallback)(NSURL* url);

/**
 *  Be able upload/download file to DroiCloud with DroiFile.
 */
DroiObjectName(@"_File")
@interface DroiFile : DroiObject
/**
 *  Construct `DroiFile` with file path.
 *
 *  @param fileName File path.
 *
 *  @return `DroiFile`
 */
+ (instancetype) fileWithFileName:(NSString*) fileName;

/**
 *  Construct `DroiFile` with file path and mime type.
 *
 *  @param fileName File path.
 *  @param mimeType File mime type.
 *
 *  @return `DroiFile`
 */
+ (instancetype) fileWithFileName:(NSString*) fileName mimeType:(NSString*) mimeType;
/**
 *  Construct `DroiFile` with data and name.
 *
 *  @param data data
 *  @param name name
 *
 *  @return `DroiFile`
 */
+ (instancetype) fileWithData:(NSData*) data name:(NSString*) name;
/**
 *  Construct `DroiFile` with data
 *
 *  @param data data
 *
 *  @return `DroiFile`
 */
+ (instancetype) fileWithData:(NSData*) data;
/**
 *  Construct `DroiFile` with data and mime type.
 *
 *  @param data     data
 *  @param mimeType mime type.
 *
 *  @return `DroiFile`
 */
+ (instancetype) fileWithData:(NSData*) data mimeType:(NSString*) mimeType;
/**
 *  Construct `DroiFile` with data and name and mime type.
 *
 *  @param data     data
 *  @param name     name
 *  @param mimeType mime type.
 *
 *  @return `DroiFile`
 */
+ (instancetype) fileWithData:(NSData*) data name:(NSString*) name mimeType:(NSString*) mimeType;

/**
 *  Synchronously get file data from cache if available or fetches from the network.
 *
 *  @param error     Pass DroiError to retrieve error details, or pass nil to ignore.
 *
 *  @return NSData for file content.
 */
- (NSData*) get:(DroiError**) error;

/**
 *  Get file data from cache if available or fetches from the network in the background.
 *
 *  @param callback Callback when done.
 */
- (NSString*) getInBackground:(DroiGetFileCallback) callback;
/**
 *  Get file data from cache if available or fetches from the network in the background.
 *
 *  @param callback         Callback when done.
 *  @param progressCallback Progress callback.
 */
- (NSString*) getInBackground:(DroiGetFileCallback) callback progressCallback:(DroiFileProgressCallback) progressCallback;
/**
 *  Save file data to cloud storage in the background thread.
 *
 *  @param callback         Callback when done.
 *  @param progressCallback Progress callback.
 */
- (NSString*) saveInBackground:(DroiObjectCallback) callback progressCallback:(DroiFileProgressCallback) progressCallback;
/**
 *  Update file. Will remove old file after successfuly updated.
 *
 *  @param fileName File path.
 *
 *  @return DroiError DroiError object. Developer should use isOk to check whether this result is OK.
 */
- (DroiError*) updateFileName:(NSString*) fileName;
/**
 *  Update file. Will remove old file after successfuly updated.
 *
 *  @param fileName File path.
 *  @param mimeType File mime type.
 *
 *  @return DroiError DroiError object. Developer should use isOk to check whether this result is OK.
 */
- (DroiError*) updateFileName:(NSString*) fileName mimeType:(NSString*) mimeType;
/**
 *  Update file. Will remove old file after successfuly updated.
 *
 *  @param data data
 *  @param name name
 *
 *  @return DroiError DroiError object. Developer should use isOk to check whether this result is OK.
 */
- (DroiError*) updateData:(NSData*) data name:(NSString*) name;
/**
 *  Update file. Will remove old file after successfuly updated.
 *
 *  @param data data
 *
 *  @return DroiError DroiError object. Developer should use isOk to check whether this result is OK.
 */
- (DroiError*) updateData:(NSData*) data;
/**
 *  Update file. Will remove old file after successfuly updated.
 *
 *  @param data     data
 *  @param mimeType mime type.
 *
 *  @return DroiError DroiError object. Developer should use isOk to check whether this result is OK.
 */
- (DroiError*) updateData:(NSData*) data mimeType:(NSString*) mimeType;
/**
 *  Update file. Will remove old file after successfuly updated.
 *
 *  @param data     data
 *  @param name     name
 *  @param mimeType mime type.
 *
 *  @return DroiError DroiError object. Developer should use isOk to check whether this result is OK.
 */
- (DroiError*) updateData:(NSData*) data name:(NSString*) name mimeType:(NSString*) mimeType;
/**
 *  Update file in background thread. Will remove old file after successfuly updated.
 *
 *  @param fileName File path.
 *  @param callback The callback object `DroiObjectCallback` is used to receive save result.
 *
 *  @return taskId for calling the task.
 */
- (NSString*) updateFileNameInBackground:(NSString*) fileName callback:(DroiObjectCallback) callback;
/**
 *  Update file in background thread. Will remove old file after successfuly updated.
 *
 *  @param fileName File path.
 *  @param mimeType File mime type.
 *  @param callback The callback object `DroiObjectCallback` is used to receive save result.
 *
 *  @return taskId for calling the task.
 */
- (NSString*) updateFileNameInBackground:(NSString*) fileName mimeType:(NSString*) mimeType callback:(DroiObjectCallback) callback;
/**
 *  Update file in background thread. Will remove old file after successfuly updated.
 *
 *  @param data data
 *  @param name name
 *  @param callback The callback object `DroiObjectCallback` is used to receive save result.
 *
 *  @return taskId for calling the task.
 */
- (NSString*) updateDataInBackground:(NSData*) data name:(NSString*) name callback:(DroiObjectCallback) callback;
/**
 *  Update file in background thread. Will remove old file after successfuly updated.
 *
 *  @param data data
 *  @param callback The callback object `DroiObjectCallback` is used to receive save result.
 *
 *  @return taskId for calling the task.
 */
- (NSString*) updateDataInBackground:(NSData*) data callback:(DroiObjectCallback) callback;
/**
 *  Update file in background thread. Will remove old file after successfuly updated.
 *
 *  @param data     data
 *  @param mimeType mime type.
 *  @param callback The callback object `DroiObjectCallback` is used to receive save result.
 *
 *  @return taskId for calling the task.
 */
- (NSString*) updateDataInBackground:(NSData*) data mimeType:(NSString*) mimeType callback:(DroiObjectCallback) callback;
/**
 *  Update file in background thread. Will remove old file after successfuly updated.
 *
 *  @param data     data
 *  @param name     name
 *  @param mimeType mime type.
 *  @param callback The callback object `DroiObjectCallback` is used to receive save result.
 *
 *  @return taskId for calling the task.
 */
- (NSString*) updateDataInBackground:(NSData*) data name:(NSString*) name mimeType:(NSString*) mimeType callback:(DroiObjectCallback) callback;

/**
 *  Get file url.
 *
 *  @return URL
 */
- (NSURL*) getUrl;

/**
 *  Get file url in background thread.
 *
 *  @param callback url callback function.
 *
 *  @return false to fail to run in background thread.
 */
- (BOOL) getUrlInBackground:(DroiFileGetUrlCallback) callback;
/**
 *  Is the file content data modified.
 */
@property BOOL isContentDirty;

-(instancetype) init __attribute__((unavailable("init not available")));

@end
