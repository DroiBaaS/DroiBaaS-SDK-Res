

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>


@interface DroiAnalytics : NSObject

/**初始化DroiAnalytics SDK 必须调用
 */
+ (BOOL)startAnalytics;

/**配置上传数据策略
 *  @param minutes  间隔时间
 *  @param wifiOnly
 */
+ (void)setScheduleConfigTime:(int)minutes onlyInWifi:(BOOL)wifiOnly;

/**配置Log功能，默认为开启状态
 */
+ (void)setLogEnabled:(BOOL)enabled;

/**配置崩溃信息统计功能,默认为开启状态
 */
+ (void)setCrashReportEnabled:(BOOL)enabled;

/**页面统计功能 必须在对应的方法中调用,且必须与endLogPageView方法配合使用才能准确统计页面信息
   @param pageName 自定义的页面名称
 */
+ (void)beginLogPageView:(NSString *)pageName;

/**页面统计功能 必须在对应的方法中调用,且必须与beginLogPageView方法配合使用才能准确统计页面信息
 @param pageName 自定义的页面名称
 */
+ (void)endLogPageView:(NSString *)pageName;

/**自定义事件统计功能
   @param eventId      事件id    字符串类型用于标记事件
   @param num          事件数值   计算事件代表这次事件的数值
   @param attributes   额外参数   可以传入自定义的额外参数
 */

+ (void)event:(NSString *)eventId;

+ (void)event:(NSString *)eventId attributes:(NSDictionary *)attributes;

+ (void)event:(NSString *)eventId attributes:(NSDictionary *)attributes num:(NSInteger)num;

/**地位位置信息统计
 */
+ (void)setLatitude:(double)latitude longitude:(double)longitude;

+ (void)setLocation:(CLLocation *)location;

@end
