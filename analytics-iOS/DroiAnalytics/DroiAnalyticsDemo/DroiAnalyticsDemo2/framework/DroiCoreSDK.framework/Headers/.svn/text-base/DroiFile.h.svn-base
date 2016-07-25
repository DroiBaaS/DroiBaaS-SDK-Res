/*
 * Copyright (c) 2016-present Shanghai Droi Technology Co., Ltd.
 * All rights reserved.
 */

#import <Foundation/Foundation.h>
#import "DroiObject.h"

#define MAX_AVAILABLE_FILE_SIZE 10485760

/**
 *  Get file status code.
 */
typedef NS_ENUM(int, GetFileCode) {
    /**
     *  Download successfully.
     */
    GETFILE_OK,
    /**
     *  Download fail.
     */
    GETFILE_FAIL,
    /**
     *  Download fail due to file is not ready.
     */
    GETFILE_NOT_READY
};

/**
 *  Get file result for `get`
 */
@interface DroiGetFileResult : NSObject
/**
 *  File data
 */
@property NSData* data;
/**
 *  Get file status
 */
@property GetFileCode code;
@end

/**
 *  Callback definition for `getInBackground:` or `getInBackground:progressCallback:`
 *
 *  @param result Get file result
 */
typedef void(^DroiGetFileCallback)(DroiGetFileResult* result);
/**
 *  Progressive callback for `getInBackground:progressCallback:`
 *
 *  @param current current uploaded size
 *  @param max     file size
 */
typedef void(^DroiFileProgressCallback)(long current, long max);

/**
 *  Be able upload/download file to DroiCloud with DroiFile.
 */
@interface DroiFile : NSObject
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
 *  @return `DroiGetFileResult`
 */
- (DroiGetFileResult*) get;

/**
 *  Get file data from cache if available or fetches from the network in the background.
 *
 *  @param callback Callback when done.
 */
- (void) getInBackground:(DroiGetFileCallback) callback;
/**
 *  Get file data from cache if available or fetches from the network in the background.
 *
 *  @param callback         Callback when done.
 *  @param progressCallback Progress callback.
 */
- (void) getInBackground:(DroiGetFileCallback) callback progressCallback:(DroiFileProgressCallback) progressCallback;
/**
 *  Set save progress callback
 *
 *  @param progressCallback Progress callback
 */
- (void) setSaveProcessListener:(DroiFileProgressCallback) progressCallback;
/**
 *  Set file storage. Local or cloud.
 */
@property BOOL localStorage;
/**
 *  DroiFile name.
 */
@property NSString* name;

@end
