# DroiOauth  

## 简介

### 卓易授权是什么
卓易授权(DroiOauth)是依托卓易市场、FreeMeOS庞大用户群体和海量优势资源，使得广大合作伙伴和第三方开发者接入卓易账号功能后能够轻松享有卓易产品中的海量用户资源。用户可以方便快捷地使用卓易授权登录卓易账号系统，同时为用户带来统一、友好的登录体验。


## 安装

### 开发环境  

操作系统：Windows、Linux、Mac等；  
开发工具：Eclipse 3.x 及以上版本或Android Studio，Android API15及以上版本；  
支持设备：Android模拟器或手机(支持Android API15及以上版本)；  

### 快速入门

由于卓易授权(DroiOauth)SDK基于卓易CoreSDK，所以请在安装卓易授权SDK之前仔细阅读[快速入门](http://baastest.droi.cn/Index/docStart.html) 。


### Eclipse环境集成  

下载[SDK压缩包]()，解压后将DroiOauthDemo工程libs下的jar包文件复制到项目工程的libs目录下；右键工程根目录，选择`Properties` -> `Java Build Path` -> `Libraries`，然后点击`Add External JARs...` 选择指向jar的路径，点击OK，即导入成功。`（ADT17及以上不需要手动导入）`

### Android Studio环境集成 
Android Studio环境下只需要在Project的`build.gradle`中添加如下依赖：

	dependencies {
		/*其他依赖 */
		compile 'com.droi.sdk:oauth:1.0.001'
		compile 'com.android.support:support-v4:23.3.0'
	}

### 组件配置(AndroidManifest.xml配置)

    <!-- 账号登录、注册和找回密码界面Activity -->
    <activity
        android:name="com.droi.sdk.oauth.ui.AuthActivity"
        android:configChanges="keyboardHidden|orientation|screenSize" >
    </activity>


**注意：具体配置可以参考zip包内Demo的清单文件**


## 使用

登录授权的开放接口类为DroiOauth。开发者可以根据具体的使用场景和需求，调用其中的接口，实现包括语言定制、界面主色调定制、登陆授权以及获取账号信息在内的功能。

### 初始化  

在Application的`onCreate()`方法中初始化DroiOauth。

	DroiOauth.initialize(getApplicationContext());

**注意：DroiOauth初始化方法必须在其他方法之前调用，否则会抛出异常**

### 语言设置  

  卓易授权SDK支持界面语言的定制化，通过SDK语言设置接口指定页面的语言类别，支持的语言包括中`中文(zh_CN)`、`英文(en_US)`等。

	DroiOauth.setLanguage("zh_CN");

### 登录授权 

  账号SDK仅支持**Implicit Grant**授权模式。账号登录回调提供两个方法，分别对应登录成功和失败，登录成功返回token和openId等信息，否则返回失败信息。

	DroiOauth.requestTokenAuth(MainActivity.this, new DroiAccountCallBack() {
		    @Override
		    public void onSuccess(String data) {
		        // 登录成功执行回调
			    // data 包含access_token等信息
		    }

		    @Override
		    public void onError(String data) {
		        // data 登录失败执行回调
		    }
		});

登陆成功后返回登录信息Json串，其中各字段及含义如下：

| 字段名称 | 类型 | 字段含义   |
| --------  | ----- | -----  |
| result | int | 登录结果代码   |
| desc   | String | 登录结果描述 |
| expire | long | token有效截止时间，超出token失效   |
| openid | String | 唯一标识应用户身份的id   |
| token  | String | 授予登录账号的令牌字段  |

### 获取账号信息  

账号登录成功后，可以通过调用getAccountInfo方法来获取账户相关信息，其中Scope枚举指定获取信息类型，如下所示：

| Scope      | 含义   |
| --------   | -----  |
| Scope.USERINFO   | 获取账户基本信息 |
| Scope.USERPHONE  | 获取账户电话信息   |
| Scope.USERMAIL   | 获取账户邮箱信息   |

getAccountInfo方法需要传递scope数组，指定获取那些账号信息，可以一次传递多个类型，同时获取多种信息。

getAccountInfo方法传入GetAccountInfoCallBack回调对象，其中返回OauthError对象用于表示获取是否成功。调用OauthError相关方法获取执行成功与否信息，如下：

    // 获取信息操作是否成功
    boolean success = error.isOk();  
    
    // 获取操作失败信息
    String message = error.getDescMessage();


如下为获取所有账号信息示例代码：  

	DroiOauth.getAccountInfo(mOpenId, mToken, new Scope[]{Scope.USERINFO, Scope.USERPHONE, Scope.USERMAI}, new GetAccountInfoCallBack() {
        @Override
        public void onGetAccountInfo(final OauthError error, final String data) {
            // TODO Auto-generated method stub
			// OauthError封装操作是否成功以及相关提示语句
			// data为操作结果。若OauthError的isOk()返回true, 则data为包含用户信息的json串，否则为null
			if(error != null){
			    if(error.isOk()){//操作成功
				    //data为返回账号信息
				}else{//操作失败, data内容无效
				    //error.getDescMessage()为错误信息
				}
			}
        }
    });  

**注意：后台创建应用时必须申请以上账号信息权限，若未申请，则获取对应信息失败**  
	
GetAccountInfoCallBack回调data参数为账户信息Json字符串，解析后获取账号信息，以下为各字段含义：

| 字段名称 | 范围 | 字段含义 |
| -------- | -----  | -----  |
| result   |   int   | 执行结果返回码，为0表示返回正常，否则为错误代码 |
| desc      |   String   | 执行结果描述 |
| gender   |   int   |   性别(0-男，1-女，2-保密)   |
| nickname |   String   |    账户昵称   |
| username |   String   |    用户手机号码   |
| mail     |   String   |    用户邮箱   |
| age      |   int   |    年龄   |
| birthday |   String   |    生日   |

获取账号信息失败，result字段则为对应错误码，以下为常见错误码的含义：

| 错误码        | 含义   |
| -------- | -----  |
| -10300   | 未获取到会话信息 |
| -10301   | id不符合会话记录 |
| -10302   |    权限不符   |
| -10303   |    未找到用户信息   |
| -10304   |    非法的权限   |
| -10020   |    会话不存在   |
| -10021   |    token不存在或已过期   |
| -10010   |    用户不存在   |

### 查询Token有效性  

DroiOauth提供查询token有效性接口，用于判断token是否失效，示例代码如下：

    DroiOauth.checkTokenExpire(mToken, new CheckTokenStateCallback() {
        @Override
        public void onTokenCheckResult(final OauthError oauthError, final String data) {
            // oauthError 包含SDK执行的错误码和出错信息
            // data 为查询结果Json字符串，含有有效时间信息
        ｝
    });

其中data包含token有效信息，字段如下所示：

| 字段名称 | 类型 | 字段含义   |
| --------  | ----- | -----  |
| result | int | 执行结果代码，包括错误码   |
| desc   | String | 执行结果描述，包括错误提示信息 |
| expire_in | long | token有效截止时间，超出token失效  |

若token失效，则data参数包含对应错误码及描述信息，如下：

| 错误码   | 含义   |
| -------- | -----  |
| -10020   | 会话不存在 |

### 快速授权登录   

卓易授权SDK内部封装了快速登录子模块。在FreeMe OS系统环境下，如果系统账号APK已经登录且支持快速登录，卓易授权SDK会自动跳转到快速授权登录页面，借助系统已登录账号实现快速登录。同时，还可选择切换账号，使用其他账号登录，如下所示：  

![cmd-markdown-logo](http://baastest.droi.cn/Uploads/DocFile/576367f885e00.png)


**注意：快速授权登录由系统账号APK是通过内部封装实现，无需调用API。**


