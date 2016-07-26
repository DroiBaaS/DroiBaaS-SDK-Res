# 用户管理
DroiBaas提供了用于用户管理的类，叫做 DroiUser，可自动处理用户帐户管理需要的很多功能。
开发者可以使用这个类在应用程序中添加用户帐户功能。  
DroiUser 是 DroiObject 的一个子类，拥有与之完全相同的特性，如键值对接口。DroiObject 上的所有方法也存在于 DroiUser 中。不同的是 DroiUser 具有针对用户帐户的一些特殊的附加功能。
## 云端存取控制 (ACL - Access Control List)

当数据都是在放在云端时，会衍生一个重要的问题 -安全性。为了保护开发者的数据，DroiBaaS 提供了完整的存取控制 API，可以让开发者保护私有数据。存取控制是强制开启的，即使开发者没有设置也会预设启用。存取控制分成以下部分说明：

* [用户](#user)
* [群组](#group)
* [权限](#permission)
* [DroiObject 权限](#object)

## <a id="user"></a>用户

DroiBaaS 提供了 DroiUser，让开发者可以设计一套有使用户概念的应用程式。DroiUser 定义了以下三种：

* [匿名用户](#user_anonymous)
* [注册用户](#user_normal)
* [第三方授权用户](#user_third)

### <a id="user_anonymous"></a>匿名用户

所有对云端数据存取都需要先进行使用者认证，认证后才能够使用。为了方便开发者和用户，DroiBaaS 提供了匿名用户。DroiBaaS 会为每台设备生成一个 ID，利用这串 ID，可以让开发者可以辨认出用户而不需要用户先注册账号。以下是 DroiUser 匿名用户相关 API使用示例。

#### 登录

* Objective-C

    ```objc
    DroiError* error = nil;
    DroiUser* anonymousUser = [DroiUser loginWithAnonymous:&error];
    if (error.isOk && anonymousUser != nil) {
        // Login OK
        // ObjectId 为 User 的唯一 ID，每个 User 都有其独一无二的 ID
        NSLog(@"User object id: %@", anonymousUser.objectId);
        // SessionToken 为 User 成功登入后，服务器生成的 ID，对服务器所有动作都需要有合法的 SessionToken
        NSLog(@"User session token: %@", anonymousUser.sessionToken);
    }
    ```
    
* Swift

    ```swift
    var error: DroiError? = nil;
    let anonymousUser = DroiUser.loginWithAnonymous(&error)
    if (error!.isOk() && anonymousUser != nil) {
        // Login OK
        // ObjectId 为 User 的唯一 ID，每个 User 都有其独一无二的 ID
        NSLog("User object id", anonymousUser.objectId)
        // SessionToken 为 User 成功登入后，服务器生成的 ID，对服务器所有动作都需要有合法的 SessionToken
        NSLog("User session token", anonymousUser.sessionToken);
    }    
    ```

#### 登出

* Objective-C
    
    ```objc
    // 取得目前成功登入的使用者
    DroiUser* user = [DroiUser getCurrentUser];
    if (user != nil && [user isAuthorized]) {
        [user logout];
    }    
    ```
    
* Swift

    ```swift
    // 取得目前成功登入的使用者
    let user = DroiUser.getCurrentUser()
    if user != nil && user.isAuthorized() {
        user.logout()
    }
    ```

#### 自动登录

因为所有对云端数据存取时都需要是合法的使用者，为了简化开发者应用的流程，可以让开发者不用处理用户概念就能够存取云端数据，在这里提供了匿名用户自动登录的机制，只要启用 (默认启用），SDK 会在存取云端数据前先检测用户合法性，如果是尚未登入过，即会自动以匿名用户登入。以下是使用示例。

* Objective-C

    ```objc    
    @interface MyObject : DroiObject
    DroiExpose
    @property NSString* stringField;
    @end
    ```
    
	```objc
    MyObject* object1 = [MyObject new];
    // 设置此对象直接存到 DroiBaaS 云端服务器上
    object1.localStorage = NO;
    // 直接存储，并且沒有先登入合法用户。save 时，会自动登入匿名用户。
    [object1 save];
    
    // 因为 save 会自动登入，所以这里可以用 getCurrentUser 取出目前登入的合法用户。
    DroiUser* user = [DroiUser getCurrentUser];
    NSLog(@"Is anonymous %d, sessionToken: %@", [user isAnonymous], user.sessionToken);
    ```    
    
* Swift

    ```swift
    class MyObject : DroiObject {
        // DroiExpose
        var stringField: String = ""
    }
    ```

    ```swift
    let object1 = MyObject()
    // 设置此对象直接存到 DroiBaaS 云端服务器上
    object1.localStorage = false
    // 直接存储，并且沒有先登入合法用户。save 时，会自动登入匿名用户。
    object1.save()
    
    // 因为 save 会自动登入，所以这里可以用 getCurrentUser 取出目前登入的合法用户。
    let user = DroiUser.getCurrentUser()
    NSLog("Is anonymous %d, sessionToken: %@", user.isAnonymous(), user.sessionToken);
    ```

### <a id="user_normal"></a>注册用户

所谓注册用户，这里定义为需要用账号/密码登录的用户。DroiUser 定义了 UserId 属性和 Password 属性代表账号/密码。为了让匿名用户可以延续操作经验，DroiUser 可以让匿名用户通过注册直接升级为注册用户。例如设计一款游戏，平时游玩时，玩家不需要登录，这时会是以自动登录匿名用户的方式游玩，但因为购买游戏内道具需要成为注册用户，通过这个机制，可以直接把该匿名用户升级为注册用户。以下是注册用户 DroiUser API 使用示例。

另外 DroiUser 提供了开发者记录用户资料的功能。例如，今天设计一款外卖类App，会需要额外记录像是用户的手机号码，地址，等等。开发者可以直接创建一个类继承 DroiUser，这样就可以像一般的创建 DroiObject 一样定义属性了。

#### 注册

* Objective-C

    ```objc
    @interface MyUser : DroiUser
    DroiExpose
    @property NSString* phoneNumber;
    
    DroiExpose
    @property NSString* address;
    @end
    ```
    
    ```objc
    MyUser* user = [MyUser new];
    
    user.UserId = @"userid";
    user.Password = @"p@ssw0rd";
    user.phoneNumber = @"0912345678";
    user.address = @"NO 1, IN EARTH";
    
    DroiError* result = [user signUp];
    if (result.isOk) {
        // SIGN UP OK!
    }
    ```
    
* Swift

    ```swift
    class MyUser : DroiUser {
        // DroiExpose
        var phoneNumber: String = ""
        
        // DroiExpose
        var address: String = ""
    }    
    ```
    
    ```swift
    let user = MyUser()
    
    user.UserId = "userid"
    user.Password = "p@ssw0rd"
    user.phoneNumber = "0912345678"
    user.address = "NO 1, IN EARTH"
    
    let result = user.signUp()
    if result!.isOk() {
        // SIGN UP OK!
    }
    ```

#### 登录

* Objective-C

    ```objc
    // 登录時，需传入自定义的对象的名称
    DroiError* error = nil;
    MyUser* user = [DroiUser loginByUserClass:@"userid" password:@"p@ssw0rd" userClass:[MyUser class] error:&error];
    if (error.isOk && user != nil && [user isAuthorized]) {
        // LOGIN OK
    }    
    ```
    
* Swift

    ```swift
    var error: DroiError? = nil;
    let user = DroiUser.loginByUserClass("userid", password: "p@ssw0rd", userClass: MyUser.self, error:&error)
    if error!.isOk() && user != nil && user.isAuthorized() {
        // LOGIN OK
    }
    ```

#### 使匿名用户成为注册用户

* Objective-C

    ```objc
    // 取得当前用户
    MyUser* user = [DroiUser getCurrentUserByUserClass:[MyUser class]];
    // 如果当前用户合法且是匿名用户
    if (user != nil && [user isAuthorized] && [user isAnonymous]) {
        // 填入用户资料
        user.UserId = @"userid";
        user.Password = @"p@ssw0rd";
        user.phoneNumber = @"0912345678";
        user.address = @"NO 1, IN EARTH";
        
        DroiError* result = [user signUp];
        if (result.isOk) {
            // SIGN UP OK!
        }
    }
    ```
    
* Swift

    ```swift
    // 取得当前用户    
    let user = MyUser.getCurrentUserByUserClass(MyUser.self) as! MyUser!
    // 如果当前用户合法且是匿名用户    
    if user != nil && user.isAuthorized() && user.isAnonymous() {
        // 填入用户资料
        user.UserId = "userid"
        user.Password = "p@ssw0rd"
        user.phoneNumber = "0912345678"
        user.address = "NO 1, IN EARTH"
        
        let result = user.signUp()
        if result!.isOk() {
            // SIGN UP OK!
        }
    }
    ```

### <a id="user_third"></a>第三方授权用户

为了更方便开发者操作，DroiUser 提供像 Facebook, Twitter, Google, QQ, Sina 微博 这样的第三方账号登录。相关的 DroiUser API 如下。

## <a id="group"></a>群组管理

当App成长到一定规模后，管理用户将会是十分负责的事情。DroiBaaS 提供群组管理功能，可以让开发者为用户分群，权限控制就可以设定到这个群组上，可以简化管理和减少出错的可能。

***新增群组***

* Objective-C

    ```objc
    DroiGroup* group = [DroiGroup groupWithName:@"Normal User"];
    [group addUser:user1.objectId];
    [group addUser:user2.objectId];
    [group save];    
    ```
    
* Swift

    ```swift
    let group = DroiGroup(name: "Normal User")
    group.addUser(user1.objectId)
    group.addUser(user2.objectId)
    group.save()    
    ```

***查询群组***

* Objective-C

    ```objc
    // 建立 Query
    DroiQuery* query = [DroiQuery create];
    NSArray* list = [[[[query cloudStorage] queryByClass:[DroiGroup class]] whereStatement:@"Name" opType:DroiCondition_EQ arg2:@"Normal User"] runQuery:nil]; // 传入 nil 表示忽略错误
    if (list.count > 0) {
        DroiGroup* group = [list objectAtIndex:0];
        // 要取得 Group 里所有用户，需要呼叫 fetch
        [group fetch];
        NSArray* userList = [group getUserIdList];
    }
    ```
    
* Swift

    ```swift
    // 建立 Query
    let query = DroiQuery.create().cloudStorage().queryByClass(DroiGroup.self)
        .whereStatement("Name", opType: DroiCondition_EQ, arg2: "Normal User")
    let list = query.runQuery(nil) as! [DroiGroup]! // 传入 nil 表示忽略错误
    var userList: [String]!
    
    if list?.count > 0 {
        let group = list[0]
        // 要取得 Group 里所有用户，需要呼叫 fetch
        group.fetch()
        
        userList = group.getUserIdList() as! [String]!
    }    
    ```
    
***阶层式群组***

除了可以加入用户到一个群组内，也可以加入群组到另一个群组内，成为阶层式的群组。例如，设定App有两个群组，一个是`系统管理群`，另一个是`普通用户群`。`系统管理群`可以操作到管理的功能，`普通用户群`只有普通功能而以。但是`系统管理群`同时也要能操作普通功能，所以在这里会把`系统管理群`加入到`普通用户群`里，示例如下：

* Objective-C

```objc
- (void) groupDemo {
    DroiGroup* adminGroup = [self queryGroup:@"Administrator"];
    DroiGroup* userGroup = [self queryGroup:@"User"];
    
    [userGroup addGroup:adminGroup.objectId];
    [userGroup save];
}

- (DroiGroup*) queryGroup:(NSString*) name {
    DroiQuery* query = [DroiQuery create];
    NSArray* list = [[[[query cloudStorage] queryByClass:[DroiGroup class]] whereStatement:@"Name" opType:DroiCondition_EQ arg2:name] runQuery:nil]; // 传入 nil 表示忽略错误
    if (list.count == 0){
        return nil;
    }
    return [list objectAtIndex:0];
}	
```

* Swift

    ```swift
    func groupDemo() {
        let adminGroup = queryGroup("Administrator")
        let userGroup = queryGroup("User")
        
        userGroup.addUser(adminGroup.objectId)
        userGroup.save()
    }
    
    func queryGroup(name: String) -> DroiGroup! {
        let query = DroiQuery.create().cloudStorage().queryByClass(DroiGroup.self).whereStatement("Name", opType: DroiCondition_EQ, arg2: name)
        let list = query.runQuery(nil) as! [DroiGroup]! // 传入 nil 表示忽略错误
        if list == nil || list.count == 0 {
            return nil;
        }
        
        return list[0];
    }
    ```

## <a id="permission"></a>权限

权限是控制谁可以做什么事，谁不能做什么事。所以在介绍权限前，先说明了用户和群组这两个权限依附对象。在 DroiBaaS，可以设定权限的目标为 DroiObject。细分的话，可以设定 DroiObject 这个表格存取控制权限和表格内每一份数据的存取控制权限。不过在 DroiBaaS SDK 内部无法设置表格存取权限，需至 Droi 开发平台上面设置。

存取控制可分別设置读取和写入权限，所以会有以下四种情况。

|读取|写入|说明|
|---|---|---|
|false|false|不可读不可写|
|true|false|可读不可写|
|false|true|不可读可写|
|true|true|可读可写

对象分成三类。
 
 * 所有用户
 * 特定用户
 * 特定群组

以下介绍权限相关 API。

### 设置权限

* Objective-C

    ```objc
    DroiPermission* permission = [DroiPermission new];
    
    // 设置所有用户权限为只读
    [permission setPublicReadPermission:YES];
    [permission setPublicWritePermission:YES];
    
    // 设置当前登录的用户的权限为可读可写
    DroiUser* user = [DroiUser getCurrentUser];
    [permission setUserReadPermission:user.objectId flag:YES];
    [permission setUserWritePermission:user.objectId flag:YES];
    
    // 设置 User 群组的权限是可写不可读
    DroiGroup* group = [self queryGroup:@"User"];
    [permission setGroupReadPermission:group.objectId flag:NO];
    [permission setGroupWritePermission:group.objectId flag:YES];
    ```
    
* Swift

    ```swift
    let permission = DroiPermission()
    
    // 设置所有用户权限为只读
    permission.setPublicReadPermission(true)
    permission.setPublicWritePermission(true)
    
    // 设置当前登录的用户的权限为可读可写
    let user = DroiUser.getCurrentUser()
    permission.setUserReadPermission(user.objectId, flag: true)
    permission.setUserWritePermission(user.objectId, flag: true)
    
    // 设置 User 群组的权限是可写不可读
    let group = queryGroup("User")
    permission.setGroupReadPermission(group.objectId, flag: false)
    permission.setGroupWritePermission(group.objectId, flag: true)
    ```

### 设置预设权限

操作应用程序时，需要存储数据到云端时，预设每一份 DroiObject 数据都会预设加入当前登录用户可以读写。这个预设权限是可以改变的。

* Objective-C

    ```objc
    DroiPermission* permission = [DroiPermission getDefaultPermission];
    if (permission == nil)
        permission = [DroiPermission new];
    
    // 设置预设权限为所有用户只读
    [permission setPublicReadPermission:YES];
    [permission setPublicWritePermission:NO];
    
    [DroiPermission setDefaultPermission:permission];
    ```
    
* Swift

    ```swift
    var permission = DroiPermission.getDefaultPermission()
    if permission == nil {
        permission = DroiPermission()
    }
    
    // 设置预设权限为所有用户只读
    permission.setPublicReadPermission(true)
    permission.setPublicWritePermission(false)
    
    DroiPermission.setDefaultPermission(permission)
    ```

## <a id="object"></a>DroiObject 权限 

从一开始介绍了存取控制的依附对象(用户、群组)，跟着介绍了存取控制的权限本身(DroiPermission)，最后要说明的是存取控制的对象 - DroiObject。  

DroiBaaS SDK 提供了完整的存取控制 API 让开发者能够弹性使用。开发者可以不用写一行存取控制的代码就可以有基本的权限保护，每份数据都只有其拥有者可以存取。  

以下是云端数据存取控制相关 API 介绍。

### 设置权限

* Objective-C

    ```objc
    DroiUser* user = [DroiUser getCurrentUser];
    
    // 设置权限为所有用户只读，拥有者可读写
    DroiPermission* permission = [DroiPermission new];
    [permission setPublicReadPermission:YES];
    [permission setPublicWritePermission:NO];
    
    [permission setUserReadPermission:user.objectId flag:YES];
    [permission setUserWritePermission:user.objectId flag:YES];
    
    MyObject* myObject = [MyObject new];
    myObject.stringField = @"Object field name";
    myObject.permission = permission;
    
    [myObject save];
    ```
    
* Swift

    ```swift
    let user = DroiUser.getCurrentUser()
    
    //  设置权限为所有用户只读，拥有者可读写
    let permission = DroiPermission()
    permission.setUserReadPermission(user.objectId, flag: true)
    permission.setUserWritePermission(user.objectId, flag: true)
    
    let myObject = MyObject()
    myObject.stringField = "Object field name"
    myObject.permission = permission
    
    myObject.save()
    ```

### 默认权限

在未设定 DroiObject 的 Permission 时，SDK 会自动默认预设权限，默认权限可由 `DroiPermission.setDefaultPermisson` 设置。默认值是当前登录用户可读可写。
