# 云存储

## 简介
云存储是 DroiBaaS 提供的数据存储服务，它建立在对象 DroiObject 的基础上，所有 DroiObject 均存储在云端上。您可以通过 Android Core SDK 对其进行操作，也可在 DroiBaaS 后台管理所有的对象。  

以下就云存储开发相关技术区分成不同小节来描述如何开发：  

* [云端数据储存](#DataStorage)：描述如何使用 DroiBaaS 将对象数据上传的方式
* [数据表名](#DataTableName)：描述当 DroiBaaS 数据上传后实际在云端数据库建立的表格名称
* [数据关联](#Reference)：描述如何使用 DroiBaaS 进行不同数据表之间的数据关联
* [数据查询](#DataQuery)：实际说明使用 DroiBaaS 提供的功能查询已上传的数据
* [文件储存](#DroiFile)：如何使用 DroiBaaS 提供的功能上传文件到云端储存
* [应用程序参数设置](#DroiPreference)：DroiBaaS 提供一个方式让开发者可以动态设置应用程序相关数据参数
* [临时应用程序数据](#DroiCloudCache):当需要暂时且整个应用程序都可以使用的数据时，DroiBaaS 提供一个储存临时数据的功能给开发者使用
* [错误管理](#DroiError)：如何使用DroiError进行操作成功与否的判断以及得到错误信息。
* [多线程开发指引](#DroiTask)：如何使用DroiBaaS提供的TaskDispatcher和DroiTask进行多线程开发。

## <a id="DataStorage"></a>云端数据储存

DroiBaaS 使用最简便的方式去存储数据至云端或是由云端数据库下载数据。目前 DroiBaaS 提供开发者以下几种数据存储的方式：   

* [使用基础类```DroiObject```储存数据](#DroiObjectSave)
* [继承基础类```DroiObject```并扩充储存的数据属性 (推荐)](#DroiObjectExtension)

开发者可以根据实际的数据复杂度去选择使用哪种数据储存方式。基本的环境设置可以参考[快速入门]()。云端数据储存开发指南可以参考以下的文件内容。

> ***注意***：DroiBaaS 为了提高系统性能以便服务更多的应用程序，所以将云端数据读取、写入分开处理。在正常的情况下刚存入的数据立即可以抓取出来。但是在系统负载非常高的情况下，最差的情况有可能有五秒的延迟。

### <a id="DroiObjectSave"></a>使用基础类```DroiObject```

`DroiObject`对象在上传到云端前，会将包含在`DroiObject`内的数据(key-value)转换为JSON数据。目前```DroiObject```支持的数据格式有  

* `String`字符串
* `Number`数字
* `Boolean`布尔值
* `Array`数组（数组内数据格式也必须在此列表)
* `byte []`字节数组
* `Date`日期对象
* [`DroiFile`](#DroiFile)对象
* `DroiObject`扩充类　　
  
至于键值(key)则为字符串格式。但某些特殊字元如`$`,`@`及`.``在 DroiBaaS 系統之中已有特殊使用，开发者请不要在键值之中包含这些字符。

以一个简单的例子来说，假设开发者需要将使用者名称、密码以及性别等数据存放在DroiObject之中，可以使用如下的程序代码  

```java 
// 建立DroiObject，使用的数据表名为UserData
DroiObject userData = DroiObject.create("UserData");

// 依序将名称、密码及性别等数据放入userData之中
userData.put("name", "Droi");
userData.put("password", "xxxxx");
userData.put("gender", "Male");

// 上传user data至云端数据库
userData.save();
```
> ***注意***: ```DroiObject:save```是同步方法(synchronous)会等到数据上传到云端后才会返回。如需要使用异步方法(asynchronous)可以使用```DroiObject:saveInBackground:```配合回调方法(callback method)来达到不阻断目前执行线程的目的。[DroiBaaS][droibaas]内所有的函数都提供异步方法，如需要避免阻断目前工作线程，可以使用[Method Name]**InBackground**形式的方法。

当数据属性个数不多的时候可以直接使用```DroiObject```配合```put```及```get```方法将数据由```DroiObject```之中存入或取出。但当数据属性太多时，使用这种方式在程序代码的阅读及使用上不方便。[下节](#DroiObjectExtension)介绍继承基础类```DroiObject```并扩充储存数据属性的方式。

### <a id="DroiObjectExtension"></a>继承基础类```DroiObject```

当数据属性格式很多的情况时，使用```DroiObject```去存取数据在使用上非常地不方便。[上节]
(#DroiObjectSave)的程序代码可以改成

```java
// 声明UserData继承DroiObject，系统默认会以类的名称作为数据表名
public class UserData extends DroiObject {
	@DroiExpose
	String name;		// 使用者名称

	@DroiExpose
	String password;	// 密码

	@DroiExpose
	String gender;		// 性别
        
	String other;		// 其他属性
}    
...

// 1. 注册UserData至SDK
DroiObject.registerCustomClass( UserData.class );

// 2. 建立UserData对象
UserData userData = new UserData();

// 3. 设置相关名称、密码及性别数据
userData.name = "Droi";
userData.password = "xxxxx";
userData.gender = "Male";

// 4. 上传至云端数据库
userData.save();
```

> ***注意*** ```DroiObject:save```是同步方法(synchronous)会等到数据上传到云端后才会返回。如需要使用异步方法(asynchronous)可以使用```DroiObject:saveInBackground:```配合回调方法(callback method)来达到不阻断目前执行线程的目的。[DroiBaaS][droibaas]内所有的函数都提供异步方法，如需要避免阻断目前工作线程，可以使用[Method Name]**InBackground**形式的方法。

首先声明一个```UserData```类继承```DroiObject```，并将需要储存的数据属性上增加注解```@DroiExpose```，DroiBaaS 在储存数据时会自动将已注解的数据属性储存下来。  
接下来必须将自定义的类注册到 DroiBaaS 之中（使用```DroiObject.registerCustomClass```)。最后和正常的数据结构一样存取相关的数据属性，当数据属性设置完成后，开发者可以使用```DroiObject.save```将数据储存到云端。  
上面的程序代码中，注解```@DroiExpose```代表要将正下方的属性上传到云端。至于属性```other```由于没有注解```@DroiExpose```在上方，所以在上传数据到云端数据库时并不会将```other```的数据上传储存。  

## <a id="DataTableName"></a>数据表名

### 默认数据表名
数据表名代表数据储存在云端中显示出来的名称。如果开发者直接使用```DroiObject```储存数据时，DroiBaaS 会以```DroiObject.create( className )```中的```className```作为表名称。下面例子，DroiBaaS 会以```table_Name```作为```obj```数据储存时的表名称。  

```java
DroiObject obj = DroiObject.create("table_Name")
```

如果开发人员是以扩充```DroiObject```的方式建立数据，DroiBaaS 会以扩充的类名称作为数据表名。如下所示，DroiBaaS 会以```UserData```作为数据表名。  

```java	
public class UserData extends DroiObject {
    // ...
}
```

### 自定义数据表名
开发者开发不同平台应用时需要自定义数据储存时的表名称，让不同平台的数据表名相同，以便达到不同平台的应用可以共享同一个数据表。这时就可以利用注解```DroiObjectName```的方式来达到自定义表名称的目的。  
   
```java
@DroiObjectName("Table_Name")
public class UserData extends DroiObject {
    // ...
}
```

上面的例子会让数据储存直接使用```Table_Name```这一表名，而不是使用```UserData```这一个默认的数据表名。开发者可以透过这一方式让 DroiBaaS 不使用类名称作为数据表名。  

## <a id="Reference"></a>数据关联
DroiBaaS 在系统默认情况下，类含有其他```DroiObject```扩充类时，会直接将该资料转换成JSON并储存下来。但在某些数据库的使用上，需要有外部引用（reference）数据关联的功能。在此，DroiBaaS 提供两种外部数据关联的功能。  　　

* [使用```DroiReferenceObject```类](#DroiReferenceObject)
* [使用注解```DroiReference```](#DroiReference)

其中使用注解```DroiReference```只适用于```DroiObject```扩充类。下面各节会详述使用方式。  

> ***注意***：由于查询性能上的限制，使用外部引用无法作为数据查询的条件。如需要将外部引用作为其中一个查询条件，请先将符合条件的 外部引用查询出来再使用`IN`条件判断来查询结果。

### <a id="DroiReferenceObject"></a>使用```DroiReferenceObject```类
使用```DroiReferenceObject```类可以直接使用```property droiObject```设置```DroiObject```给此对象使用，当对象需要储存至云端时，DroiBaaS 会将引用到的对象储存在另一个数据表内。其设计的概念与数据库开发的外部数据关联是相同的。  

以下简单的程序代码中，主要是声明了作者的对象并使用一个```DroiReferenceObject```引用到该对象，并直接将```DroiReferenceObject```放置到书籍信息的对象之中。当此对象上传至云端时，系统会建立两个数据表（***Author***及***Book***)，由于***作者(Author)***的数据在***书籍(Book)***之中是使用引用的方式，所以当***作者(Author)***数据表的条数据有修改时，所有引用到该条数据的***书籍(Book)***数据都会一并更新。  

这在实际使用上有很大的好处，如果开发者并不是使用引用的方式，每本书的***作者(Author)***信息都直接将作者信息储存下来，当某位作者的个人资料需要更新时，将需要将所有的***书籍(Book)***表内此作者的数据都一并更新，这个动作将会耗费非常大的运算量及时间。   

假设开发者是使用引用的方式来储存数据，只需要修改一条***作者(Author)***内的数据即可。这可以省去大量的数据更新量及更新时间。

```java
// 声明作者信息对象
DroiObject author = DroiObject.create("Author");
author.put("name", "authorName");     // 设置作者名字
author.put("phoneNumber", "0000000"); // 设置作者电话号码

// 使用DroiReferenceObject设置外部引用
DroiReferenceObject refAuthor = new DroiReferenceObject();
refAuthor.setDroiObject( author ); // 指向author物件

// 声明书籍信息对象
DroiObject book = DroiObject.create("Book");
book.put("author", refAuthor);     // 设置作者信息（外部引用）
book.put("name", "bookName");      // 设置书名
book.put("price", 130);            // 设置价格
```

### <a id="DroiReference"></a>使用注解```DroiReference```
DroiBaaS 在系统默认情况下，类含有其他```DroiObject```扩充类时，会直接将该数据转换成JSON并储存下来。但在某些数据库的使用上，需要有外部引用（reference）数据关联的功能。在此，DroiBaaS 也提供外部数据关联的功能，此功能目前只支持```DroiObject```扩充类时使用。开发者只需要使用注解```DroiReference```就可以使用外部关联的方式储存数据在不同的表之中。下面是一个简单的例子  

```java
// 声明作者类
public class Author extends DroiObject {

    @DroiExpose
    String name; 			// 作者姓名

    @DroiExpose
    String address;			// 作者地址

    @DroiExpose
    String phoneNumber;		// 作者电话
}

// 声明书籍类
public class Book extends DroiObject {
    @DroiReference
    Author author;			// 作者信息

    @DroiExpose
    String name;			// 书名

    @DroiExpose
    String ISBN;			// ISBN

    @DroiExpose
    int price;				// 价格
}
// ...
```

在类```Book```中使用```@DroiReference```将author设置为关联数据。当```Book```对象储存时，DroiBaaS 会建立两个数据表(Author及Book)。其中```author```数据会储存在```Author```表格之中，而原来在```Book```类的```author```属性则会存```objectId```这个外部主键值。未来只要更新```Author```表内的数据则会一并更新所有用到同一条数据的```Author```数据。  
反之，若开发者没有使用```@DroiReference```，DroiBaaS 会直接将author的数据储存下来。由于没有使用另一个数据表储存数据，这就没有办法只更新```Author```表的数据以便所有引用到同一个作者的数据都更新。  

## <a id="DataQuery"></a>数据查询
当数据储存至云端后，必须有一个方式可以将数据查询回来使用。DroiBaaS 提供```DroiQuery```来完成数据查询的动作。开发者可以使用```DroiQuery```配合一些查询条件将数据查询回来使用。目前```DroiQuery```提供的功能有：  

* 可设置多个查询条件
* 支持属性排序及设置回传数据的起始位置(Offset)
* 通过[APT (Annotation Processor Tool)](#APT)可生成查询程序代码，减少开发者编程时间  

以下就简单介绍一下如何使用`DroiQuery`的查询功能。  

> ***注意***：为了增加系统执行效率。每次数据查询一次回传数据条数为100条（默认值）。开发者可以使用limit去增加回传数量（最大每次1000条）；如果希望去得到超过1000条以后的数据，开发者可以使用`offset`方法来改变回传数据的起始位置。

### <a id="NoAPT" ></a>使用`DroiQuery`查询数据
DroiBaaS 提供开发者使用`DroiQuery`查询有储存的数据。开发人员必须设置数据表名（或是直接引入类名）以及额外的查询条件（选用），即可将数据由云端查询回本地端使用。以下是一个简单的例子  

```java
// 建立一个查询条件(price = 70)
DroiCondition cond = DroiCondition.cond("price", DroiCondition.Type.EQ, 70);

// 建立一个查询对象
DroiQuery query = DroiQuery.newBuilder()
    .query( Book.class )		// 由Book数据表查询
    .where( cond )				// 加入查询条件 price = 70
    .orderBy("ISBN", false)		// 加入排序结果. 按照ISBN排序
    .build();

// 开始查询并回传结果
DroiError error = new DroiError();
List<Book> result = query.runQuery(error);
if (error.isOk()) {
    // 成功！
}
```

其中建立查询条件使用的```DroiCondition```类，它提供了简单的条件式设置以及复杂的查询条件(AND及OR)组合。目前```DroiCondition```提供的查询条件有：

| 常量名称 | 说明 |
|---------|:---------|
| **LT** | Less Than(<)，小于 |
| **LT\_OR\_EQ** | Less Than or Equal (<=)，小于或等于 |
| **EQ** | Equal (=)，等于 |
| **NEQ** | Not Equal (<>)，不等于 |
| **GT** | Greater Than(>)，大于 |
| **GT\_OR\_EQ** | Greater Than or Equal (>=)，大于或等于 |
| **ISNULL** | Is NULL，是否为空(NULL) |
| **ISNOTNULL** | Is Not NULL，是否不为空(NOT NULL) |
| **CONTAINS** | Contains, 包含 |
| **NOTCONTAINS** | Not Contains, 不包含 |
| **STARTSWITH** | Starts With, 前置字符串为指定的字符串 |
| **NOTSTARTSWITH** | Not Starts With, 前置字符串不为指定的字符串 |
| **ENDSWITH** | Ends With, 结尾字符串为指定的字串 |
| **NOTENDSWITH** | Not Ends With, 结尾字符串不为指定的字符串 |
| **IN** | IN, 数据是否符合所传入的列表。如```DroiCondition.cond("price", DroiCondition.Type.IN, "70, 80");```，代表要查询price是70或80的数据 |
| **NOTIN** | NOT IN，数据是否不符合所传入的列表 |

#### 复杂的查询条件 
开发者如果需要使用较复杂的查询条件，一样也可以使用```DroiCondition```类来达到需求。假设一个情景，需要查询书名有包含**'DIY'**且price小于80或是书名有包含**'HOW'**且price小于30的查询条件。我们可以写成  

```java
// 建立书名有包含DIY，且price小于80的查询条件
DroiCondition cond1 = DroiCondition.cond("name", DroiCondition.Type.CONTAINS, "DIY").and(
    DroiCondition.cond("price", DroiCondition.Type.LT, 80 ) );

// 建立书名有包含HOW，且price小于30的查询条件
DroiCondition cond2 = DroiCondition.cond("name", DroiCondition.Type.CONTAINS, "HOW").and(
    DroiCondition.cond("price", DroiCondition.Type.LT, 30 ) );

// 将上述两个条件式组合在一起
DroiCondition cond = cond1.or( cond2 );
```

到这里是不是觉得虽然 DroiBaaS 有提供复杂的查询条件功能，但是在使用上所需要编写的程序代码非常的多？为了减少开发者的工作，DroiBaaS 使用APT来有效地达到这个需求。[下节](#APT)会快速地说明这个方式。  

### <a id="APT" ></a>使用APT（Annotation Processor Tool)辅助查询
DroiBaaS 提供APT工具以简化开发者在编写复杂查询条件时的工作。具体的安装方式可以参考[快速入门]()。安装完成后，开发者只需要在需要生成程序代码的类上加上注解```@DroiQueryAnnotation```，APT即会自动生成该类的查询程序代码，以之前的例子来说我们加入```@DroiQueryAnnotation```在```class Book```之上，其程序代码如下：  

```java
@DroiQueryAnnotation
public class Book extends DroiObject {
    @DroiReference
    Author author;          // 作者信息

    @DroiExpose
    String name;            // 书名

    @DroiExpose
    String ISBN;            // ISBN

    @DroiExpose
    int price;              // 价格
}
```
	
加入注解后请执行**Make Build**，编辑器即会调用APT去生成相对应得程序代码。```QBook```(命名方式为原来的类名前面加上Q)。[上节](#NoAPT)的复杂查询条件即可以简单写成

```java
// ( name包含"DIY" 且 price < 80 )
DroiCondition cond = QBook.name_contains("DIY").and( QBook.price_lt("80") ) 
    .or( // 或
        // ( name包含"HOW" 且　price < 30 )
        QBook.name_contains("HOW").and( QBook.price_lt("30") 
    )
);
```

APT工具会依照开发者的类属性去生成相对于的程序代码。这样可以有效地减轻开发者在编写查询条件的困难度。  

## <a id="DroiFile"></a>DroiFile文件储存

云端数据是数据的集合，其实可以想象成云端上的数据库。有时候想要操作大型数据像是文件时，如果直接放进云端数据的话，会造成每次查询都会有大型的数据传输，而且如果又是多条数据时，数据量更是大，所以文件通常都会与云端数据分开管理，但分开管理又会让操作不方便，在DroiBaaS SDK，我们提供了DroiFie，让开发者可以操作文件上传下载，却又能与 DroiObject 关联在一起，是要声明 DroiFile 是个 DroiObject 内的 Expose 属性。

> ***注意***: `目前上传文件大小限制为 14 MB`  

``` java
public class MySongs extends DroiObject {
    @DroiExpose
    public String Name;
   
    @DroiExpose
    public DroiFile Cover;
}
```

### 上传文件

因为 DroiFile 是 DroiObject 内的一个属性，所以最终是通过 `DroiObject.save`，SDK 会先上传 DroiFile，成功后，再把云端数据储存。

``` java
DroiFile cover = new DroiFile(new File("/sdcard/my_heart.jpg"));

MySongs song = new MySongs();
song.Name = "My heart will go on";
song.Cover = cover;

DroiError result = song.save();

if (result.isOk()) {
    // Save OK!
}
```

### 下载文件

DroiFile 是属于 DroiObject 的一部分，所以在下载文件前，需先把 DroiObject 查询回来。

``` java
DroiQuery query = DroiQuery.Builder.newBuilder().cloudStorage().query(MySongs.class).build();
List<MySongs> list = query.runQuery(null); // DroiError 传 null 表示不需要知道错误
MySongs song = list.get(0);

byte[] coverData = song.Cover.get(null); // DroiError 传 null 表示不需要知道错误
String path = song.Cover.getName();

// Save coverData 
```

### 上传/下载进度

DroiBaaS SDK 提供了进度 callback，让开发者可以加强 UI 操作流程。

#### 上传

``` java
DroiFile cover = new DroiFile(new File("/sdcard/pokerface.jpg"));
cover.setSaveProcessListener(new DroiProgressCallback() {
    @Override
    public void progress(Object sender, long current, long max) {
        // from 0 to file size;
    }
});

MySongs song = new MySongs();
song.Name = "Poker face";
song.Cover = cover;
song.save();
```

#### 下载

``` java
DroiQuery query = DroiQuery.Builder.newBuilder().cloudStorage().query(MySongs.class).build();
List<MySongs> list = query.runQuery(null);
MySongs song = list.get(0);

song.Cover.getInBackground(new DroiCallback<byte[]>() {
    @Override
    public void result(byte[] bytes, DroiError error) {
        // Download complete.
    }
}, new DroiProgressCallback() {
    @Override
    public void progress(Object sender, long current, long max) {
        // process
    }
});
```

## <a id="DataPreference"></a>DroiPreference应用程序参数设置

DroiBaaS SDK 提供了一个全域只读的设置可让应用程序使用，这个设置是个Key/Value形式。开发者可在开发平台页面上设置，在应用程序内读取。开发者可以使用这一个功能可以做到让应用程序即时调整。  

### 使用示例
在DroiBaaS后台页面可以设置DroiPreference。  
在 `Core.initialize` 时，SDK 会后台下载设置。之后就不会再更新了，开发人员需调用`refresh` 来更新设置。

``` java
if (DroiPreference.instance().isReady()) {
    int intValue = DroiPreference.instance().getInt("IntKey", -1);
    long longValue = DroiPreference.instance().getLong("LongKey", -1);
    boolean boolValue = DroiPreference.instance().getBoolean("BoolKey", false);
    double doubleValue = DroiPreference.instance().getDouble("DoubleKey", -1);
    JSONObject jsonValue = DroiPreference.instance().getJsonObject("JsonKey");
    String strValue = DroiPreference.instance().getString("StrKey");
}
```

## <a id="DroiCloudCache"></a>DroiCloudCache临时应用程序数据

当开发者需要储存临时数据时， DroiBaas 提供`DroiCloudCache`来达到上述的功能。这类功能刚刚应用于网络购物的购物车记录中。请想象一个常常发生的使用情境，有味应用程序的使用这在A手机选择了几个物品放到购物车中，他可以利用临时数据这一个功能在B手机显示出刚才加入购物车的物品。  
但是这些资料不需要实际储存在云端数据库中。这大大减少应用程序对云端数据库的容量需求，而且也可以做到不同设备间的数据交换的目的。  
`DroiCloudCache`与`DroiObject`数据最大的不同点在于由`DroiCloudCache`所储存的数据并不会真正储存到云端数据库中，它的资料是暂存在云端系统内存中。以下表格是两者的简单比较：  

|  	| DroiCloudCache 	| DroiObject 	|
|--------------	|:----------------:|:------------:|
| 使用云端数据库 	| 否 	| 是 	|
| 可储存数据量 	| 较少(<500KB) 	| 大 	|
| 数据是否会消失 	| 是 	| 否 	|
| 数据形式 	| 键值(Key/Value) 	| 记录(Record)


由于数据并不保证永久存在于系统中，在实际使用上请开发者注意这个数据类别的特性。请勿使用这一功能去储存一些重要的数据。  

`DroiCloudCache`的数据格式是键值(Key/Value)的形式。一个Key字串会对应一个Value的字串。数据上传至服务器后，任意的设备都可以使用同样的键（Key）得到设定的数值（Value）。以下是简单的程序代码：  

```java
// 设定数据 key:1234, value:54678
DroiCloudCache.set("1234", "54678");

// 由云端取回设定数据
DroiError error = new DroiError();
String value = DroiCloudCache.get("1234", error );
```

## <a id="DroiBaaS"></a>DroiBaaS 错误管理

为了让开发者可以做到一致性的错误管理，DroiBaaS SDK 提供了 `DroiError` 这个错误类，如果 SDK API 有错误，会回传 `DroiError`。另外有些 API 的回传值已定义，也会将 `DroiError` 设置为最后一个参数回传错误码。另外，`DroiError` 也可以输出对应的错误信息，让开发者能更清楚的了解错误原因。

### DroiError 方法说明

#### isOk

让开发者快速知道调用的 API 是否成功。

#### getCode

取得错误代码。需要注意的是 0 表示成功，当 isOk 为 true 时，错误代码必定为 0。

#### toString

输出错误信息。

### DroiError 错误代码说明

|名称|值|说明|
|---|---|---|
|OK|0|API 执行成功|
|UNKNOWN_ERROR|1070000|API 发生未知的错误|
|ERROR|1070001|API 发生错误，详情请见错误信息|
|USER\_NOT\_EXISTS|1070002|此用户不存在|
|USER\_PASSWORD\_INCORRECT|1070003|用户名称或密码错误|
|USER\_ALREADY\_EXISTS|1070004|此用户已存在|
|NETWORK\_NOT\_AVAILABLE|1070005|网络连接出现问题|
|USER\_NOT\_AUTHORIZED|1070006|此用户认证已失效，请重新登录|
|SERVER\_NOT\_REACHABLE|1070007|无法连接服务器|
|HTTP\_SERVER\_ERROR|1070008|服务器回传错误，详情请见错误信息|
|SERVICE\_NOT\_ALLOWED|1070009|服务器拒绝服务|
|SERVICE\_NOT\_FOUND|1070010|服务不存在|
|INTERNAL\_SERVER\_ERROR|1070011|服务器发生错误|
|INVALID_PARAMETER|1070012|API 参数输入错误|
|NO\_PERMISSION|1070013|没有权限使用 API|
|USER_DISABLE|1070014|此用户已被禁止使用|
|EXCEED\_MAX\_SIZE|1070015|超出上限|
|FILE_\NOT\_READY|1070016|文件尚未准备好|
|CORE\_NOT\_INITIALIZED|1070017|必需在 DroiBaaS SDK 初始化后才能使用|
|USER_CANCELED|1070018|用户取消动作|

## <a id="DroiTask"></a>DroiBaaS多线程开发指引

### TaskDispatcher功能说明

DroiBaaS 提供开发者一个简单的工具去开发多线程apps。开发人员可以直接使用```TaskDispatcher```将```Task```放到指定的线程(Thread)执行。简单的程序代码如下：

```java
// 得到后台线程TaskDispatcher实例
TaskDispatcher taskDispatcher = TaskDispatcher.getDispatcher( TaskDispatcher.BackgroundThreadName );

// 将Task放到指定线程（thread)执行
taskDispatcher.enqueueTask(new Runnable() {
	@Override
	public void run() {
		// Do something
	}
});
```  

<br />
其中```TaskDispatcher.BackgroundThreadName```是一个```TaskDispatcher```定义的变量，开发者可以自定线程的名称。如果希望放置一个每秒执行一次的```Task```可以使用如下的程序代码：    
<br />

```java
// 得到后台线程TaskDispatcher实例
TaskDispatcher taskDispatcher = TaskDispatcher.getDispatcher( TaskDispatcher.BackgroundThreadName );

// 将Task定时(每1000ms)在指定的线程执行
taskDispatcher.enqueueTimerTask(new Runnable() {
	@Override
	public void run() {
		// 定时执行的Task
	}
}, 1000, "TimerTask");
```
其中第三个参数代表此```Task```的标记名称。开发者可以使用```TaskDispatcher.killTask```将已置入的```Task```删除。如果已经有同名的```Task```已置入在执行队列（running queue)之中，```TaskDispatcher```就不会将最后要置入的```Task```放置至执行队列之中。换句话说，在执行队列之中不能有其他同名的```Task```。

### DroiTask功能说明

虽然 DroiBaaS 提供了```TaskDispatcher```给开发者可任意将```Task```置入至另一个执行线程之中。但在实际开发时，其实有很多完整的流程是由许多小型的```Task```组合而成的。而且在执行完成之后，也可能需要触发一些```Callback Task```将执行的结果做出相对应的动作。
为了这一个目的，DroiBaaS 另外提供了```DroiTask```来达到此类的要求。  
<br />
#### ***连续性的```Task```串接***  
以下的程式代码将置入三个```Task```至```DroiTask```中并在线程```TaskDispatcher.BackgroundThreadName```执行，每个```Task```执行后再延迟500ms再执行下一个```Task```。此程式代码中在第二个```Task```就将这整个```DroiTask```通过```cancel```函数停止```DroiTask```执行。所以第三个```Task```并不会被执行到。而在主线程则是使用```waitTask```函数去等待```DroiTask```的工作完成。开发者可以将每个```Task```重复使用并达到最好的利用效率。

```java
	// 建立DroiTask
	DroiTask task = DroiTask.create(new DroiRunnable() {
			@Override
			public void run() {
				// 想要执行的程序
			}
		}).delay(500).then(new DroiRunnable() {
			@Override
			public void run() {
				// 停止执行此DroiTask
				this.cancel();
			}
		}).delay(500).then(new DroiRunnable() {
			@Override
			public void run() {
				// 想要执行的程序代码，但此段目前并不会被执行到
            }
		});
		
		// 将此DroiTask放到TaskDispatcher.BackgroundThreadName线程执行
		task.runInBackground(TaskDispatcher.BackgroundThreadName);

		try {
			// 等待DroiTask执行完成
			task.waitTask();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
```
<br />
#### ***```Callback Task```的使用***
DroiBaaS 另外提供了所有```Task```执行完毕后在执行的```Callback Task```。一般在apps的开发上，常常会需要将```Task```放到后台线程执行，当所有的工作完成后再回到主线程去更新 apps UI的画面。```DroiTask```也提供了这类的机制去完成上述所需要的项目。  

```java
	// 建立DroiTask
	DroiTask task = DroiTask.create(new DroiRunnable() {
			@Override
			public void run() {
				// 想要执行的程序代码 Task Block 1
			}
		}).then(new DroiRunnable() {
			@Override
			public void run() {
				// 想要执行的程序代码 Task Block 2
			}
		}).callback(new DroiRunnable() {
			@Override
			public void run() {
				// 此段会在所有Task Block执行完毕后，回到主线程执行
  			}
		}, TaskDispatcher.MainThreadName);
		
		// 将此DroiTask放到TaskDispatcher.BackgroundThreadName线程执行
		task.runInBackground(TaskDispatcher.BackgroundThreadName);
```

上述的程式代码简单地将```Task Block```置放到后台线程执行；当执行线程完毕后，会回到主线程执行```callback```的程式代码。当开发者在其中一个```Task Block```执行```cancel```将工作取消。```DroiTask```会直接调用```callback```，开哦发着可以直接使用```DroiRunnable.isCancelled```来判断是否有调用过```cancel```这一函数以取消```DroiTask```执行。
