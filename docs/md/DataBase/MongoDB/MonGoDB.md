## MongoDB笔记

官网 https://www.mongodb.com/

## 1. 什么是MongDB

MongoDB是一款为web应用程序和互联网基础设施设计的数据库管理系统。没错MongoDB就是数据库，是NoSQL类型的数据库

主要特性

* 文档数据类型
* 即时查询能力
* 复制能力
* 数据扩展

MongoDB shu

## 2 window 安装MongoDb

参考博客： 

https://blog.csdn.net/x_i_xw/article/details/82258125

创建root 用户

> use admin
> db.createUser({user:"root",pwd:"123",roles[{role:"userAdminAnyDatabase",db:"admin"}]})

启动关闭，使用管理员权限

> net start mongodb

>  net stop mongodb



## 3. Liunx 安装 Mongodb

### 下载

> wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-4.4.9.tgz
>
> wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-4.4.13.tgz
>
> tar -xzvf mongodb-linux-x86_64-rhel70-4.4.13.tgz
>
> mv mongodb-linux-x86_64-rhel70-4.4.13 /usr/local/soft/mongodb1



> mkdir /mongodb/data /mongodb/logs /mongodb/conf

### 启动服务

方式一 命令行启动

>bin/mongod --port=27017 --dbpath=/mongodb/data --logpath=/mongodb/log/mongodb.log  --bind_ip=0.0.0.0  --fork

![image-20220415201909587](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220415201909587.png)

方式二 文件启动

进入到bin目录下，编辑mongodb.conf文件，内容如下：



> vim /mongodb/conf/mongod.conf

```properties
dbpath=/mongodb/data 
logpath=/mongodb/log/mongod.log
port=27017
fork=true
bind_ip=0.0.0.0
```

> ./mongod -f mongod.conf 

![image-20220415193025385](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220415193025385.png)

注意：（也可以使用YAML格式）

```yml
systemLog:
  destination: file
  path: /mongodb/log/mongod.log
  logAppend: true
storage:
  dbPath: /mongodb/data 
  engine: wiredTiger 
  journal: 
    enabled: true
net:
  bindIp: 0.0.0.0
  port: 27017 
processManagement:
  fork: true
```



配置环境vim 、/etc/profile

![image-20220415193710338](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220415193710338.png)

> source /etc/profile

关闭mongodb

>  ps -ef | grep mongo
>
> kill -9 端口号

![image-20220415194117782](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220415194117782.png)

再次启动

> cd ~
>
> mongod -f /opt/mongodb/bin/mongodb.conf

### ![image-20220415202230399](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220415202230399.png)关闭服务

方式一

> mongod -f /opt/mongodb/bin/mongodb.conf --shutdown

![image-20220415202300588](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220415202300588.png)

方式二

使用db.shutdownServer();命令可以关闭到MongoDB服务，但是这个命令的执行要在admin数据库下，所以先切换到admin，再关闭服务，完整运行过程如下：

![图片](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/640-16500021406875.jpeg) 

方式三

关闭mongodb

> ps -ef | grep mongo
>
> kill -9 端口号

### 去除启动警告

[always] madvise never

> echo "never" > /sys/kernel/mm/transparent_hugepage/enabled
> echo "never" >  /sys/kernel/mm/transparent_hugepage/defrag

> vim /etc/security/limits.conf

>#/etc/security/limits.conf
>
>mongod  soft  nofile  64000
>mongod  hard  nofile  64000
>mongod  soft  nproc  32000
>mongod  hard  nproc  32000

![image-20220422140223451](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220422140223451.png)

### 自启动服务

配置开机自启动vim /lib/systemd/system/mongodb.service

> [Unit] 
> Description=mongodb 
> After=network.target remote-fs.target nss-lookup.target 
>
> [Service] 
> Type=forking 
> ExecStart=/usr/local/soft/mongodb/bin/mongod --auth --config mongodb/conf/mongod.conf 
> ExecReload=/bin/kill -s HUP $MAINPID 
> ExecStop=/usr/local/soft/mongodb/bin/mongod --shutdown --config mongodb/conf/mongod.conf 
> PrivateTmp=true 
>
> [Install] 
> WantedBy=multi-user.target

### 开启服务端口

![image-20220415195218260](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220415195218260.png)

在其他主机访问

### 在window 中访问

连接语法：

mongodb://username:paswd@host:port

> mongo mongodb://root:123@192.168.247.129:27017

![image-20220415195527237](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220415195527237.png)

### 使用 Mongdb Compass 工具连接



![image-20220415160155291](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220415160155291.png)



## 4. 安全认证

### 安全管理

上面我们所做的所有的操作都没有涉及到用户，我们在用Oracle、MySQL或者MSSQL时都有用户名密码需要登录才可以操作，MongoDB中当然也有，但是需要我们手动添加。在添加之前，我们先来说说MongoDB中用户管理的几个特点：

> 1.MongoDB中的账号是在某一个库里边进行设置的，我们在哪一个库里边进行设置，就要在哪一个库里边进行验证。
> 2.创建用户时，我们需要指定用户名、用户密码和用户角色，用户角色表示了该用户的权限。

OK，假设我给admin数据库创建一个用户，方式如下：

```
use admin

db.createUser({user:"root",pwd:"root",roles:[{role:"userAdminAnyDatabase",db:"admin"}]})
```

user表示用户名，pwd表示密码，role表示角色，db表示这个用户应用在哪个数据库上。用户的角色，有如下几种：
1.Read：允许用户读取指定数据库
2.readWrite：允许用户读写指定数据库
3.dbAdmin：允许用户在指定数据库中执行管理函数，如索引创建、删除，查看统计或访问system.profile
4.userAdmin：允许用户向system.users集合写入，可以找指定数据库里创建、删除和管理用户
5.clusterAdmin：只在admin数据库中可用，赋予用户所有分片和复制集相关函数的管理权限
6.readAnyDatabase：只在admin数据库中可用，赋予用户所有数据库的读权限
7.readWriteAnyDatabase：只在admin数据库中可用，赋予用户所有数据库的读写权限
8.userAdminAnyDatabase：只在admin数据库中可用，赋予用户所有数据库的userAdmin权限
9.dbAdminAnyDatabase：只在admin数据库中可用，赋予用户所有数据库的dbAdmin权限
10.root：只在admin数据库中可用。超级账号，超级权限

用户创建成功之后，我们关闭掉当前MongoDB服务实例，然后重新启动新的实例，启动方式如下：

```
mongod -f /opt/mongodb/bin/mongodb.conf --auth
```

启动成功之后，如果我们直接执行如下命令，会提示没有权限：

```
show dbs
```

执行结果如下：

```
"errmsg" : "not authorized on admin to execute command { listDatabases: 1.0 }",
"code" : 13,
"codeName" : "Unauthorized"
```

此时我们需要先进入到admin数据库中，然后授权，操作如下：

```
use admin
db.auth("root","123")
```

auth方法执行结果返回1表示认证成功。然后再去执行show dbs就可以看到预期结果了。此时我再在sang库下创建一个只读用户，如下：

```
use sang
db.createUser({user:"readuser",pwd:"123",roles:[{role:"read",db:"sang"}]})
```

创建成功之后，再按照上面的流程进入到sang库中，使用readuser用户进行认证，认证成功之后一切我们就可以在sang库中执行查询操作了，步骤如下：

```
use sang
db.auth("readuser","123")
```

做完这两步之后再执行查询操作就没有任何问题了，但是此时如果执行插入操作会提示没有权限，那我们可以创建一个有读写功能的用户执行相应的操作，这里就不再赘述。



实例操作

## 创建管理员

> mongo

```sql
#设置管理员用户和密码需要切换到admin库
use admin
查看库
show dbs
#创建管理员
db.createUser({user:"root",pwd:"root",roles:["root"]})
# 删除管理员用户
//db.dropUser("root")
```

![image-20220418082932928](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220418082932928.png)



管理员认证登录

设置管理员账户后，关闭服务，使用下面命令重新启动服务

> mongod -f /opt/mongodb/bin/mongodb.conf --auth  

鉴权登录

> mongo -uroot -p root

## 数据库操作

使用管理员用户登录

> mongo -uroot -p root

创建用户

>  use sang

>  db.createUser({user:"sang",pwd:"123",roles:["dbOwner"]})

退出使用 新用户登录操作sang 数据库

> mongo mongodb://sang:123@192.168.247.129:27017/?authSource=sang

查看数据库

> show dbs

删除数据库(首先进入数据)

> use appdb
>
> db.dropDatabase()

## Shell 操作



![image-20220422180640531](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220422180640531.png)



![image-20220422180811873](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220422180811873.png)

![image-20220422181120453](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220422181120453.png)

运行shell 脚本

在用户目录下

> vim .mongorc.js

```shell
print("Hello MONGDB")
```







![image-20220422183030200](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220422183030200.png)



## 数据类型

MongoDB使用BSON做为文档数据存储和网络传输格式

### 数字

### 字符串

### 正则表达式

语法和JavaScript中正则表达式的语法相同

> db.sang_collec.find({x:/^(hello)(.[a-zA-Z0-9])+/i})

### 数组

### 日期

### 内嵌文档

个文档也可以作为另一个文档的value

>db.sang_collect.insert({name:"三国演义",author:{name:"罗贯中",age:99}});

### ObjectId

我们在前面提到过，我们每次插入一条数据系统都会自动帮我们插入一个`_id`键，这个键的值不可以重复，它可以是任何类型的，我们也可以手动的插入，默认情况下它的数据类型是ObjectId，由于MongoDB在设计之初就是用作分布式数据库，所以使用ObjectId可以避免不同数据库中`_id`的重复（如果使用自增的方式在分布式系统中就会出现重复的`_id`的值），这个特点有点类似于Git中的版本号和Svn中版本号的区别。

ObjectId使用12字节的存储空间，每个字节可以存储两个十六进制数字，所以一共可以存储24个十六进制数字组成的字符串，在这24个字符串中，前8位表示时间戳，接下来6位是一个机器码，接下来4位表示进程id，最后6位表示计数器。

### 二进制

## 代码

文档中也可以包括JavaScript代码，如下：

> db.sang_collect.insert({x:function f1(a,b){return a+b;}});

## 集合操作



创建集合(进入数据库，创建集合)

> use appdb
>
> db.createCollection("emp")

查看集合

> show collections
>
> show tables

删除集合

>db.emp.drop()



创建集合语法

db.createCollection(name, options)

<img src="http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220420133232411.png" alt="image-20220420133232411" style="zoom:80%;" />



## MongoDB文档操作

[MongoDB CRUD Operations — MongoDB Manual](https://www.mongodb.com/docs/manual/crud/)

### 插入文档

#### 插入单个文档

```shell
db.inventory.insertOne(
   { item: "canvas", qty: 100, tags: ["cotton"], size: { h: 28, w: 35.5, uom: "cm" } }
)
```

​	查询

```shell
db.inventory.find( { item: "canvas" } )
```

#### 插入多个文档

```shell
db.inventory.insertMany([
   { item: "journal", qty: 25, tags: ["blank", "red"], size: { h: 14, w: 21, uom: "cm" } },
   { item: "mat", qty: 85, tags: ["gray"], size: { h: 27.9, w: 35.5, uom: "cm" } },
   { item: "mousepad", qty: 25, tags: ["gel", "blue"], size: { h: 19, w: 22.85, uom: "cm" } }
])
```

查询

```shell
db.inventory.find( {} )
```



> mongodb://tom:123@192.168.247.129:27017/?authMechanism=DEFAULT&authSource=sang

## 查询文档

### 数据准备

```shell
db.inventory.insertMany([
   { item: "journal", qty: 25, size: { h: 14, w: 21, uom: "cm" }, status: "A" },
   { item: "notebook", qty: 50, size: { h: 8.5, w: 11, uom: "in" }, status: "A" },
   { item: "paper", qty: 100, size: { h: 8.5, w: 11, uom: "in" }, status: "D" },
   { item: "planner", qty: 75, size: { h: 22.85, w: 30, uom: "cm" }, status: "D" },
   { item: "postcard", qty: 45, size: { h: 10, w: 15.25, uom: "cm" }, status: "A" }
]);
```

查询文档

```shell
db.inventory.find( {} )
```

等价于 `SELECT * FROM inventory`

```shell
db.inventory.find( { status: "D" } )
```

等价于 `SELECT * FROM inventory WHERE status = "D"`

```shell
db.inventory.find( { status: { $in: [ "A", "D" ] } } )
```

等价于 `SELECT * FROM inventory WHERE status in ("A", "D")`

```shell
db.inventory.find( { status: "A", qty: { $lt: 30 } } )
```

等价于 `SELECT * FROM inventory WHERE status = "A" AND qty < 30`

```shell
db.inventory.find( { $or: [ { status: "A" }, { qty: { $lt: 30 } } ] } )
```

等价于 `SELECT * FROM inventory WHERE status = "A" OR qty < 30`

```shell
db.inventory.find( {
     status: "A",
     $or: [ { qty: { $lt: 30 } }, { item: /^p/ } ]
} )
```

等价于 `SELECT * FROM inventory WHERE status = "A" AND ( qty < 30 OR item LIKE "p%")`

嵌套查询

```shell
db.inventory.find( { "size.uom": "in" } )
```

```shell
db.inventory.find( { "size.h": { $lt: 15 } } )
```

```shell
db.inventory.find( { "size.h": { $lt: 15 }, "size.uom": "in", status: "D" } )
```

数组查询

数据准备

```shell
db.inventory.insertMany([
   { item: "journal", qty: 25, tags: ["blank", "red"], dim_cm: [ 14, 21 ] },
   { item: "notebook", qty: 50, tags: ["red", "blank"], dim_cm: [ 14, 21 ] },
   { item: "paper", qty: 100, tags: ["red", "blank", "plain"], dim_cm: [ 14, 21 ] },
   { item: "planner", qty: 75, tags: ["blank", "red"], dim_cm: [ 22.85, 30 ] },
   { item: "postcard", qty: 45, tags: ["blue"], dim_cm: [ 10, 15.25 ] }
]);
```

查询

```shell
db.inventory.find( { tags: ["red", "blank"] } )
```

查询结果

```shell
{ "_id" : ObjectId("625fb18c251ac66c968b3c2a"), "item" : "notebook", "qty" : 50, "tags" : [ "red", "blank" ], "dim_cm" : [ 14, 21 ] }
```

查询

```
db.inventory.find( { tags: { $all: ["red", "blank"] } } )
```

查询结果

```shell
{ "_id" : ObjectId("625fb18c251ac66c968b3c29"), "item" : "journal", "qty" : 25, "tags" : [ "blank", "red" ], "dim_cm" : [ 14, 21 ] }
{ "_id" : ObjectId("625fb18c251ac66c968b3c2a"), "item" : "notebook", "qty" : 50, "tags" : [ "red", "blank" ], "dim_cm" : [ 14, 21 ] }
{ "_id" : ObjectId("625fb18c251ac66c968b3c2b"), "item" : "paper", "qty" : 100, "tags" : [ "red", "blank", "plain" ], "dim_cm" : [ 14, 21 ] }
{ "_id" : ObjectId("625fb18c251ac66c968b3c2c"), "item" : "planner", "qty" : 75, "tags" : [ "blank", "red" ], "dim_cm" : [ 22.85, 30 ] }

```

```shell
db.inventory.find( { tags: "red" } )
```

查询结果

```shell
{ "_id" : ObjectId("625fb18c251ac66c968b3c29"), "item" : "journal", "qty" : 25, "tags" : [ "blank", "red" ], "dim_cm" : [ 14, 21 ] }
{ "_id" : ObjectId("625fb18c251ac66c968b3c2a"), "item" : "notebook", "qty" : 50, "tags" : [ "red", "blank" ], "dim_cm" : [ 14, 21 ] }
{ "_id" : ObjectId("625fb18c251ac66c968b3c2b"), "item" : "paper", "qty" : 100, "tags" : [ "red", "blank", "plain" ], "dim_cm" : [ 14, 21 ] }
{ "_id" : ObjectId("625fb18c251ac66c968b3c2c"), "item" : "planner", "qty" : 75, "tags" : [ "blank", "red" ], "dim_cm" : [ 22.85, 30 ] 
```

查询

```shell
db.inventory.find( { dim_cm: { $gt: 25 } } )
```

结果

```shell
{ "_id" : ObjectId("625fb18c251ac66c968b3c2c"), "item" : "planner", "qty" : 75, "tags" : [ "blank", "red" ], "dim_cm" : [ 22.85, 30 ] }

```

查询数组字段满足大于15或者小于20 

```shell
db.inventory.find( { dim_cm: { $gt: 15, $lt: 20 } } )
```

查询结果

```shell
{ "_id" : ObjectId("625fb18c251ac66c968b3c29"), "item" : "journal", "qty" : 25, "tags" : [ "blank", "red" ], "dim_cm" : [ 14, 21 ] }
{ "_id" : ObjectId("625fb18c251ac66c968b3c2a"), "item" : "notebook", "qty" : 50, "tags" : [ "red", "blank" ], "dim_cm" : [ 14, 21 ] }
{ "_id" : ObjectId("625fb18c251ac66c968b3c2b"), "item" : "paper", "qty" : 100, "tags" : [ "red", "blank", "plain" ], "dim_cm" : [ 14, 21 ] }
{ "_id" : ObjectId("625fb18c251ac66c968b3c2d"), "item" : "postcard", "qty" : 45, "tags" : [ "blue" ], "dim_cm" : [ 10, 15.25 ] }

```



查询嵌入文档数组

```
db.inventory.insertMany( [
   { item: "journal", instock: [ { warehouse: "A", qty: 5 }, { warehouse: "C", qty: 15 } ] },
   { item: "notebook", instock: [ { warehouse: "C", qty: 5 } ] },
   { item: "paper", instock: [ { warehouse: "A", qty: 60 }, { warehouse: "B", qty: 15 } ] },
   { item: "planner", instock: [ { warehouse: "A", qty: 40 }, { warehouse: "B", qty: 5 } ] },
   { item: "postcard", instock: [ { warehouse: "B", qty: 15 }, { warehouse: "C", qty: 35 } ] }
]);
```

```shell
db.inventory.find( { status: "A" } )
```

等价于 `SELECT * from inventory WHERE status = "A"`

指定查询返回字段

```
db.inventory.find( { status: "A" }, { item: 1, status: 1 } )
```

等价于 `SELECT _id, item, status from inventory WHERE status = "A"`

```shell
db.inventory.find( { status: "A" }, { item: 1, status: 1, _id: 0 } )
```

等价于 `SELECT item, status from inventory WHERE status = "A"`



## 文档替换

假设我的集合中现在存了如下一段数据：

```
db.sang_collect.insert({
    "_id" : ObjectId("59f005402844ff254a1b68f7"),
    "name" : "三国演义",
    "authorName" : "罗贯中",
    "authorGender" : "男",
    "authorAge" : 99.0
})
```

这是一本书，有书名和作者信息，但是作者是一个独立的实体，所以我想将之提取出来，变成下面这样：

```
{
    "_id" : ObjectId("59f005402844ff254a1b68f6"),
    "name" : "三国演义",
    "author" : {
        "name" : "罗贯中",
        "gender" : "男",
        "age" : 99.0
    }
}
```

我可以采用如下操作：

```shell
var book=db.sang_collect.findOne({name:"三国演义"})
book.author={name:book.authorName,gender:book.authorGender,age:book.authorAge}
delete book.authorAge
delete book.authorGender
delete book.authorName
db.sang_collect.update({name:"三国演义"},book)
db.sang_collect.find()
db.sang.collect.remove({name:"三国演义"})
```

![图片](https://mmbiz.qpic.cn/mmbiz_png/GvtDGKK4uYmNMHUfOsSHGKz8yG8lFibXeJiavHLe21XWzFI4velm6JvJ43MITTUibksBAPjlhoclFicNzxU1WTHxqw/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1) 

另外一个问题是更新时，MongoDB只会匹配第一个更新的文档，假设我的MongoDB中有如下数据：

```
{ "_id" : ObjectId("59f00d4a2844ff254a1b68f7"), "x" : 1 }
{ "_id" : ObjectId("59f00d4a2844ff254a1b68f8"), "x" : 1 }
{ "_id" : ObjectId("59f00d4a2844ff254a1b68f9"), "x" : 1 }
{ "_id" : ObjectId("59f00d4a2844ff254a1b68fa"), "x" : 2 }
```

我想把所有x为1的数据改为99，我们很容易想到如下命令：

```
db.sang_collect.update({x:1},{x:99})
```

但我们发现执行结果却是这样：

```
{ "_id" : ObjectId("59f00d4a2844ff254a1b68f7"), "x" : 99 }
{ "_id" : ObjectId("59f00d4a2844ff254a1b68f8"), "x" : 1 }
{ "_id" : ObjectId("59f00d4a2844ff254a1b68f9"), "x" : 1 }
{ "_id" : ObjectId("59f00d4a2844ff254a1b68fa"), "x" : 2 }
```

即只有第一条匹配的结果被更新了，其他的都没有变化。这是MongoDB的更新规则，即只更新第一条匹配结果。如果我们想将所有x为1的更新为x为99，可以采用如下命令：

```
db.sang_collect.update({x:1},{$set:{x:99}},false,true)
```

首先我们将要修改的数据赋值给set是一个修改器，我们将在下文详细讲解，然后后面多了两个参数，第一个false表示如果不存在update记录，是否将我们要更新的文档作为一个新文档插入，true表示插入，false表示不插入，默认为false，第二个true表示是否更新全部查到的文档，false表示只更新第一条记录，true表示更新所有查到的文档。

## 使用修改器

很多时候我们修改文档，只是要修改文章的某一部分，而不是全部，但是现在我面临这样一个问题，假设我有如下一个文档：

```
{x:1,y:2,z:3}
```

我现在想把这个文档中x的值改为99，我可能使用如下操作：

```
db.sang_collect.update({x:1},{x:99})
```

但是更新结果却变成了这样：

```
{ "_id" : ObjectId("59f02dce95769f660c09955b"), "x" : 99 }
```

如下图：

![图片](https://mmbiz.qpic.cn/mmbiz_png/GvtDGKK4uYm4hs6deF49BFiapdhKX6Ndqjguq9KU2S3gNVGLktYoXj8icSzIzVeKNNdQYQibiaobb4sJjylwHFYeEg/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1) 

MongoDB帮我把整个文档更新了！要解决这个问题，我们可以使用修改器。

### $set修改器

$set可以用来修改一个字段的值，如果这个字段不存在，则创建它。如下：

![图片](https://mmbiz.qpic.cn/mmbiz_png/GvtDGKK4uYm4hs6deF49BFiapdhKX6NdqSWMyT735whezM1JF0alyy0JOH3FcoEzXx9kqOEBE7ruNT5MMd9oU1w/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1) 

如果该字段不存在，则创建，如下：

![图片](https://mmbiz.qpic.cn/mmbiz_png/GvtDGKK4uYm4hs6deF49BFiapdhKX6NdqHBxUjvMO6ULT9RlrNXyyz9g7oteEEbhhcjRHNUdG2icOvGzNYvFqWrg/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1) 

也可以利用$unset删除一个字段，如下：

![图片](https://mmbiz.qpic.cn/mmbiz_png/GvtDGKK4uYm4hs6deF49BFiapdhKX6Ndqa2ZIgWkoKyFtfEcicIKfRl2HB6Yy52kTefJvwp6KvRiciaQOSoW3ibRP5A/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1) 

$set也可以用来修改内嵌文档，还以刚才的书为例，如下：

```
{
    "_id" : ObjectId("59f042cfcafd355da9486008"),
    "name" : "三国演义",
    "author" : {
        "name" : "罗贯中",
        "gender" : "男"
    }
}
```

想要修改作者的名字，操作如下：

```
db.sang_collect.update({name:"三国演义"},{$set:{"author.name":"明代罗贯中"}})
```

修改结果如下：

```
{
    "_id" : ObjectId("59f042cfcafd355da9486008"),
    "name" : "三国演义",
    "author" : {
        "name" : "明代罗贯中",
        "gender" : "男"
    }
}
```

### $inc修改器

$inc用来增加已有键的值，如果该键不存在就新创建一个。比如我想给上文的罗贯中增加一个年龄为99，方式如下：

```
db.sang_collect.update({name:"三国演义"},{$inc:{"author.age1":99}})
```

执行结果如下：

```
{
    "_id" : ObjectId("59f042cfcafd355da9486008"),
    "name" : "三国演义",
    "author" : {
        "name" : "明代罗贯中",
        "gender" : "男",
        "age" : 99.0
    }
}
```

加入我想给罗贯中增加1岁，执行如下命令：

```
db.sang_collect.update({name:"三国演义"},{$inc:{"author.age":1}})
```

这是会在现有值上加1，结果如下：

```
{
    "_id" : ObjectId("59f042cfcafd355da9486008"),
    "name" : "三国演义",
    "author" : {
        "name" : "明代罗贯中",
        "gender" : "男",
        "age" : 100.0
    }
}
```

注意$inc只能用来操作数字，不能用来操作null、布尔等。

### 数组修改器

数组修改器有好几种，我们分别来看。
$push可以向已有数组末尾追加元素，要是不存在就创建一个数组，还是以我们的上面的book为例，假设book有一个字段为comments，是一个数组，表示对这个book的评论，我们可以使用如下命令添加一条评论：

```
db.sang_collect.update({name:"三国演义"},{$push:{comments:"好书666"}})
```

此时不存在comments字段，系统会自动帮我们创建该字段，结果如下：

```
{
    "_id" : ObjectId("59f042cfcafd355da9486008"),
    "name" : "三国演义",
    "author" : {
        "name" : "明代罗贯中",
        "gender" : "男",
        "age" : 100.0
    },
    "comments" : [ 
        "好书666"
    ]
}
```

此时我们可以追加评论，如下：

```
db.sang_collect.update({name:"三国演义"},{$push:{comments:"好书666啦啦啦啦"}})
```

结果如下：

```
{
    "_id" : ObjectId("59f042cfcafd355da9486008"),
    "name" : "三国演义",
    "author" : {
        "name" : "明代罗贯中",
        "gender" : "男",
        "age" : 100.0
    },
    "comments" : [ 
        "好书666", 
        "好书666啦啦啦啦"
    ]
}
```

如果想一次添加3条评论，可以结合$each一起来使用，如下：

```
db.sang_collect.update({name:"三国演义"},{$push:{comments:{$each:["111","222","333"]}}})
```

结果如下：

```
{
    "_id" : ObjectId("59f042cfcafd355da9486008"),
    "name" : "三国演义",
    "author" : {
        "name" : "明代罗贯中",
        "gender" : "男",
        "age" : 100.0
    },
    "comments" : [ 
        "好书666", 
        "好书666啦啦啦啦", 
        "111", 
        "222", 
        "333"
    ]
}
```

我们可以使用$slice来固定数组的长度，假设我固定数组的长度为5，如果数组中的元素不足5个，则全部保留，如果数组中的元素超过5个，则只会保留最新的5个，如下：

```
db.sang_collect.update({name:"三国演义"},{$push:{comments:{$each:["444","555"],$slice:-5}}})
```

注意$slice的值为负数，运行结果如下：

```
{
    "_id" : ObjectId("59f042cfcafd355da9486008"),
    "name" : "三国演义",
    "author" : {
        "name" : "明代罗贯中",
        "gender" : "男",
        "age" : 100.0
    },
    "comments" : [ 
        "111", 
        "222", 
        "333", 
        "444", 
        "555"
    ]
}
```

我们还可以在清理之前使用$sort对数据先进行排序，然后再清理比如我有一个class文档，如下：

```
db.class.ins{
    "_id" : ObjectId("59f07f3649fc5c9c2412a662"),
    "class" : "三年级二班"
}
```

现在向这个文档中插入student，每个student有姓名和成绩，然后按照成绩降序排列，只要前5条数据，如下：

```
db.sang_collect.update({class:"三年级二班"},{$push:{students:{$each:[{name:"张一百",score:100},{name:"张九九",score:99},{name:"张九八",score:98}],$slice:5,$sort:{score:-1}}}})
```

$sort的取值为-1和1，-1表示降序，1表示升序。
上面的命令执行两次之后（即插入两次），结果如下：

```
{
    "_id" : ObjectId("59f07f3649fc5c9c2412a662"),
    "class" : "三年级二班",
    "students" : [ 
        {
            "name" : "张一百",
            "score" : 100.0
        }, 
        {
            "name" : "张一百",
            "score" : 100.0
        }, 
        {
            "name" : "张九九",
            "score" : 99.0
        }, 
        {
            "name" : "张九九",
            "score" : 99.0
        }, 
        {
            "name" : "张九八",
            "score" : 98.0
        }
    ]
}
```

**sort不能只和each。** 

### $addToSet

我们可以在插入的时候使用$addToSet，表示要插入的值如果存在则不插入，否则插入，如下：

```
db.sang_collect.update({name:"三国演义"},{$addToSet:{comments:"好书"}})
```

上面的命令执行多次之后，发现只成功插入了一条数据。也可以将each结合起来使用，如下：

```
db.sang_collect.update({name:"三国演义"},{$addToSet:{comments:{$each:["111","222","333"]}}})
```

### $pop

$pop可以用来删除数组中的数据，如下：

```
db.sang_collect.update({name:"三国演义"},{$pop:{comments:1}})
```

1表示从comments数组的末尾删除一条数据，-1表示从comments数组的开头删除一条数据。

### $pull

使用$pull我们可以按条件删除数组中的某个元素，如下：

```
db.sang_collect.update({name:"三国演义"},{$pull:{comments:"444"}})
db.sang_collect.update({name:"三国演义"},{$pull:{author.age1:"99"}})
```

表示删除数组中值为444的数据。

### $

既然是数组，我们当然可以通过下标来访问，如下一行操作表示将下标为0的(第一个comments)comments修改为999：

```
db.sang_collect.update({name:"三国演义"},{$set:{"comments.0":"999"}})
```

可是有的时候我并不知道我要修改的数据处于数组中的什么位置，这个时候可以使用$符号来解决：

```
db.sang_collect.update({comments:"333"},{$set:{"comments.$":"333-1"}})
```

查询条件查出来333的下标，符号就能将之修改。

## save

save是shell中的一个函数，接收一个参数，这个参数就是文档，如果文档中有`_id`参数save会执行更新操作，否则执行插入操作，使用save操作我们可以方便的完成一些更新操作。

![图片](https://mmbiz.qpic.cn/mmbiz_png/GvtDGKK4uYm4hs6deF49BFiapdhKX6Ndqu1iaJ4bA2HGfmAicvYOg3elYtuKUrZXLyxXOIlGMnGISjlQTryyqicUbA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1) 

类似于如下命令则表示一个插入操作(因为没有`_id`)：

```
db.sang_collect.save({x:111})
```

好了，MongoDB的更新操作我们就先介绍这么多，有问题欢迎留言讨论。

删除文档



## 5. Spring boot 整合 MongoDB

引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

创建emp

```java
@Document("emp") //对应emp集合中的一个文档
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id //映射文档中的_id
    private Integer id;
    @Field("username")
    private String name;
    @Field
    private int age;
    @Field
    private Double salary;
    @Field
    private Date birthday;
}
```

测试代码

```java
@SpringBootTest
class LearnMongodbApplicationTests {
    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void contextLoads() {
        boolean exists = mongoTemplate.collectionExists("emp");
        if (exists) {
            //删除集合
            mongoTemplate.dropCollection("emp");
        }
        //创建集合
        mongoTemplate.createCollection("emp");


    }

    @Test
    @DisplayName( "测试插入操作" )
    void insert(){
        Employee employee = new Employee(1, "小明", 30,10000.00, new Date());
    //添加文档
    // sava: _id存在时更新数据
     //   mongoTemplate.save(employee);
    // insert： _id存在抛出异常 支持批量操作
      //  mongoTemplate.insert(employee);
        List<Employee> list = Arrays.asList(
                new Employee(2, "张三", 21,5000.00, new Date()),
                new Employee(3, "李四", 26,8000.00, new Date()),
                new Employee(4, "王五",22, 8000.00, new Date()),
                new Employee(5, "张龙",28, 6000.00, new Date()),
                new Employee(6, "赵虎",24, 7000.00, new Date()),
                new Employee(7, "赵六",28, 12000.00, new Date()));
        //插入多条数据
        mongoTemplate.insert(list,Employee.class);
    }
    @Test
    @DisplayName( "测试查询数据库" )
    void testFind(){
        System.out.println("==========查询所有文档===========");
        //查询所有文档
        List<Employee> list = mongoTemplate.findAll(Employee.class);
        list.forEach(System.out::println);
        System.out.println("==========根据_id查询===========");
        //根据_id查询
        Employee e = mongoTemplate.findById(1, Employee.class);
        System.out.println(e);
        System.out.println("==========findOne返回第一个文档===========");
        //如果查询结果是多个，返回其中第一个文档对象
        Employee one = mongoTemplate.findOne(new Query(), Employee.class);
        System.out.println(one);
        System.out.println("==========条件查询===========");
        //new Query() 表示没有条件
        //查询薪资大于等于8000的员工
        //Query query = new Query(Criteria.where("salary").gte(8000));
        //查询薪资大于4000小于10000的员工
        //Query query = new Query(Criteria.where("salary").gt(4000).lt(10000));
        //正则查询（模糊查询） java中正则不需要有//
        //Query query = new Query(Criteria.where("name").regex("张"));
        //and or 多条件查询
        Criteria criteria = new Criteria();
        //and 查询年龄大于25&薪资大于8000的员工
        //criteria.andOperator(Criteria.where("age").gt(25),Criteria.where("salary").gt(8000));
        //or 查询姓名是张三或者薪资大于8000的员工
        criteria.orOperator(Criteria.where("name").is("张 三"),Criteria.where("salary").gt(5000));
        Query query = new Query(criteria);
        //sort排序
        //query.with(Sort.by(Sort.Order.desc("salary")));
        //skip limit 分页 skip用于指定跳过记录数，limit则用于限定返回结果数量。
        query.with( Sort.by(Sort.Order.desc("salary")))
                .skip(0) //指定跳过记录数
                .limit(4); //每页显示记录数
        //查询结果
        List<Employee> employees = mongoTemplate.find(
                query, Employee.class);
        employees.forEach(System.out::println);
    }

    @Test
    @DisplayName( "测试json查询" )
    public void testFindByJson() {
        //使用json字符串方式查询
        //等值查询
        //String json = "{name:'张三'}";
        //多条件查询
        String json = "{$or:[{age:{$gt:25}},{salary:{$gte:8000}}]}";
        Query query = new BasicQuery(json);
        //查询结果
        List<Employee> employees = mongoTemplate.find(
                query, Employee.class);
        employees.forEach(System.out::println);
    }

    @Test
    @DisplayName( "更新文档" )
    public void testUpdate(){
        //query设置查询条件
        Query query = new Query(Criteria.where("salary").gte(15000));
        System.out.println("==========更新前===========");
        List<Employee> employees = mongoTemplate.find(query, Employee.class);
        employees.forEach(System.out::println);
        Update update = new Update();
        //设置更新属性
        update.set("salary",13000);
        //updateFirst() 只更新满足条件的第一条记录
        //UpdateResult updateResult = mongoTemplate.updateFirst(query, update,Employee.class);
        //updateMulti() 更新所有满足条件的记录
        //UpdateResult updateResult = mongoTemplate.updateMulti(query, update,Employee.class);
        //upsert() 没有符合条件的记录则插入数据
        //update.setOnInsert("id",11); //指定_id
        UpdateResult updateResult = mongoTemplate.upsert(query, update,
                Employee.class);
        //返回修改的记录数
        System.out.println(updateResult.getModifiedCount());
        System.out.println("==========更新后===========");
        employees = mongoTemplate.find(query, Employee.class);
        employees.forEach(System.out::println);
    }

    @Test
    @DisplayName( "删除文档" )
    public void testDelete(){
        //删除所有文档
        //mongoTemplate.remove(new Query(),Employee.class);
        //条件删除
        Query query = new Query(Criteria.where("salary").gte(10000));
        mongoTemplate.remove(query,Employee.class);
    }
}
```

## 6. 聚合操作

### 单一聚合

单一作用聚合：提供了对常见聚合过程的简单访问，操作都从单个集合聚合文档。

![image-20220419155030860](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220419155030860.png)

### 管道聚合

聚合管道是一个数据聚合的框架，模型基于数据处理流水线的概念。文档进入多级管道，将文档转
换为聚合结果。

官方参考文档

[Aggregation Pipeline Stages — MongoDB Manual](https://www.mongodb.com/docs/manual/reference/operator/aggregation-pipeline/)

语法 db.collection.aggregate( [ { <stage> }, ... ] )

数据准备

准备数据集，执行脚本

```shell
var tags = ["nosql","mongodb","document","developer","popular"];
var types = ["technology","sociality","travel","novel","literature"];
var books=[];
for(var i=0;i<50;i++){
var typeIdx = Math.floor(Math.random()*types.length);
var tagIdx = Math.floor(Math.random()*tags.length);
var tagIdx2 = Math.floor(Math.random()*tags.length);
var favCount = Math.floor(Math.random()*100);
var username = "xx00"+Math.floor(Math.random()*10);
var age = 20 + Math.floor(Math.random()*15);
var book = {
title: "book-"+i,
type: types[typeIdx],
tag: [tags[tagIdx],tags[tagIdx2]],
favCount: favCount,
author: {name:username,age:age}
};
books.push(book)
}
db.books.insertMany(books);
```

#### $project

语法





![image-20220419162224740](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220419162224740.png)



![image-20220419162517134](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220419162517134.png)





## 视图

数据准备

```shell
db.students.insertMany( [
   { sID: 22001, name: "Alex", year: 1, score: 4.0 },
   { sID: 21001, name: "bernie", year: 2, score: 3.7 },
   { sID: 20010, name: "Chris", year: 3, score: 2.5 },
   { sID: 22021, name: "Drew", year: 1, score: 3.2 },
   { sID: 17301, name: "harley", year: 6, score: 3.1 },
   { sID: 21022, name: "Farmer", year: 1, score: 2.2 },
   { sID: 20020, name: "george", year: 3, score: 2.8 },
   { sID: 18020, name: "Harley", year: 5, score: 2.8 },
] )
```



方法一

```shell
db.createCollection(
  "<viewName>",
  {
    "viewOn" : "<source>",
    "pipeline" : [<pipeline>],
    "collation" : { <collation> }
  }
)
```

方法二

```shell
db.createView(
  "<viewName>",
  "<source>",
  [<pipeline>],
  {
    "collation" : { <collation> }
  }
)
```

使用db.createView()创建视图

```shell
db.createView(
   "firstYears",
   "students",
   [ { $match: { year: 1 } } ]
)
```

查询视图

```shell
db.firstYears.find({}, { _id: 0 } )
```

查询结果

```shell
{ "sID" : 22001, "name" : "Alex", "year" : 1, "score" : 4 }
{ "sID" : 22021, "name" : "Drew", "year" : 1, "score" : 3.2 }
{ "sID" : 21022, "name" : "Farmer", "year" : 1, "score" : 2.2 }
```

使用db.createCollection() 创建视图

```shell
db.createCollection(
   "graduateStudents",
   {
      viewOn: "students",
      pipeline: [ { $match: { $expr: { $gt: [ "$year", 4 ] } } } ],
      collation: { locale: "en", caseFirst: "upper" }
   }
)
```

查看视图

```shell
db.graduateStudents.aggregate(
   [
      { $sort: { name: 1 } },
      { $unset: [ "_id" ] }
   ]
)
```

输出结果

```shell
[
  { sID: 18020, name: 'Harley', year: 5, score: 2.8 },
  { sID: 17301, name: 'harley', year: 6, score: 3.1 }
]
```

使用视图连接2个集合

数据做准备

```shell
db.inventory.insertMany( [
   { prodId: 100, price: 20, quantity: 125 },
   { prodId: 101, price: 10, quantity: 234 },
   { prodId: 102, price: 15, quantity: 432 },
   { prodId: 103, price: 17, quantity: 320 }
] )

db.orders.insertMany( [
   { orderID: 201, custid: 301, prodId: 100, numPurchased: 20 },
   { orderID: 202, custid: 302, prodId: 101, numPurchased: 10 },
   { orderID: 203, custid: 303, prodId: 102, numPurchased: 5 },
   { orderID: 204, custid: 303, prodId: 103, numPurchased: 15 },
   { orderID: 205, custid: 303, prodId: 103, numPurchased: 20 },
   { orderID: 206, custid: 302, prodId: 102, numPurchased: 1 },
   { orderID: 207, custid: 302, prodId: 101, numPurchased: 5 },
   { orderID: 208, custid: 301, prodId: 100, numPurchased: 10 },
   { orderID: 209, custid: 303, prodId: 103, numPurchased: 30 }
] )
```

创建视图 sales

```shell
db.createView( "sales", "orders", [
   {
      $lookup:
         {
            from: "inventory",
            localField: "prodId",
            foreignField: "prodId",
            as: "inventoryDocs"
         }
   },
   {
      $project:
         {
           _id: 0,
           prodId: 1,
           orderId: 1,
           numPurchased: 1,
           price: "$inventoryDocs.price"
         }
   },
      { $unwind: "$price" }
] )
```

查看视图

```shell
db.sales.find({})
```

输出结果

```shell
{ "prodId" : 100, "numPurchased" : 20, "price" : 20 }
{ "prodId" : 101, "numPurchased" : 10, "price" : 10 }
{ "prodId" : 102, "numPurchased" : 5, "price" : 15 }
{ "prodId" : 103, "numPurchased" : 15, "price" : 17 }
{ "prodId" : 103, "numPurchased" : 20, "price" : 17 }
{ "prodId" : 102, "numPurchased" : 1, "price" : 15 }
{ "prodId" : 101, "numPurchased" : 5, "price" : 10 }
{ "prodId" : 100, "numPurchased" : 10, "price" : 20 }
{ "prodId" : 103, "numPurchased" : 30, "price" : 17 }

```

查找每个产品的总销售量



输出结果

```shell
[
  { _id: 100, amountSold: 600 },
  { _id: 103, amountSold: 1105 },
  { _id: 101, amountSold: 150 },
  { _id: 102, amountSold: 90 }
]
```

修改视图



删除视图

```shell
db.sales.drop()
```

## 索引

[Indexes — MongoDB Manual](https://www.mongodb.com/docs/v4.0/indexes/)

创建索引

```
db.collection.createIndex( <key and index type specification>, <options> )
```

## MongoDB 复制集环境搭建



环境准备



![image-20220421094239853](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220421094239853.png)

## 单台服务器模拟

在一台主机中启动三个MonDB实例

> mkdir ‐p /data/db{1,2,3}

服务一

```shell
dbpath=/data/db1
logpath=/data/db1/mongod.log
bind_ip=0.0.0.0
port=27017
fork=true
replSet=rs
```

```shell
dbpath=/data/db2
logpath=/data/db2/mongod.log
bind_ip=0.0.0.0
port=27018
fork=true
replSet=rs
```

```shell
dbpath=/data/db3
logpath=/data/db3/mongod.log
bind_ip=0.0.0.0
port=27019
fork=true
replSet=rs
```

启动服务



![image-20220424135327684](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220424135327684.png)



> mongo --port=27017

```shell
rs.initiate({
    _id: "rs",
    members: [{
        _id: 0,
        host: "192.168.247.129:27017"
        },{
        _id: 1,
        host: "192.168.247.129:27018"
        },{
        _id: 2,
        host: "192.168.247.129:27019"
    }]
})


```

查看状态

```shell
rs.status()
```

在三台主机中

## 多台服务器模拟

一般准备奇数台复制集主机，这里使用三个。

首先准备好三台装好了MongoDB的服务器，地址分别如下：（注意三台主机的27017端口防火墙要放行，）

```
192.168.247.141
192.168.247.142
192.168.247.143
```
创建存放数据目录/data/mongodb/db 存放日志的目录/data/mongodb/logs，存放服务启动配置文件的目录/data/mongodb/conf

```shell
mkdir -p /data/mongodb/db /data/mongodb/logs /data/mongodb/conf
```

每台服务器的配置文件mongod.conf，添加replSet=rs，表示副本集的名称，修改后的配置文件内容如下：

> vim /data/mongodb/conf/mongod.conf

```
dbpath=/data/mongodb/db
logpath=/data/mongodb/logs/mongodb.log
bind_ip=0.0.0.0
port=27017
fork=true
replSet=rs
```

修改完成之后，分别启动三台服务器上的MongoDB，

> mongod -f /data/mongodb/conf/mongod.conf

启动成功之后，连接上任意一台的shell，连接成功之后，先定义配置文件，如下：

```
config={
    _id: "rs0",
    members: [{
        _id: 0,
        host: "192.168.247.140:27017"
        },{
        _id: 1,
        host: "192.168.247.141:27017"
        },{
        _id: 2,
        host: "192.247.142:27017"
    }]
}
```

id后面跟着的是副本集的名称，也就是我们在mongodb.conf中定义的名称，后面三个是副本集的成员，定义好之后，再执行如下命令初始化副本集：

```
rs.initiate(config)
```

也可以把上面2个命令写在一起

```shell
rs.initiate({
    _id: "rs0",
    members: [{
        _id: 0,
        host: "192.168.247.140:27017"
        },{
        _id: 1,
        host: "192.168.247.141:27017"
        },{
        _id: 2,
        host: "192.247.142:27017"
    }]
})
```



初始化成功之后，我们就可以通过rs.status()来查看副本集的状态，也可以看到每个服务器的角色，部分日志内容如下：

```
rs.status()
```

我们可以看到每台服务器的角色，有primary，也有secondary，secondary上还注明了从哪个服务器上同步数据。

### 安全认证

### 创建用户

在主节点服务器上，启动mongo

```shell
use admin
# 创建用户
db.createUser({
    user: "fox",
    pwd: "fox",
    roles: [
        { role: "clusterAdmin", db: "admin" } ,
        { role: "userAdminAnyDatabase", db: "admin"},
        { role: "userAdminAnyDatabase", db: "admin"},
        { role: "readWriteAnyDatabase", db: "admin"}
    ]
})
```

创建成功后，三个主机mongo服务关掉并重新开启

> mongod -f /opt/mongodb/bin/mongod.conf --shutdown

> mongod -f /opt/mongodb/bin/mongod.conf --auth



### 创建keyFile文件

在任意一台主机生成mongo.key

生成文件复制到其他2个主机。并修改权限（必须为600）

```shell
openssl rand -base64 745 > /data/mongo.key
chmod 600 /data/mongo.key
```

关掉服务，依次重启三个服务

使用keyFile 默认开启用户认证，不需要再使用mongo命令

```shell
mongod -f /opt/mongodb/bin/mongod.conf  --keyFile /data/mongo.key 
```



## 副本集成员添加删除



在副本集环境搭建好之后，我们可以利用如下命令删除一个副本集成员：

```
rs.remove('192.168.247.129:27017')
```

上面的命令执行完成后，我们可以通过rs.status()命令来查看是否删除成功，也可以通过如下命令来为副本集添加一个成员：

```
rs.add('192.168.247.129:27017')
```

当然，副本集也是可以更新的，使用reconfig命令即可，如下：

首先定义config，如下：

```
config={_id:"rs",members:[{_id:3,host:"192.168.247.129"},{_id:1,host:"192.168.247.135"}]}
```

然后执行更新操作：

```
rs.reconfig(config)
```

我们也可以利用config=rs.config()获取原始的config文件，然后进行修改，修改之后再执行 rs.reconfig(config)，如下：

```
config=rs.config()
config.members[0].host="192.168.248.136"
rs.reconfig(config)
```

## 选举仲裁者

在上文中给小伙伴们演示了主节点挂掉后的情况，和其他的(如Redis)数据库主从复制不同，MongoDB中主节点挂掉之后会自动从备份节点中选出一个新的主节点出来，这是一个选举的过程，投票选举，但是如果备份节点数为偶数的话，可能会出现两台服务器票数相等的情况，为了避免这种问题的出现，我们一般有两种解决方案：

> 1.数据节点为奇数个，这样就会避免上面描述的问题出现。
> 2.使用选举仲裁者，这是一种特殊的成员，仲裁者不保存数据，也不为客户端提供服务，只是在选举投票出现僵持时出来投个票，一个副本集中最多只能有一个仲裁者。

选举仲裁者占用的系统资源很小，因此对部署的服务器性能没多大要求，向副本集中添加仲裁者的方式如下：

```
rs.addArb('192.168.248.128:27017')
```

也可以利用我们之前说的reconfig来操作：

```
config=rs.config()
config.members[2]={_id:2,host:'192.168.248.128',arbiterOnly:true}
rs.reconfig(config)
```

添加完成之后，我们可以通过rs.status()命令来查看是否添加成功，如果看到如下内容，表示添加成功：

```
{
"_id" : 2,
"name" : "192.168.248.128:27017",
"health" : 1,
"state" : 7,
"stateStr" : "ARBITER",
"uptime" : 2,
"lastHeartbeat" : ISODate("2017-11-03T08:56:12.406Z"),
"lastHeartbeatRecv" : ISODate("2017-11-03T08:56:08.417Z"),
"pingMs" : NumberLong(1),
"configVersion" : 8
}
```

仲裁者的移除和普通节点的移除是一样的，这里不再赘述。

## 优先级问题

优先级用来描述一个备份节点成为主节点的优先性问题，优先级的取值范围为[0-100]，默认为1，数字越大优先级越高，越有可能成为主节点，0表示该节点永远不能成为主节点。
我们可以在添加节点时指定优先级，如下:

```
rs.add({_id:0,host:'192.168.248.128:27017',priority:2})
```

也可以为已有的节点设置优先级：

```
config=rs.config()
config.members[0].priority=99
rs.reconfig(config)
```

## 数据同步方式

MongoDB中的复制功能主要是使用操作日志oplog.rs来实现的，oplog.rs包含了主节点的每一次写操作，oplog.rs是主节点中local数据库的一个固定集合，我们可以通过如下命令查看到：

```
use local
show tables
```

如下：

![图片](https://mmbiz.qpic.cn/mmbiz_png/GvtDGKK4uYm4hs6deF49BFiapdhKX6NdqasJa3DmnK41PgIIykDKib24BwzC4B8TRJLAibbBzQul6NI5L7mAlqvew/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1) 

备份节点通过查询这个集合就知道要复制哪些数据，同时，每一个备份节点也都维护着自己的oplog.rs，自己的oplog.rs则用来记录每一次从主节点复制数据的操作，如此，每一个备份节点都可以再作为数据源提供给其他成员使用，如果某一个备份节点在使用的过程中挂掉了，那么当它重启之后，会自动从oplog.rs的最后一个操作开始同步。

上文我们也已经说过oplog.rs是一个固定集合，我们可以通过`db.getCollection('oplog.rs').stats()`这个命令来查看这个固定集合的属性，包括集合大小等，执行部分结果如下：

```
{
    "ns" : "local.oplog.rs",
    "size" : 18170305,
    "count" : 177443,
    "avgObjSize" : 102,
    "storageSize" : 5902336,
    "capped" : true,
    "max" : -1,
    "maxSize" : 1038090240,
    "sleepCount" : 0,
    "sleepMS" : 0,
}
```

既然是固定集合，它里边能够保存的数据大小就是有限的。通常，oplog.rs使用空间的增长速度与系统处理处理写请求的速率近乎相同，比如主节点每分钟处理了1KB的写入请求，那么oplog.rs也可能会在一分钟内写入1KB条操作日志，但是如果主节点执行了批量删除的命令，比如下面这种：

```
db.c1.deleteMany({x:{$type:1}})
```

此时每一个受影响的文档都会产生一条oplog中的日志，这个时候oplog.rs中的日志会快速增加。

## 成员状态

到目前为止我们了解到的成员状态有两种，一个是PRIMARY，还有一个是SECONDDARY，成员状态的获取需要靠心跳来维护，副本集中的每一个成员每隔两秒就会向其他成员发送一个心跳请求，用来检查成员的状态，成员的状态主要有如下几种：

#### STARTUP

副本集中的成员刚刚启动时处于这个状态下，此时，MongoDB会去加载成员的副本集配置，配置加载成功之后，就进入到STARTUP2的状态。

#### STARTUP2

整个初始化同步过程都处于这个状态。

#### RECOVERING

这个状态是由STARTUP2状态来的，此时成员运转正常，但是此时还不能处理读取请求。

#### ARBITER

这是仲裁者所处的状态。

#### DOWN

当一个原本运行正常的成员无法访问到时，该成员就处于DOWN的状态。

#### UNKNOWN

如果一个成员无法到达其他任何成员，该成员就处于UNKNOWN状态，比如我们利用rs.add()方法添加一个不存在的成员，这个成员的状态就是UNKNOWN。

#### REMOVED

成员被从副本集中移除时就变成这个状态。

#### ROLLBACK

如果成员正在进行数据回滚，它就处于ROLLBACK状态，回滚结束后会转换为RECOVERING状态。

#### FATAL

当一个成员发生了不可挽回的错误时，且不再尝试恢复正常的话，就处于这个状态。

## 主节点转备份节点

通过如下命令可以让主节点转为备份节点：

```
rs.stepDown()
```

主节点转为备份节点之后会有新的主节点被选举出来，可以通过rs.status()来查看新的主节点。

## rs.status()方法

前面我们已经多次使用过rs.status()方法，rs.status()方法会列出每个备份节点的含义，我们来看看这些参数的含义，先来列出一个rs.status()方法的返回值样例：

```
{
"members" : [
    {
        "_id" : 1,
        "name" : "192.168.248.135:27017",
        "health" : 1,
        "state" : 2,
        "stateStr" : "SECONDARY",
        "uptime" : 241,
        "optime" : {
                "ts" : Timestamp(1509881297, 1),
                "t" : NumberLong(16)
        },
        "optimeDurable" : {
                "ts" : Timestamp(1509881297, 1),
                "t" : NumberLong(16)
        },
        "optimeDate" : ISODate("2017-11-05T11:28:17Z"),
        "optimeDurableDate" : ISODate("2017-11-05T11:28:17Z"),
        "lastHeartbeat" : ISODate("2017-11-05T11:28:18.073Z"),
        "lastHeartbeatRecv" : ISODate("2017-11-05T11:28:18.769Z"),
        "pingMs" : NumberLong(0),
        "syncingTo" : "192.168.248.136:27017",
        "configVersion" : 15
    },
    {
        "_id" : 3,
        "name" : "192.168.248.136:27017",
        "health" : 1,
        "state" : 1,
        "stateStr" : "PRIMARY",
        "uptime" : 250,
        "optime" : {
                "ts" : Timestamp(1509881297, 1),
                "t" : NumberLong(16)
        },
        "optimeDate" : ISODate("2017-11-05T11:28:17Z"),
        "electionTime" : Timestamp(1509881276, 1),
        "electionDate" : ISODate("2017-11-05T11:27:56Z"),
        "configVersion" : 15,
        "self" : true
    }
]
}
```

1.stateStr用来描述当前节点的状态。
2.uptime表示从成员可达到现在所经历的时间。
3.optimeDate表示每个成员的oplog中最后一个操作发生的时间。
4.lastHeartbeat表示当前服务器最后一次收到其他成员心跳的时间。
5.pingMs表示心跳从当前服务器到达某个成员所花费的平均时间。
6.syncingTo表示同步的数据源。
7.health表示该服务器是否可达，1表示可达，0表示不可达。

## 复制链问题

数据复制时可以从主节点直接复制，也可以从备份节点开始复制，从备份节点复制可以形成复制链，如果想禁止复制链，即所有的数据都从主节点复制，可以通过chainingAllowed属性来设置，具体步骤如下：

```
config=rs.config()
config.settings.chainingAllowed=false
rs.reconfig(config)
```



# MongDB 分片环境搭建

参考博客

https://blog.csdn.net/qq_24757635/article/details/108591622

## 准备工作

准备三台主机，并安装了mongdb 4.4.9

192.168.247.131

192.168.247.132

192.168.247.133

配置域名解析

在三台主机上执行下面三条命令

```

echo "192.168.247.131 mongo1 mongo01.com mongo02.com" >> /etc/hosts
echo "192.168.247.132 mongo2 mongo03.com mongo04.com" >> /etc/hosts
echo "192.168.247.133 mongo3 mongo05.com mongo06.com" >> /etc/hosts

```

检查三台主机防火墙，关闭防火墙，实际工作中开启对应端口

```shell
systemctl status firewalld

systemctl stop firewalld

systemctl start firewalld

systemctl restart firewalld
```



配置分片1的目录创建

mongo01.com|mongo03.com|mongo05.com

也就是三台主机各执行下面语句（三台主机各自执行一次）

```shell
mkdir -p /data/shard1/db /data/shard1/log 
```

配置服务器目录创建（三台主机各自执行一次）

```shell
mkdir -p /data/config/db /data/config/log
```

配置路由（三台主机各自执行一次）

```shell
mkdir -p /data/mongos
```

## 分片1创建

创建第一个分片复制集

三台主机分别执行下面启动服务（也可使用配置文件启动）

```shell
mongod --bind_ip 0.0.0.0 --replSet shard1 --dbpath /data/shard1/db --logpath /data/shard1/log/mongod.log --port 27010 --fork --shardsvr --wiredTigerCacheSizeGB 1
```

复制集节点初始化

```shell
rs.initiate({
	_id: "shard1",
	"members" : [
	{
		"_id": 0,
		"host" : "mongo01.com:27010"
	},
	{
		"_id": 1,
		"host" : "mongo03.com:27010"
	},
	{
		"_id": 2,
		"host" : "mongo05.com:27010"
	}]
})
```









![image-20220424092253836](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220424092253836.png)

```
rs.status()
```

分片1配置完成。

## 配置集配置

在mongo01.com / mongo03.com / mongo05.com上执行以下命令：

三台主机各自执行一次启动服务

```shell
mongod --bind_ip 0.0.0.0 --replSet config --dbpath /data/config/db --logpath /data/config/log/mongod.log --port 27019 --fork --configsvr --wiredTigerCacheSizeGB 1
```

进入其中一个主机 执行下面命令

> mongo --prot=27019

```shell
rs.initiate({
	_id: "config",
	"members" : [
	{
		"_id": 0,
		"host" : "mongo01.com:27019"
	},
	{
		"_id": 1,
		"host" : "mongo03.com:27019"
	},
	{
		"_id": 2,
		"host" : "mongo05.com:27019"
	}]
})
```

![image-20220424143418027](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220424143418027.png)

```shell
rs.status()
```

配置集搭建完成

## 搭建路由mongos

在mongo01.com / mongo03.com / mongo05.com上执行以下命令（也就是三台主机各自执行一次）

启动mongos,

### 配置config复制集

```shell
mongos --bind_ip 0.0.0.0 --logpath /data/mongos/mongos.log --port 27017 --fork --configdb config/mongo01.com:27019,mongo03.com:27019,mongo05.com:27019
```

### mongos 添加分片

```shell
mongo mongo01.com:27017
```

```shell
sh.addShard("shard1/mongo01.com:27010,mongo03.com:27010,mongo05.com:27010")
sh.status()
```

### 创建分片集合

```shell
mongo mongo01.com:27017
sh.enableSharding("company")

sh.shardCollection("company.emp", {_id: 'hashed'})
sh.status()

```

插入数据，默认test 数据库，注意切换的company数据库

```shell 
# 切换数据库
use company
# 插入数据
for (var i = 0; i < 10000; i++) {
	db.emp.insert({i: i});
}
```

插入数据时间较长，耐心等待1到2分钟

查询数据分布（在company数据库下）

```shell
db.emp.getShardDistribution()
```



![image-20220424102839201](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220424102839201.png)



到这里分片集的基本功能就搭建完了。包括分片，配置集，路由mongos



## 添加第分片集

添加分片2过程和分片一类似

在mongo02.com / mongo04.com / mongo06.com上执行以下命令：（三台主机各自执行一次）

```shell
mongod --bind_ip 0.0.0.0 --replSet shard2 --dbpath /data/shard2/db --logpath /data/shard2/log/mongod.log --port 27011 --fork --shardsvr --wiredTigerCacheSizeGB 1
```

### 初始化第二个分片集

> mongo mongo06.com:27011

```shell
rs.initiate({
	_id: "shard2",
	"members" : [
	{
		"_id": 0,
		"host" : "mongo06.com:27011"
	},
	{
		"_id": 1,
		"host" : "mongo02.com:27011"
	},
	{
		"_id": 2,
		"host" : "mongo04.com:27011"
	}]
})
```

创建分片2完成



## 连接mongos加入分片2

> mongo mongo01.com:27017

```shell
sh.addShard("shard2/mongo02.com:27011,mongo04.com:27011,mongo06.com:27011")
查看mongos状态
sh.status()
```

发现之前添加在分片1中的数据移动一部分到了分片二

![image-20220424104314689](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220424104314689.png)

搭建完毕
