# 云代码
## 简介
云代码是部署运行在 DroiBaaS 云引擎上的代码，您可以用它来实现较复杂的，需要运行在云端的业务逻辑。  
从客户端执行 DroiBaaS 云代码，使用基础类 DroiCloud 并结合基础类 DroiObject 来定义输入以及输出即可操作云代码。  
目前支持 lua，其他语言尽请期待。  

## 执行CloudCode
### 使用DroiCloud类
开发者可从客户端调用存放于DroiBaaS的Cloud Code。通过DroiCloud类的 callCloudService 来指定要执行的Cloud Code，想传入的参数，以及接受Cloud Code的执行结果，以及相关的DroiError。  

```
DroiCloud.callCloudService(String name, T params, Class<V> resultType, DroiError error);
```

或是通过DroiCloud类的callCloudServiceInBackground

```
DroiCloud.callCloudServiceInBackground(final String name, final T params, 
    final DroiCallback<V> callback, final Class<V> resultType)
```

### 限制
从客户端只能调用Entry Cloud Code, 相关定义请参照[Cloud Code Getting Start]()

### sample
我们提供的sample cloud code中有一个名为add.lua的示例。他可以处理两个数字相加并回传其结果。

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
若是想要执行add.lua, 则可以通过下面的程序片段即可：

``` java
// Add class ++
public static class Add {
    // Input
    public static class Request extends DroiObject {
        @DroiExpose
        float num1;

        @DroiExpose
        float num2;
    }

    // Output
    public static class Response extends DroiObject {
        @DroiExpose
        float result;
    }
}
// Add class --

public String testAdd() {
    Add.Request request = new Add.Request();
    Add.Response response = null;

    request.num1 = 1;
    request.num2 = 2;

    String target_lua = "add.lua";
    DroiError err = new DroiError();

    response = DroiCloud.callCloudService(target_lua, request, Add.Response.class, err);
}
```