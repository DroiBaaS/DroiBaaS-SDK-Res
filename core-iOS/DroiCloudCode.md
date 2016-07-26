[CC_Geting_Start]: ./../cloudcode/lua/GETTING_STARTED.md


# 云代码

## 简介
云代码（Cloud Code）是部署运行在 DroiBaaS 云引擎上的代码，您可以用它来实现较复杂的，需要运行在云端的业务逻辑。DroiBaaS希望帮助开发者无需处理服务器逻辑便可简单的开发出各种apps，只需要编写额外的的逻辑程序上传至DroiBaaS即可。这类逻辑程序即为云代码。目前DroiBaaS 能处理以lua脚步语言编写的云代码。
## 执行CloudCode
### 使用DroiCloud
开发者可以从客户端呼叫存放于DroiBaaS的Cloud Code. 通过调用DroiCloud类的接口callCloudService 来指定欲执行的Cloud Code, 并且可以传入参数, 以及接受Cloud code的执行结果和相关错误(DroiError).

* Objective-C

```objc
    
+ (id) callCloudService : (NSString*) name
              parameter : (DroiObject*) parameter
           andClassType : (Class) clazz
                   error:(DroiError**) error;
                   
```

* Swift

```swift
DroiCloud.callCloudService(name: String!, parameter: DroiObject!, andClassType: AnyClass!, error: AutoreleasingUnsafeMutablePointer<DroiError?>)
```


也可以调用DroiCloud类的callCloudServiceInBackground接口后台运行


* Objective-C

```objc
+ (NSString*) callCloudServiceInBackground : (NSString*) name
                                 parameter : (DroiObject*) parameter
                               andCallback : (DroiCloudCallback) callback
                             withClassType : (Class) clazz;
```

* Swift

```swift
DroiCloud.callCloudServiceInBackground(name: String!, parameter: DroiObject!, andCallback: DroiCloudCallback!, withClassType: anyClass!)
```

**注意：从客户端只能呼叫Entry Cloud Code, 相关定义请参考[Cloud Code Getting Start][CC_Geting_Start]**


### sample
我们提供的sample cloud code中有一个名为add.lua的示例. 它可以获取两个数字的和.

``` lua
local Logger = require("DroiCloud.CloudLogger")
local _M = {}

function _M.main(request)
    local response = {}
    Logger.log(Logger.DEBUG, "add")

    if request then
        local cal = request.num1 + request.num2
        --Put result in a table and return it.
        response['result'] = cal
    end

    return response
end

return _M
```
若是想要执行add.lua,可以调用以下代码.


* Objective-C

```objc   
    @interface AddInput : DroiObject
        DroiExpose
        @property float num1;
        DroiExpose
        @property float num2;
    @end

    @implementation AddInput
    @end

    @interface AddResult : DroiObject
        DroiExpose
        @property float result;
    @end

    @implementation AddResult
    @end

    - (void) callSimpleCloudCode() {
        AddInput* addInput = [AddInput new];
        addInput.num1 = 1.0;
        addInput.num2 = 2.0;
 		AddResult* result = [DroiCloud callCloudService:@"add.lua"
                                     		   parameter:addInput
                                   		andClassType:[AddResult class]
                                                 error:nil];
        NSLog(@"Result: %d", addResult);
    }
    @end

    - (void) callSimpleCloudCodeInBG {
        AddInput* addInput = [AddInput new];
        addInput.num1 = 1.0;
        addInput.num2 = 2.0;

        DroiTaskDispatcher* taskDispatcher = [DroiTaskDispatcher getTaskDispatcher:BackgroundThreadDispatcher];

        [taskDispatcher enqueueTask:^{
        [DroiCloud callCloudServiceInBackground:@"add.lua" parameter:addInput andCallback:^(AddResult *addResult , DroiError *error) {
                XCTAssertNotEqual(addResult.result, 0.0);
        } withClassType:[AddResult2 class]];
    }];
        AddResult* addResult = [DroiCloud callCloudService:@"add.lua" parameter:addInput andClassType:[AddResult2 class] error:nil];
        XCTAssertNotEqual(addResult.result, 0.0);
}
```

* Swift

```swift
    import DroiCoreSDK.DroiObject
    import DroiCoreSDK.DroiCloud

    class AddInput : DroiObject{
        // DroiExpose
        var num1: Float = 0.0
        // DroiExpose
        var num2: Float = 0.0
    }

    class AddResult : DroiObject{
        // DroiExpose
        var result: Float = 0.0
    }

    func callSimpleCloudCode(){
        let target_cloudcode_name = "add.lua"
        var error: DroiError? = nil

        let addInput = AddInput()
        addInput.num1 = 1.0
        addInput.num2 = 2.0

        var addResult = AddResult()

        addResult = DroiCloud.callCloudService(target_cloudcode_name, parameter: addInput, andClassType: AddResult.self, error: &error) as! AddResult!
    }

    func callSimpleCloudCodeInBG(){
        let disp = DroiTaskDispatcher.getTaskDispatcher("123")
        disp.enqueueTask {
            DroiCloud.callCloudServiceInBackground(self.target_cloudcode_name, parameter: self.addInput, andCallback:
                {
                    (addResult, error) in
                    XCTAssertNotEqual(addResult.result, 0.0, "Not a valiad calcuation")
                }
                , withClassType: AddResult.self)

        }
        sleep(3)
    }
```

    