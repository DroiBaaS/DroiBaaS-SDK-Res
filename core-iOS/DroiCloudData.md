# 云存储
云存储（DroiCloudData）是DroiBaaS提供的云数据存储服务，它建立在对象DroiObject的基础上，每个DroiObject包含若干键值对。所有 DroiObject均存储在DroiBaaS云端，您可以通过DroiCore SDK对其进行操作，也可在DroiBaaS的后台中管理所有的对象。此外DroiBaaS 还提供一些特殊的对象，如DroiUser(用户)，DroiFile(文件)，他们都是继承于DroiObject的对象。  

以下将云存储开发相关技术区分为不同小节：   

* [云端数据存储](#DataStorage)：介绍如何使用DroiBaaS将对象数据上传至云端
* [数据表名](#DataTableName)：介绍通过DroiBaaS上传数据后实际在云数据库建立的表名
* [数据关联](#Reference)：介绍如何使用DroiBaaS完成对不同数据表进行数据关联
* [数据查询](#DataQuery)：介绍如何使用DroiBaaS提供的功能查询云数据库中的数据
* [云文件存储](#DroiFile)：介绍如何使用DroiBaaS对文件数据进行操作
* [应用程序参数配置](#DroiPreference)：介绍如何使用DroiBaaS对应用程式参数开发者进行动态配置
* [应用程序临时数据](#DroiCloudCache):介绍如何使用DroiBaaS提供的临时数据功能对需要临时保存且整个应用程式都需要使用的数据进行操作。

## <a id="DataStorage"></a>云端数据存储

DroiBaaS提供了最简便的方式去存储数据至云端或是由云端数据库下载数据。开发者可以使用以下几种方式：   

* [使用`DroiObject`存储数据](#DroiObjectSave)
* [继承`DroiObject`并扩充存储的对象属性 (推荐)](#DroiObjectExtension)

开发者可以依照实际的数据复杂度去决定使用哪种数据存储方式。
> DroiBaaS为了提高系统效率以便服务更多的用户，所以将云端数据读、写分离处理。在正常的情况下刚存入的数据立即可以抓取出來。但是在系统负载非常高的情况下，有可能会存在一定的延迟，但时差不会超过5S。

> ***注意***：DroiBaaS 为了提高系统性能以便服务更多的应用程序，所以将云端数据读取、写入分开处理。在正常的情况下刚存入的数据立即可以抓取出来。但是在系统负载非常高的情况下，最差的情况有可能有五秒的延迟。

### <a id="DroiObjectSave"></a>使用`DroiObject`存储数据

每个`DroiObject`对象在上传至云端前，都会将包含在`DroiObject`内的数据(key-value)转换为JSON数据。目前`DroiObject`支持的数据类型有

* `NSString`字符串
* `NSNumber`数字对象
* `NSArray`数组
* `NSDictionary`字典
* `NSDate`日期对象
* [`DroiFile`](#DroiFile)文件 
* `DroiObject`其他类型

至于键值(key)则为字符串格式。但某些特殊字符如`$`,`@`及`.`在DroiBaaS系统之中已有特殊使用，开发者禁止在键值之中包含这些字符。

示例：假设开发者需要将用户名称、密码以及性別等资料存放在DroiObject之中，可以使用如下的代码  

* Objective-C  

    ```objc	
	// 建立DroiObject，使用的数据表名为UserData
	DroiObject* userData = [DroiObject createWithClassName:@"UserData"];
    
	// 依次将名称，密码及性別等数据放入userData之中
	[userData putKey:@"name" andValue:@"Droi"];
	[userData putKey:@"password" andValue:@"xxxxx"];
	[userData putKey:@"gender" andValue:@"Male"];
   
	// 上传user data至云端数据库
	[userData save];
    ```

* Swift

    ```swift
	// 建立DroiObject，使用的数据表名为UserData
	let userData : DroiObject = DroiObject.createWithClassName("UserData")
        
	// 依次将名称，密码及性別等数据放入userData之中
	userData.putKey("name", andValue: "Droi")
	userData.putKey("password", andValue: "xxxxx")
	userData.putKey("gender", andValue: "Male")
        
	// 上传user data至云端数据库
	userData.save() 
    ```  
  > ***注意*** ```DroiObject:save```为同步方法(synchronous)会等到数据上传云端后才会执行完毕。如需要使用异步方法(asynchronous)可以使用```DroiObject:saveInBackground:```配合回调Block(callback method)来达到不阻塞当前线程的目的。[DroiBaaS][droibaas]內所有的方法都提供了相应异步方法，如需要避免阻塞当前线程，可以使用[Method Name]**InBackground**形式的方法。
  
当数据属性不多的时候可以直接使用`DroiObject`配合`putKey:andValue:`及`getValue:key:`方法将数据由`DroiObject`之中存入或取出。但当数据属性太多时，使用这种方式在代码的阅读及使用上又不方便。[下节](#DroiObjectExtension)介绍继承`DroiObject`并扩充存储资料属性的方式。


### <a id="DroiObjectExtension"></a>继承`DroiObject`并扩充存储数据属性

当数据属性个数很多的情況时，直接使用`DroiObject`去存取数据在使用上非常的不方便。[上节](#DroiObjectSave)的代码可以改成

* Objective-C

    ```objc
	// 声明UserData继承DroiObject，系统预设会以类名作为数据表名
	@interface UserData : DroiObject

	DroiExpose	// 设定下列属性需要存储
	@property NSString* name;   	// 用户名称

	DroiExpose
	@property NSString* password;   //　密码

	DroiExpose
	@property NSString* gender; 	// 性別

	@property NSString* other;		// 其他属性
	@end    
	
	
	...
	
	// 1. 创建UserData对象
	UserData* userData = [[UserData alloc] init];
   
	// 2. 设定相关名称、密码及性別数据 
	userData.name = @"Droi";
	userData.password = @"xxxxx";
	userData.gender = @"Male";
   
	// 3. 上传至云端数据库 
	[userData save];
    ```  

* Swift

    ```swift
	// 声明UserData继承DroiObject，系统预设会以类名作为数据表名
	class UserData : DroiObject {
		// DroiExpose
		var name : String!		// 用户名称
    
		// DroiExpose
		var password : String!	//　密码
    
		// DroiExpose
		var gender : String!	// 性別
    
		var other : String!		// 其他属性
	}
		
	...
	
	// 1. 创建UserData对象
	let userData = UserData()
   
	// 2. 设定相关名称、密码及性別数据  
	userData.name = "Droi"
	userData.password = "xxxxx"
	userData.gender = "Male"
   
	// 3. 上传至云端数据库
	userData.save()
    ```

  > ***注意*** ```DroiObject.save()```为同步方法(synchronous)会等到数据上传云端后才会执行完毕。如需要使用异步方法(asynchronous)可以使用```DroiObject.saveInBackground()```配合回调Block(callback method)来达到不阻塞当前线程的目的。[DroiBaaS][droibaas]內所有的方法都提供了相应异步方法，如需要避免阻塞当前线程，可以使用[Method Name]**InBackground**形式的方法。

首先声明一个`UserData`类继承自`DroiObject`，并将需要存储的数据属性上增加标记`DroiExpose`(Objective-C)或是`// DroiExpose`(Swift)，DroiBaaS在存储数据时会自动将已标记的数据属性存储下来。  
最后如同使用正常的属性一样存取相关的数据属性，当数据属性设置完成后，开发人者可以使用`DroiObject:save`将数据存储至云端。  
上面代码中，标记`DroiExpose`代表需要将正下方的属性上传至云端。至于属性`other`由于没有添加标记`DroiExpose`在上方，所以在上传数据至云端时不会将`other`的数据上传。

## <a id="DataTableName"></a>数据表名 

### 预设数据表名

数据表名代表数据存储在云端之中显示出来的名称。如果开发者直接使用``DroiObject`存储数据时，DroiBaaS会以`DroiObject:createWithClassName:`中的`className`作为表名。下面例子，DroiBaaS会以`table_Name`作为`obj`数据表名。

* Objective-C

    ```objc
    DroiObject* obj = [DroiObject createWithClassName:@"table_Name"];
    ```
* Swift

    ```swift
    let obj : DroiObject = DroiObject.createWithClassName("table_Name")
    ```

如果开发者是以继承`DroiObject`的方式创建对象，DroiBaaS会以相应的对象名作为数据表名。如下所示，DroiBaaS会以`UserData`作为数据表名。

* Objective-C

    ```objc
    @interface UserData : DroiObject
    ```
* Swift

    ```swift
    class UserData : DroiObject 
    ```

### 自定义数据表名
当开发者开发不同平台的应用时需要自定义数据表名，让不同平台的数据表名相同，以便达到不同平台的应用可以共享同一份的数据表格。这时就可以利用标记`DroiObjectName`的方式来达到自定义数据表名的目的。  

* Objective-C

    ```objc  
	DroiObjectClass(@"Table_Name")
	@interface UserData : DroiObject
    ```  
* Swift

    ```swift
	// DroiObjectClass("Talbe_Name")
	class UserData : DroiObject 
    ```

上面的例子会让数据存储直接使用`Table_Name`这一个数据表名，而不是使用`UserData`这一预设的数据表名。开发者可以通过这种方式让DroiBaaS不使用类名作为数据表名。

## 数据关联<a id="Reference"></a>
DroiBaaS在默认配置的情况下，一个类中包含其他`DroiObject`的子类对象时，会直接将该对象转换成JSON数据并存储下来。但在某些数据库上使用时，需要有外部引用(reference)数据的功能。DroiBaaS提供两种方式实现外部数据关联的功能。　　

* [使用`DroiReferenceObject`](#DroiReferenceObject)
* [使用`DroiReference`标记](#DroiReference)

其中使用`DroiReference`标记只适用于`DroiObject`的子类。下面会详细说明使用方式。  

> ***注意***：由于查询性能上的限制，使用外部引用无法作为数据查询的条件。如需要将外部引用作为其中一個查询条件，请先将符合条件的外部引用查询出来再使用`IN`条件判断来查询结果。

### <a id="DroiReferenceObject"></a>使用`DroiReferenceObject`
使用`DroiReferenceObject`对象可以直接使用该对象的`droiObject`属性设置`DroiObject`给此对象使用，当对象需要存储至云端时，DroiBaaS会将引用到的对象存储在另一个数据表格内。其设计概念与数据库开发中的外部数据关联是相同的。　　

以下简单的代码中，主要是声明了Author的对象并使用一个`DroiReferenceObject`对象引用到该对象，并直接将`DroiReferenceObject`放置到Book的对象之中。当此Book对象上传至云端时，系统会建立两个数据表格（***Author***及***Book***)，由于***作者(Author)***的资料在***书籍(Book)***之中是使用了引用的方式，所以当***作者(Author)***数据表格中的某项数据有修改时候，所有引用到该数据的***书籍(Book)***数据都会一并更新。  

这在实际开发中有很大的作用，如果开发者并不是使用引用的方式，每本书的***作者(Author)***数据都直接将作者资料单独存储下來。当有某位作者的个人资料需要变动时，需要将所有的***书籍(Book)***表格内的此位作者的资料都一并更新，这个操作将会耗费非常大的资源。  

假设开发者是使用引用的方式来存储资料，只需要修改一份***作者(Author)***内的数据即可。这可以省去大量的数据更新量及时间。

* Objective-C

	```objc
	// 创建作者对象
	DroiObject* author = [DroiObject createWithClassName:@"Author"];
	[author putKey:@"name" andValue:@"authorName"];       // 设定作者名称
	[author putKey:@"phoneNumber" andValue:@"00000000"];  // 设定作者电话号码
	
	// 使用DroiReferenceObject设定外部引用
	DroiReferenceObject* refAuthor = [DroiReferenceObject new];
	refAuthor.droiObject = author;               // 指向author对象
	
	// 设置书籍对象
	DroiObject* book = [DroiObject createWithClassName:@"Book"];
	[book putKey:@"author" andValue:refAuthor];  // 设定作者（外部引用）
	[book putKey:@"name" andValue:@"bookName"];  // 设定书名
	[book putKey:@"price" andValue:@130];        // 设定价格
	```

* Swift

	```swift
	// 创建作者对象
	let author : DroiObject = DroiObject.createWithClassName("Author");
	author.putKey("name", andValue: "authorName")      // 设定作者名称
	author.putKey("phoneNumber", andValue: "00000000") // 设定作者电话号码
        
	// 使用DroiReferenceObject設定外部參照
	let refAuthor : DroiReferenceObject = DroiReferenceObject()
	refAuthor.droiObject = author              // 指向author对象
    
	// 设置书籍对象    
	let book : DroiObject = DroiObject.createWithClassName("Book")
	book.putKey("author", andValue: refAuthor) // 设定作者（外部引用）
	book.putKey("name", andValue: "bookName")  // 设定书名
	book.putKey("price", andValue: 130)        // 设定价格
    ```
    
### <a id="DroiReference"></a>使用`DroiReference`标记
此功能目前只支持`DroiObject`的子类使用。开发者只需要使用`DroiReference`标记就可以通过外部关联的方式存储数据到在不同的表格之中。下面是一个简单的例子

* Objective-C

    ```objc
	// 声明作者类继承DroiObject
	@interface Author : DroiObject
	
	DroiExpose
	@property NSString* name;		// 作者姓名
	
	DroiExpose
	@property NSString* address;    // 作者地址
	
	DroiExpose
	@property NSString* phoneNumber;// 作者电话
	end
	
	
	// 声明书籍类继承DroiObject
	@interface Book : DroiObject
	
	DroiReference
	@property Author* author;	// 作者
	
	DroiExpose
	@property NSString* name;	// 书名
	
	DroiExpose
	@property NSString* ISBN;	// ISBN
	
	DroiExpose
	@property int price;		// 价格
	@end
    ```
  
* Swift

    ```swift
  	// 声明作者类继承DroiObject
	class Author : DroiObject {
		// DroiExpose
		var name : String!;			// 作者姓名
    
		// DroiExpose
		var address : String!;		// 作者地址
    
		// DroiExpose
		var phoneNumber : String!;	// 作者电话
    }

	// 声明书籍类继承DroiObject
	class Book : DroiObject {
		// DroiReference
		var author : Author!;	// 作者
    
		// DroiExpose
		var name : String!;		// 书名
    
		// DroiExpose
		var ISBN : String!; 	// ISBN
    
		// DroiExpose
		var price : Int!;		// 价格
	}
    ```
在类``Book`中使用`DroiReference`将author设定为关联数据。当`Book`对象存储至云端时，DroiBaaS会建立两个数据表格(Author及Book)。其中`author`数据会存储在`Author`表格之中，而原来在`Book`中的`author`属性则会存`objectId`这个外部主键值。未来只要更新`Author`表格内的数据就会一并更新所有用到同份数据的`Author`对象。  
反之，若开发者没有使用`DroiReference`，DroiBaaS会直接将author的数据存储下来。由于没有使用另一份数据表格存储资料，这就没办法只更新`Author`表格的数据使所有引用到同一个作者的数据都更新。

## <a id="DataQuery"></a>数据查询 
数据存储至云端后，必须有一个方式可以将数据查询回來使用。DroiBaaS提供`DroiQuery`来完成数据查询的动作。开发者可以使用`DroiQuery`配合一些查询条件将数据查询回来使用。目前`DroiQuery`提供的功能有：

* 多条件查询
* 支持属性排序及设定回传数据的起始位置(Offset)
* 通过[CLI (Command-line interface)](#APT)工具可产生查询代码，减少开发者编写查询代码的时间

以下就简单介绍一下如何使用`DroiQuery`的查询功能。  

> ***注意***：为了增加系统的执行效率。每次数据查询一次回传的数据个数为1000个（预设值）。开发者可以limit去减少回传数量；如果希望去得到1000个以后的数据，开发者可以使用`offset`方法来变更回传数据其实位置。


### <a id="NoAPT" ></a>使用`DroiQuery`查询数据
DroiBaaS提供开发者使用`DroiQuery`查询已存储的数据。开发者必须设置数据表名（或是直接引入对象）以及额外的查询条件（选用），即可将数据由云端查询回本地使用。以下是一个简单的例子

* Objective-C

	```objective-c
	// 创建一个查询条件(price = 70)
	DroiCondition* cond = [DroiCondition cond:@"price" andType:DroiCondition_EQ andArg2:@70];

	// 创建一个查询对象
	DroiQuery* query = [[DroiQuery create] queryByClass:Book.class];// 由Book数据表格查询
	query = [query whereStatement:cond];							// 加入查询条件 price = 70
	query = [query orderBy:@"ISBN" ascending:NO];					// 加入排序結果. 按照ISBN排序
	
	// 开始查询并回调查询结果
	DroiError* error;
	NSArray* result = [query runQuery:&error];
	if (error.isOk) {
	   // 成功！
	}
    ```  

* Swift

	```swift
	// 创建一个查询条件(price = 70)
	let cond : DroiCondition = DroiCondition.cond("price", andType: DroiCondition_EQ, andArg2: 70)
   
	// 创建一个查询对象     
	let query : DroiQuery = DroiQuery.create()
		.queryByClass( Book.self )			// 由Book数据表格查询
		.whereStatement( cond )				// 加入查询条件 price = 70
		.orderBy("ISBN", ascending: false)	// 加入排序結果. 按照ISBN排序
        
	// 开始查询并回调查询结果
    var error: DroiError? = nil;
    let result = query.runQuery(&error);
    if error!.isOk() {
        // 成功！
    }
	```

其中创建查询条件时使用的`DroiCondition`类，它提供了简单的条件设定以及复杂的查询条件(AND及OR)组合。目前`DroiCondition`提供的查询条件有：

<center>

| 变量名称　| 说明　|
|:---------|---------|
| **LT** | Less Than(<)，小于 |
| **LT\_OR\_EQ** | Less Than or Equal (<=)，小于或等于 |
| **EQ** | Equal (=)，等於 |
| **NEQ** | Not Equal (<>)，不等于 |
| **GT** | Greater Than(>)，大于 |
| **GT\_OR\_EQ** | Greater Than or Equal (>=)，大于或等于 |
| **ISNULL** | IS NULL，是否为空(NULL) |
| **ISNOTNULL** | Is Not NULL，是否不为空(NOT NULL) |
| **CONTAINS** | Contains, 包含 |
| **NOTCONTAINS** | Not Contains, 不包含 |
| **STARTSWITH** | Starts With, 开头是否为指定的字符串 |
| **NOTSTARTSWITH** | Not Starts With, 开头是否不为指定的字符串 |
| **ENDSWITH** | Ends With, 结尾是否为指定字符串 |
| **NOTENDSWITH** | Not Ends With, 结尾是否不为指定的字符串 |
| **IN** | IN, 数据是否符合所传入的列表。如```DroiCondition.cond("price", DroiCondition.Type.IN, "70, 80");```，代表欲查price符合70或80的数据 |
| **NOTIN** | NOT IN，数据是否不符合所传入的列表 |

</center>

#### 复杂的查询条件
开发者如果需要使用较为复杂的查询条件，同样可以使用`DroiCondition`类来达到需求。假设一个情景，需要查询书名中有包含**'DIY'**且price小于80或是书名有包含**'HOW'**且price小于30的查询条件。我们可以写成：

* Objective-C

	```objective-c
	// 创建书名包含DIY，且price小于80的查询条件
	DroiCondition* cond1 = [[DroiCondition cond:@"name" andType:DroiCondition_CONTAINS andArg2:@"DIY"]
		and:[DroiCondition cond:@"price" andType:DroiCondition_LT andArg2:@80]];

	// 创建书名包含HOW，且price小于30的查询条件
	DroiCondition* cond2 = [[DroiCondition cond:@"name" andType:DroiCondition_CONTAINS andArg2:@"HOW"]
                            and:[DroiCondition cond:@"price" andType:DroiCondition_LT andArg2:@30]];
                            
	// 将上述两个条件组合
	DroiCondition* cond = [cond1 or:cond2];
    ```

* Swift

	```swift
	// 创建书名包含DIY，且price小于80的查询条件
	let cond1 : DroiCondition = DroiCondition.cond("name", andType: DroiCondition_CONTAINS, andArg2: "DIY")
            .and( DroiCondition.cond( "price", andType: DroiCondition_LT, andArg2: 80) )
   
	// 创建书名包含HOW，且price小于30的查询条件    
	let cond2 : DroiCondition = DroiCondition.cond("name", andType: DroiCondition_CONTAINS, andArg2: "HOW")
            .and( DroiCondition.cond( "price", andType: DroiCondition_LT, andArg2: 30) )
        
	// 将上述两个条件组合
	let cond : DroiCondition = cond1.or( cond2 )
    ```
	
到这里是不是觉得虽然DroiBaaS有提供复杂的查询条件功能，但是在使用上所需要编写的代码非常多？为了减轻开发者的工作，DroiBaaS 使用CLI tool来有效的规避编写代码量过多的问题。[下节](#APT)会快速的说明这种方式的使用。

### <a id="APT" ></a>使用CLI（Command-line Interface)工具生成代码辅助查询
DroiBaaS提供CLI工具以简化开发者在编写复杂条件时的工作。具体的安裝方式可以参考[快速入门][apt_ref]。安装完成后，开发者只需要在需要生成代码的类的声明中加入标记`DroiQueryAnnotation`，CLI即会自动生成该类别的查询代码。以之前的例子来说。我们加入`DroiQueryAnnotation`在类别`Book`声明之上，代码如下：

* Objective-C

	```objective-c
    // 声明Book类
    DroiQueryAnnotation
    @interface Book : DroiObject
    //...
    ```

* Swift

	```swift
    // 声明Book类
    // DroiQueryAnnotation
    class Book : DroiObject 
    //...
	```
	
加入标记后请执行**Make Build**，编辑器即会呼叫CLI去生成相对应的代码。`QBook`(命名方式为原来的类名前面加上**Q** `Objective-C`或是**QSwift** `Swift`)。[上节](#NoAPT)的复杂查询条件则可以简单写成

* Objective-C

	```objective-c
    // ( name包含"DIY" 且 price < 80 )
    DroiCondition* cond1 = [([[QBook name_contains:@"DIY"] and:[QBook price_lt:@80]])
		or:([ // 或
			// ( name包含"HOW" 且　price < 30 )
			[QBook name_contains:@"HOW"] and:[QBook price_lt:@30]])];
    ```

* Swift

	```swift
	// ( name包含"DIY" 且 price < 80 )
    let cond : DroiCondition = (QSwiftBook.name_contains("DIY").and( QSwiftBook.price_lt(80)))
		.or( // 或
			// ( name包含"HOW" 且　price < 30 )
			QSwiftBook.name_contains("HOW").and( QSwiftBook.price_lt(30)) );
    ```

CLI工具会根据开发者的类的属性去产生相对应的代码。这样可以有效地降低开发者在编写查询条件代码的难度。

## <a id="DroiFile"></a>DroiFile

云端数据是数据的集合，其实可想像成云端上的数据库。有时候想要操作大数据或者文件时，如果直接放进云端的话，会造成每次查询都有大量的数据传输，而且如果又是多笔数据的话，数据量会更大，所以文件通常都会与云端数据分开管理。但分开管理又会让操作不方便。所以，我们提供了 DroiFile，让开发者可以操作文件上传下载，而且允许与 DroiObject 关联在一起，只要声明 DroiFile 是 DroiObject 内的一个 Expose 属性。

***注意*** `目前上传文件大小限制为 14 MB`

* Objectice-C

    ```objc
    @interface MySongs : DroiObject
    DroiExpose
    @property NSString* Name;
    
    DroiExpose
    @property DroiFile* Cover;
    @end
    ```
    
* Swift

    ```swift
    class MySongs : DroiObject {
        // DroiExpose
        var Name: String = ""
        
        // DroiExpose
        var Cover: DroiFile!
    }       
    ```

### 上传文件

因为 DroiFile 是 DroiObject 内的一个属性 ，所以最终是通过 `DroiObject.save`，SDK 会先上传 DroiFile，成功后，再把云端数据存储。

* Objective-C

    ```objc
    DroiFile* cover = [DroiFile fileWithFileName:@"my_heart.jpg"];
    
    MySongs* song = [MySongs new];
    song.Name = @"My heart will go on";
    song.Cover = cover;
    
    [song save];
    ```
    
* Swift

    ```swift
    let cover = DroiFile(fileName: "my_heart.jpg")
    
    let song = MySongs()
    song.Name = "My heart will go on"
    song.Cover = cover
    
    song.save()
    ```

### 下载文件

DroiFile 是属于 DroiObject 的一部分，所以在下载文件前，需先把 DroiObject 查询回来。

* Objective-C

    ```objc
    DroiQuery* query = [DroiQuery create];
    NSArray* list = [[[query cloudStorage] queryByClass:[MySongs class]] runQuery];
    MySongs* song = [list objectAtIndex:0];
    
    DroiGetFileResult* result = [song.Cover get:nil]; // 传入 nil 表示忽略错误
    if (result.code == GETFILE_OK) {
        NSString* name = song.Cover.name;
        [result.data writeToFile:name atomically:YES];
    }
    ```
    
* Swift

    ```swift
    let query = DroiQuery.create().cloudStorage().queryByClass(MySongs.self)
    let list = query.runQuery() as! [MySongs]!
    let song = list[0]
    
    let result = song.Cover.get(nil) // 传入 nil 表示忽略错误
    if result.code == GetFileCode.GETFILE_OK {
        let name = song.Cover.name
        result.data.writeToFile(name, atomically: true)
    }
    ```

### 上传/下载进度

DroiBaaS SDK 提供了上传/下载进度的 callback，让开发者可以加强 UI 操作流程。

#### 上传

* Objective-C

    ```objc
    DroiFile* cover = [DroiFile fileWithFileName:@"pokerface.jpg"];
    [cover setSaveProcessListener:^(long current, long max) {
        // from 0 to file size
    }];
    
    MySongs* song = [MySongs new];
    song.Name = @"Poker face";
    song.Cover = cover;
    [song save];
    ```
    
* Swift

    ```swift
    let cover = DroiFile(fileName: "pokerface.jpg")
    cover.setSaveProcessListener({current, max in
        // from 0 to file size
    })
    
    let song = MySongs()
    song.Name = "Poker face"
    song.Cover = cover
    song.save()
    ```

#### 下载

* Objective-C

    ```objc
    DroiQuery* query = [DroiQuery create];
    NSArray* list = [[[query cloudStorage] queryByClass:[MySongs class]] runQuery:nil];
    MySongs* song = [list objectAtIndex:0];
    
    [song.Cover getInBackground:^(NSData* data, DroiError* error) {
        // download complete
    } progressCallback:^(long current, long max) {
        // progress
    }];
    ```
    
* Swift

    ```swift
    let query = DroiQuery.create().cloudStorage().queryByClass(MySongs.self)
    let list = query.runQuery(nil) as! [MySongs]!
    let song = list[0]
    
    song.Cover.getInBackground({
        (data: NSData!, error: DroiError!) in
        // Download complete
    }, progressCallback: {
        current, max in
        // Progress
    })
    ```

## <a id="DroiPreference"></a>DroiPreference

DroiBaaS SDK 提供了一个全局只读的设置可让应用使用，这个设置是以 Key/Value 的形式。开发者可在web后台页面设置DroiPreference，在应用里面读取。

这可以用作应用的即时调整。

### 使用示例

在 `Core.initialize` 时，SDK 会后台下载设置。之后就不会再更新了，开发者可以呼叫 `refresh` 来更新设置。

* Objective-C

    ```objc
    if ([DroiPreference preference].isReady) {
        int intValue = [[[DroiPreference preference] valueForKey:@"IntKey"] intValue];
        long longValue = [[[DroiPreference preference] valueForKey:@"LongKey"] longValue];
        BOOL boolValue = [[[DroiPreference preference] valueForKey:@"BoolKey"] boolValue];
        double doubleValue = [[[DroiPreference preference] valueForKey:@"DoubleKey"] doubleValue];
        NSDictionary* jsonValue = [[DroiPreference preference] valueForKey:@"JsonKey"];
        NSString* strValue = [[DroiPreference preference] valueForKey:@"StrKey"];
    }
    ```
    
* Swift

    ```swift
    if DroiPreference.instance().isReady {
        let intValue = (DroiPreference.instance().valueForKey("IntKey") as! NSNumber!).intValue
        let longValue = (DroiPreference.instance().valueForKey("LongKey") as! NSNumber!).longValue
        let boolValue = (DroiPreference.instance().valueForKey("BoolKey") as! NSNumber!).boolValue
        let doubleValue = (DroiPreference.instance().valueForKey("DoubleKey") as! NSNumber!).doubleValue
        let jsonValue = (DroiPreference.instance().valueForKey("JsonKey") as! NSDictionary!)
        let strValue = (DroiPreference.instance().valueForKey("StrKey") as! String)
    }
    ```
    
## <a id="DroiCloudCache"></a>DroiCloudCache应用程序临时数据

当开发者需要存储应用程序临时数据时，DroiBaaS提供了`DroiCloudCache`来达到上述的功能。这类功能常常用于记录电商类应用购物车数据。请想象一下，有位应用程序的用户在A手机选择了几种商品放置至购物车之中，他可以利用`DroiCloudCache`这一功能在B手机显示出刚才加入购物车的商品。但是这些数据不需要实际存储于云数据库中。这大大减少应用程序对云数据库的容量需求，而且也可以做到不同设备间的数据交换的目的。　　

`DroiCloudCache`与`DroiObject`数据最大的不同点在于由`DroiCloudCache`所存储的资料并不会真正存储于云数据库之中，它的数据是暂存在云端系统的内存之中。以下表格是两者的简单比较：  

<center>|  	| DroiCloudCache 	| DroiObject 	||--------------	|:----------------:|:------------:|| 使用云端数据库 	| 否 	| 是 	|| 可储存数据量 	| 较少(<500KB) 	| 大 	|| 数据是否会消失 	| 是 	| 否 	|| 数据形式 	| 键值(Key/Value) 	| 记录(Record)</center>  


由于此数据并不保证永久存在于系统之中，在实际操作时开发者需要注意这个数据类型的特性。请勿使用这一功能去存储一些重要的数据。　　
`DroiCloudCache`的数据格式是键值(Key/Value)的样式。一个Key字符串会对应一个Value的字符串。数据上传服务器后，任意的设备皆可以使用同样的键(Key)得到设定的数值(Value)。以下是简单的示例：  


* Objective-C

	``` objc
	// 设定数据。key:1234, value:54678
	[DroiCloudCache setKey:@"1234" andValue:@"54678"];
	
	// 从云端取回设定的数据资料
	DroiError* error;
	NSString* value = [DroiCloudCache getValueByKey:@"1234" error:&error];
	```
	
   
* Swift

    ``` swift
	// 设定数据。key:1234, value:54678
	DroiCloudCache.setKey("1234", andValue: "54678")
	
	// 从云端取回设定的数据资料
	var error:DroiError? = nil
	let value : String = DroiCloudCache.getValueByKey("1234", error: &error)
	 ```	
## 错误管理

为了让开发者可以作到统一的错误管理，DroiBaaS SDK 提供了 `DroiError` 这个错误类别，如果 SDK API 有错误，会回传 `DroiError`。另外有些 API 的回传值已经定义，也会将 DroiError 设置为最后一个参数回传错误码。另外，`DroiError` 也可以输出对应的错误信息，让开发者能更清楚的了解错误原因。

### DroiError 方法说明

#### isOk

让开发人员快速知道呼叫的 API 是否成功。

### DroiError 属性说明

#### code

取得错误码。需要注意的是 0 表示成功，当 isOk 為 true 時，错误码必定为 0。

#### message

输出错误信息

#### ticket

错误码的一部分

### DroiError 错误码明

|名称|值|说明|
|---|---|---|
|DROICODE_OK|0|API 执行成功|
|DROICODE\_UNKNOWN_ERROR|1070000|API 发生未知错误|
|DROICODE_ERROR|1070001|API 发生错误，详情请见错误信息|
|DROICODE\_USER\_NOT\_EXISTS|1070002|此用户不存在|
|DROICODE\_USER\_PASSWORD\_INCORRECT|1070003|用户名称或密码错误|
|DROICODE\_USER\_ALREADY\_EXISTS|1070004|此用户已存在|
|DROICODE\_NETWORK\_NOT\_AVAILABLE|1070005|网络连接出现问题|
|DROICODE\_USER\_NOT\_AUTHORIZED|1070006|此用户认证已失效，请重新登录|
|DROICODE\_SERVER\_NOT\_REACHABLE|1070007|无法连接服务器|
|DROICODE\_HTTP\_SERVER\_ERROR|1070008|服务器返回错误，详情请见错误信息|
|DROICODE\_SERVICE\_NOT\_ALLOWED|1070009|服务器拒绝服务|
|DROICODE\_SERVICE\_NOT\_FOUND|1070010|服务不存在|
|DROICODE\_INTERNAL\_SERVER\_ERROR|1070011|服务器发生错误|
|DROICODE\_INVALID_PARAMETER|1070012|API 参数输入错误|
|DROICODE\_NO\_PERMISSION|1070013|没有权限使用 API|
|DROICODE\_USER_DISABLE|1070014|此用户已被禁止使用|
|DROICODE\_EXCEED\_MAX\_SIZE|1070015|超出上限|
|DROICODE\_FILE_\NOT\_READY|1070016|文件尚未准备好|
|DROICODE\_CORE\_NOT\_INITIALIZED|1070017|必需在 DroiBaaS SDK 初始化后才能使用|
|DROICODE\_USER_CANCELED|1070018|用户取消操作|
[//]: #
	 
## 多线程开发指引

### DroiTaskDispatcher功能说明

[DroiBaaS][droibaas]提供开发者一个简单的工具去开发多线程apps。开发者可以直接使用```DroiTaskDispatcher```将```Task```放到指定的Dispatch Queue执行。简单的代码如下：

* Objective-C

	```objc
	// 得到后台Dispatch Queue实例
	DroiTaskDispatcher* taskDispatcher = [DroiTaskDispatcher getTaskDispatcher:BackgroundThreadDispatcher];
 
	// 将Task放置至指定Queue执行
	[taskDispatcher enqueueTask:^{
		// Do something
	}];

	```

* Swift

	```swift
	// 得到后台Dispatch Queue实例
	let taskDispatcher : DroiTaskDispatcher = DroiTaskDispatcher.getTaskDispatcher(BackgroundThreadDispatcher)

	// 将Task放置至指定Queue执行 
	taskDispatcher.enqueueTask { 
		// Do something
	}
	```

其中```BackgroundThreadDispatcher ```是一个```DroiTaskDispatcher```定义的常量，开发者可以自定义执行线程的名称。如果希望放置一个每秒执行一次的```Task```可以使用如下的代码：    

* Objective-C

	```objc
	// 得到后台Dispatch Queue实例
	DroiTaskDispatcher* taskDispatcher = [DroiTaskDispatcher getTaskDispatcher:BackgroundThreadDispatcher];

	// 将Task定时(每1000ms)在指定的线程执行 
	[taskDispatcher enqueueTimerTask:^{
		// 定时执行的Task
	} withInterval:1000 andName:@"TimerTask"];
	```

* Swift

	```swift
	// 得到后台Dispatch Queue实例
	let taskDispatcher : DroiTaskDispatcher = DroiTaskDispatcher.getTaskDispatcher(BackgroundThreadDispatcher)

	// 将Task定时(每1000ms)在指定的线程执行 
	taskDispatcher.enqueueTimerTask({ 
		// 定时执行的Task
		}, withInterval: 1000, andName: "TimerTask")
	```

其中第三个参数代表此```Task```的标记名称。开发者可以使用```- (BOOL)killTask:(NSString *)taskName```将已置入的```Task```刪除。如果已经有同名的```Task```已置入在执行队列（running queue)之中，```DroiTaskDispatcher```就不会将最后要置入的```Task```放置至执行队列之中。换句话说，在执行队列之中不能有其他同名的```Task```。

### DroiTask功能说明

虽然[DroiBaaS][droibaas]提供了```DroiTaskDispatcher```让开发者可任意将```Task```置入至另一个Dispatcher Queue之中。但在实际开发时，很多完整的流程是由许多小型的```Task```组合而成的。而且在执行完成之后，也可能需要触发一些```Callback Task```将执行的结果做出相应的动作。
为了这个目的，[DroiBaaS][droibaas]另外提供了```DroiTask```来实现此类的需求。 
 
#### ***连续性的```Task```串接***  
以下的代码将置入三个```Task```至```DroiTask```中并在线程```BackgroundThreadDispatcher```执行，每个```Task```执行后再延迟500ms执行下一个```Task```。此代码中在第二个```Task```就将这整个```DroiTask```通过```cancel```方法停止```DroiTask```执行。所以第三个```Task```并不会被。而在主线程则是使用```waitTask```方法去等待```DroiTask```的工作完成。开发者可以将每个```Task```重复使用并达到最好的利用效率。

* Objective-C

	```objc
	// 创建DroiTask
	DroiTask* task = [[[[[DroiTask create:^(DroiTask *taskObject) {
		// 欲执行的代码块        
	}] delay:500] then:^(DroiTask *taskObject) {
		// 停止执行此DroiTask
		[taskObject cancel];        
	}] delay:500] then:^(DroiTask *taskObject) {
		// 欲执行的代码块，但此段目前并不会被执行到        
	}];
	
	// 将此DroiTask放置至BackgroundThreadDispatcher Dispatch Queue执行
	[task runInBackground:BackgroundThreadDispatcher];
	
	// 等待DroiTask执行完毕
	[task waitTask];
	```

* Swift

	```swift
	// 创建DroiTask
	let task : DroiTask = DroiTask.create { (_) in            
			// 欲执行的代码块        
		}.delay(500).then { (taskObject) in
			// 停止执行此DroiTask
			taskObject.cancel()
		}.delay(500).then { (_) in
			// 欲执行的代码块，但此段目前并不会被执行到                    
		}
	
	// 将此DroiTask放置至BackgroundThreadDispatcher Dispatch Queue执行
	task.runInBackground(BackgroundThreadDispatcher)
        
	// 等待DroiTask执行完毕
	task.waitTask()	
	```
	
#### ***```Callback Task```的使用***
[DroiBaaS][droibaas]另外提供了所有```Task```执行完毕后再执行的```Callback Task```。一般在a应用的开发上，常常会需要将```Task```放置在到后台Dispatch Queue执行，当所有的工作完成后再回到Main Queue去更新apps UI的界面。```DroiTask```也提供了这类机制去完成上述所需。  


* Objective-C

	```objc
	DroiTask* task = [[[DroiTask create:^(DroiTask *taskObject) {
			// 欲执行的代码块 Task Block 1
		}] then:^(DroiTask *taskObject) {
			// 欲执行的代码块 Task Block 2
		}] callback:^(DroiTask *taskObject) {
			// 此段会在所有Task Block执行完毕后，回到Main Queue执行
		} inDispatcher:MainThreadDispatcher];
    
	// 将此DroiTask放置至BackgroundThreadDispatcher Dispatch Queue执行
	[task runInBackground:BackgroundThreadDispatcher];
	```

* Swift

	```swift
	let task : DroiTask = DroiTask.create { (_) in
			// 欲执行的代码块 Task Block 1
		}.then { (_) in
          // 欲执行的代码块 Task Block 2  
		}.callback({ (_) in
          // 此段会在所有Task Block执行完毕后，回到Main Queue执行  
		}, inDispatcher: MainThreadDispatcher)

	// 将此DroiTask放置至BackgroundThreadDispatcher Dispatch Queue执行
	task.runInBackground(BackgroundThreadDispatcher)
	```
	
上述的代码简单地将```Task Block```置放到后台线程执行；当执行完毕后，会回到主线程执行```callback```的代码。当开发者在其中一个```Task Block```执行```cancel```将工作取消。```DroiTask```会直接呼叫```callback```，开发者可以直接使用```DroiTask.isCancelled```来判断是否有呼叫过```cancel```这个方法以取消```DroiTask```执行。


[droibaas]: ../intro.md
[cp]: http://www.cocoapods.com
[apt_ref]: ../GETTINGSTARTED/GETTING_STARTED.md