# 用户管理

## 简介
当数据都是放在云端时，会衍生一个重要的问题 - `安全性`。为了保护开发者的数据，DroiBaaS 提供了完整的用户管理存储控制 API，可让开发者保护私有数据。ACL是强制启用的，即使开发者没有设置也会默认启用。用户管理分成以下部分说明。  

* [用户](#user)
* [群组](#group)
* [权限管理](#permission)
* [DroiObject权限](#object)

## <a id="user"></a>用户

DroiBaaS 提供了 `DroiUser`，让开发者能设计一套有用户概念的应用程序。DroiUser 定义三种用户：  

* [匿名用户](#user_anonymous)
* [注册用户](#user_normal)
* [第三方授权用户](#user_third)

### <a id="user_anonymous"></a>匿名用户

所有对云端数据存取都需要先做过用户认证，认证后才能够使用。为了方便开发者和用户，DroiBaaS 提供了匿名用户。DroiBaaS 会为每部手机配发一组 ID,利用这组 ID，可以让开发者辨识出用户而不需要用户先注册账号。以下是DroiUser 匿名用户相关 API 操作示例。  

#### 登录

``` java
DroiError error = new DroiError();
DroiUser anonymousUser = DroiUser.loginWithAnonymous(error);
if (error.isOk() && anonymousUser != null) {
    Log.i("DroiUser", "Login OK!");
    // ObjectId 为 User 的唯一 ID，每个 User 都有其独一无二 ID
    Log.i("DroiUser", "  User ObjectId: " + anonymousUser.getObjectId());
    // SessionToken 为 User 成功登录后，服务器下发的 ID，对服务器所有操作都需要有合法的 SessionToken
    Log.i("DroiUser", "  User sessionToken: " + anonymousUser.getSessionToken());
}
```

#### 登出

``` java
// 取得目前成功登录的用户
DroiUser user = DroiUser.getCurrentUser();
if (user != null && user.isAuthorized()) {
    user.logout();
}
```

#### 自动登录

因为所有云端数据存取时都需要是合法的用户，为了简化开发人员使用流程，可以让开发者不用处理用户概念就能够存取云存储数据，在这里提供了匿名用户自动登录机制，只要开启（默认开启），SDK 会在存取云存储数据前先检查用户合法性，如果是尚未登录，即会自动以匿名用户登录。以下是使用示例：  

***MyApp.java***

``` java
public class MyApp extends Application {
    // 自定义的 DroObject，需要储存的属性需加上 @DroiExpose 注解
    public static class MyObject extends DroiObject {
        @DroiExpose
        public String stringField;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Core.initialize(this);
        // 启用自动登录匿名用户（默认开启，可不写）
        DroiUser.setAutoAnonymousUser(true);
        // 将自定义的 MyObject 注册到 DroiObject里        		
		DroiObject.registerCustomClass(MyObject.class);
    }
}
```

***MainActivity.java***

``` java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MyApp.MyObject object1 = new MyApp.MyObject();
    // 设定此对象直接存到 DroiBaaS 服务器上
    object1.setLocalStorage(false);
    // 直接存储，并没有先登录合法用户。sava 时，会自动登录匿名用户
    object1.save();
    
    // 因为 save 会自动登录，所以可以用 getCurrentUser 可取出目前登录的合法用户。
    DroiUser user = DroiUser.getCurrentUser();
    Log.d("Main", "Is anonymous: " + (user.isAnonymous()) + ", sessionToken: " + user.getSessionToken());
}
```

### <a id="user_normal"></a>注册用户

所谓注册用户，定义为需要用账号/密码登录的用户。DroiUser 定义了 UserId 属性和 Password 属性代表账号/密码。为了让匿名用户可以延续之前的操作，DroiUser 可以让匿名用户通过注册直接升级成注册用户。举个例子，设计一个游戏，平时玩游戏时，玩家不需要登录，这时会以自动登录匿名用户的方式游戏，但因为购买游戏内道具需要正式用户，通过这个机制，可以直接把该匿名用户升级为正式用户。以下是注册用户DroiUser API 操作示例。  
另外 DroiUser 提供给开发者保存用户信息功能。举例，今天设计一个订餐手机应用，会需要额外保存用户的手机号码、地址等信息。开发者直接写一个类继承自 DroiUser，这样就可以像写注册 DroiObject 一样定义属性了。  

#### 注册

***MyUser.java***

``` java
public class MyUser extends DroiUser {
    @DroiExpose
    public String phoneNumber;
    @DroiExpose
    public String address;
}
```

***MyApp.java***

``` java
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ...
        // 和注册自定义对象一样，需要注册到 DroiObject里
        DroiObject.registerCustomClass(MyUser.class);
    }
}
```

***MainActivity.java***

``` java
private void onRegisterButtonClick() {
    MyUser user = new MyUser();
    
    user.setUserId(userId.getText().toString());
    user.setPassword(password.getText().toString());
    user.phoneNumber = "0912345678";
    user.address = "NO 1, IN EARTH";
    
    DroiError result = user.signUp();
    if (result.isOk()) {
        Log.i("Main", "Signup OK!");
    }
}
```

#### 登录

``` java
private void onLoginButtonClick() {
    DroiError error = new DroiError();
    // 登录时，需传入自定义对象
    MyUser user = DroiUser.login(userId.getText().toString(), 
        password.getText().toString(), MyUser.class, error);
        
    if (error.isOk() && user != null && user.isAuthorized()) {
        Log.i("Main", "Login OK!");
    }
}
```

#### 将匿名用户转换成注册用户

``` java
private void onUpgradeUserClick() {
    // 取得目前用户
    MyUser user = DroiUser.getCurrentUser(MyUser.class);
    // 如果用户是合法的且是匿名用户
    if (user != null && user.isAuthorized() && user.isAnonymous()) {
        // 填入用户信息
        user.setUserId(userId.getText().toString());
        user.setPassword(password.getText().toString());
        user.phoneNumber = "0912345678";
        user.address = "NO 1, IN EARTH";
        
        DroiError result = user.signUp();
        if (result.isOk()) {
            Log.i("Main", "sign up OK!");
        }
    }
}
```

### <a id="user_third"></a>第三方授权用户

为了更方便用户操作，`DroiUser` 提供像 Facebook, Twitter, Google, QQ, Sina微博 这些第三方授权账号连接登录。 
 
#### QQ
以下说明如何使用`QQ`作为 `DroiUser` 第三方登录  

1. 下载[QQ SDK](http://wiki.open.qq.com/wiki/mobile/SDK下载)  

2. 将 SDK 内的 jar 目录下的 `open_sdk_rxxxx.jar` 复制到工程目录下  

3. 在 AndroidManifest.xml 加入 QQ 第三方登入需要的设置，请将 `[APP ID]` 替换成 QQ 申请的应用 APP ID  

	``` xml
    <activity
        android:name="com.tencent.tauth.AuthActivity"
        android:noHistory="true"
        android:launchMode="singleTask" >
        <intent-filter>
            <action android:name="android.intent.action.VIEW" />
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />
            <data android:scheme="[APP ID]"/>
        </intent-filter>
    </activity>
    
    <activity android:name="com.tencent.connect.common.AssistActivity"
        android:theme="@android:style/Theme.Translucent.NoTitleBar"
        android:configChanges="orientation|keyboardHidden|screenSize"
    />
	```

4. 调用登录 API 

	``` java
    authProvider = new QQOAuthProvider();
    
    DroiError error = new DroiError();
    DroiUser.loginOAuthAsync(MainActivity.this, authProvider, new DroiCallback<DroiUser>() {
        @Override
        public void result(DroiUser user, DroiError error) {
            if (error.isOk()) {
                // 登录成功！
            }
        }
    });
	```

5. 将 Activity 的 onActivityResult 传入至 QQOAuthProvider 去

    ``` java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authProvider.handleActivityResult(requestCode, resultCode, data);
    }
    ```
    
## <a id="group"></a>群组管理
当手机应用程序成长到一定规模后，管理用户这件事会是件恼人的事。DroiBaaS 提供群组功能，可以让开发者为用户们分类，权限控制就可以设置到群组上，可以简化管理和减少出错几率。

***新增群组***

``` java
DroiGroup group = new DroiGroup("Normal User");
group.addUser(user1.getObjectId());
group.addUser(user2.getObjectId());
group.save();
```

***查询群组***

``` java
// 建立 Query
DroiQuery query = DroiQuery.Builder.newBuilder().cloudStorage().query(DroiGroup.class)
    .where("Name", DroiCondition.Type.EQ, "Normal User").build();
List<DroiGroup> list = query.runQuery(null);//传入 null 表示忽略错误
DroiGroup group = list.get(0);
// 要取得 Group 里所有用户，需要调用 fetch
group.fetch();
String[] users = group.getUserIdList();
```

***层级式群组***

除了可以加入用户到一个群组内，其实也可以加入群组到另一个群组内，变成层级式的群组。举个例子，规划应用程序有两个群组，一个是`系统管理群`，另一个是`注册用户群`。`系统管理群`可以操作管理功能，`注册用户群`只有注册功能而已。但是`系统管理群`同时也要能操作注册功能，所以在这里会把`系统管理群`加入到`注册用户群`里，示例如下：  

``` java
private void groupDemo() {
    DroiGroup adminGroup = queryGroup("Administrator");
    DroiGroup userGroup = queryGroup("User");

    userGroup.addGroup(adminGroup.getObjectId());
    userGroup.save();
}

private DroiGroup queryGroup(String name) {
    DroiQuery query = DroiQuery.Builder.newBuilder().cloudStorage().query(DroiGroup.class)
        .where("Name", DroiCondition.Type.EQ, name).build();
    List<DroiGroup> list = query.runQuery(null); //传入 null 表示忽略错误
    if (list.size() > 0)
        return list.get(0);
}
```

## <a id="permission"></a>权限管理

权限是控制谁可以做什么事，谁不能做什么事。所以在介绍权限前，先说明用户和群组这两个权限依附的对象。在 DroiBaas，可以设置权限的目标为 DroiObject。细分的话，可以设定 DroiObject 这个表存取控制权限和表内每一条数据的存取控制权限。不过在 DroiBaaS SDK 内无法设置数据表存取权限，需到 Droi 开发平台上面设置。  
存取控制可分别设置读取权限和写入权限，所以会有以下四种情況。  

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

``` java
DroiPermission permission = new DroiPermission();

// 设置所有用户的权限是只读
permission.setPublicReadPermission(true);
permission.setPublicWritePermission(false);

// 设置目前登录的用户权限是可读可写
DroiUser user = DroiUser.getCurrentUser();
permission.setUserReadPermission(user.getObjectId(), true);
permission.setUserWritePermission(user.getObjectId(), true);

// 设置 User 群组的权限是可写不可读
DroiGroup userGroup = queryGroup("User");
permission.setGroupReadPermission(userGroup.getObjectId(), false);
permission.setGroupWritePermission(userGroup.getObjectId(), true);
```

### 设置默认权限

操作应用程序时，需要储存数据到云端时，默认每一条 DroiObject 数据都会加入目前登录用户可以读写。这个默认权限是可以改变的。  

``` java
DroiPermission permission = DroiPermission.getDefaultPermission();
if (permission == null)
    permission = new DroiPermission();

// 设置默认权限为所有用户可读不可写   
permission.setPublicReadPermission(true);
permission.setPublicWritePermission(false);

DroiPermission.setDefaultPermission(permission);
```

## <a id="object"></a>DroiObject权限

从一开始介绍了存取控制的依赖对象（用户、群组），跟着介绍了存取控制的权限本身（DroiPermission），最后要说明的是存取控制对象 - DroiObject  

DroiBaaS SDK 提供了完整的存取控制 API 让开发者能够弹性使用。开发者可以不用写一行存取控制的程序代码即可以有基本的权限保护，数据都只有其拥有者可以存取。

以下是云端数据存取控制相关 API 介绍。

### 设置权限

``` java
DroiUser currentUser = DroiUser.getCurrentUser();

// 设置权限为所有用户只读，拥有者可读可写
DroiPermission permission = new DroiPermission();
permission.setPublicReadPermission(true);
permission.setUserReadPermission(currentUser.getObjectId(), true);
permission.setUserWritePermission(currentUser.getObjectId(), true);

MyObject myObject = new MyObject();
myObject.stringField = "Object field name";
myObject.setPermission(permission);

myObject.save();
```

### 默认权限

在未设置 DroiObject 的 Permission 时，SDK 会自动设置默认权限，默认权限可由 `DroiPermission.setDefaultPermisson` 设置。默认值是目前登入用户可读可写。
