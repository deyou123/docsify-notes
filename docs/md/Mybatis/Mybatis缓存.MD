# mybatis  缓存

几个概念

SqlSession : 代表和数据库的一次会话，向用户提供了操作数据库的方法。

MappedStatement: 代表要发往数据库执行的指令，可以理解为是Sql的抽象表示。

Executor: 具体用来和数据库交互的执行器，接受MappedStatement作为参数。

映射接口: 在接口中会要执行的Sql用一个方法来表示，具体的Sql写在映射文件中。

映射文件: 可以理解为是Mybatis编写Sql的地方，通常来说每一张单表都会对应着一个映射文件，在该文件中会定义Sql语句入参和出参的形式。

## 一级缓存

一级缓存默认开启不可关闭,有2中级别

* SESSION  缓存对真个SqlSession 有效，只有执行更新语句才会失效。
* STATEMEN  缓存仅对当前执行语句有效。当语句执行完毕缓存就会清除。

查看源码：

```
public enum LocalCacheScope {
  SESSION,STATEMENT
}
```
Configuration.java
```java
public class Configuration {
	protected LocalCacheScope localCacheScope = LocalCacheScope.SESSION;
}
```
可在mybatis-config.xml 中设置一级缓存级别。

* 默认SESSION ，可以不设置。

```xml
	<setting name="localCacheScope" value="SESSION"/> //STATEMENT	
```
当设置STATEMENT 时。查询结束立刻清除缓存。

###  下面通过项目创建一个Maven项目进行测试

```xml
<dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.7</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.25</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.20</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>

        </dependency>
    </dependencies>
<build>
    <resources>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.xml</include>
            </includes>
        </resource>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*</include>
            </includes>
        </resource>
    </resources>
</build>
```

在 resources 中新建

mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>
    <settings>
        <setting name="useGeneratedKeys" value="true"/>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <setting name="logImpl" value="STDOUT_LOGGING" />
  </settings>
    <environments default="dev">
        
        <environment id="dev">
            <transactionManager type="JDBC">
                <property name="" value=""/>
            </transactionManager>
            <dataSource type="UNPOOLED">
                <property name="driver" value="org.hsqldb.jdbcDriver"/>
                <property name="url" value="jdbc:hsqldb:mem:mybatis"/>
                <property name="username" value="sa"/>
                <property name="password" value=""/>
            </dataSource>
        </environment>
    </environments>

    <mappers>
        <mapper resource="com/zdy/mapper/UserMapper.xml"/>
    </mappers>
</configuration>

```

新建user.sql 数据库文件

```sql
use db_mybatis;
drop table user ;
create table user (
    id int primary key ,
    name varchar(255) not null ,
    age int not null

);

insert into user (id, name, age)values (1,'zhangsan',18);
insert into user (id, name, age)values (2,'lisi',18);


```

在 main/java/com.zdy.common包下新建

DButils.java

```java
package com.zdy.common;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


public class DbUtils {
    public static Connection initData() {
        Connection conn = null;
        // 加载HSQLDB驱动
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            // 获取Connection对象
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:mybatis",
                    "sa", "");
            // 使用Mybatis的ScriptRunner工具类执行数据库脚本
            ScriptRunner scriptRunner = new ScriptRunner(conn);
            // 不输出sql日志
            scriptRunner.setLogWriter(null);
            scriptRunner.runScript(Resources.getResourceAsReader("user.sql"));
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void dumpRS(ResultSet resultSet) throws Exception {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columCount = metaData.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columCount; i++) {
                String columName = metaData.getColumnName(i);
                String columVal = resultSet.getString(columName);
                System.out.println(columName + ":" + columVal);
            }
            System.out.println("-------------------------------------");
        }
    }

}

```

新建 User.java

```java
package com.zdy.entity;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private int age;
}
```

新建UserMapper.java

```java
public interface UserMapper {
    List<User> findAllUser();
    User findUserByIDd(int id);

}
```

新建UserMapper.xml

```java
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.zdy.mapper.UserMapper">
    <sql id="allFiled">
        id,name,age
    </sql>

    <select id="findAllUser" resultType="com.zdy.entity.User">
        select <include refid="allFiled"/> from USER
    </select>
    <select id="findUserByIDd" resultType="com.zdy.entity.User">
        select * from USER where ID = #{id}
    </select>
</mapper>

```

测试代码

```java
package com.zdy;

import com.zdy.common.DbUtils;
import com.zdy.entity.User;
import com.zdy.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author deyou
 * @version 1.0
 * @date 2021/6/28 14:47
 */

public class Test {
    @Before
    public void initData(){
        DbUtils.initData();
    }
    @org.junit.Test
    public void test1() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		
        System.out.println(mapper.findUserById(1));
        System.out.println(mapper.findUserById(1));
        System.out.println(mapper.findUserById(1));


    }
}

```
### 1.1 测试一级缓存 SESSION. 默认为true 可以不在xml 文件配置
```xml
<setting name="localCacheScope" value="SESSION"/>
```

```java
public class TestSeesion {
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;

    @Before
    public void initData() throws IOException {
        DbUtils.initData();
         sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
    }
 
    @Test
    public void test1() throws IOException {
        sqlSession = sqlSessionFactory.openSession(true);//开启自动提交
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        System.out.println(userMapper.findUserById(1));
        System.out.println(userMapper.findUserById(1));
        System.out.println(userMapper.findUserById(1));
    }
}
```



测试结果：

```shell
Setting autocommit to false on JDBC Connection [org.hsqldb.jdbc.JDBCConnection@5dd6264]
==>  Preparing: select * from USER where ID = ?
==> Parameters: 1(Integer)
<==    Columns: ID, NAME, AGE
<==        Row: 1, zhangsan, 18
<==      Total: 1
User(id=1, name=zhangsan, age=18)
User(id=1, name=zhangsan, age=18)
User(id=1, name=zhangsan, age=18)

Process finished with exit code 0
```

只执行一次数据库查询。

### 1.2  同一个sqlSession 修改数据

```java
public void test2() throws IOException {
    sqlSession = sqlSessionFactory.openSession(true);//开启自动提交
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    System.out.println(userMapper.findUserById(1));
    User user = new User();
    user.setName("wangwu");
    user.setId(3);
    user.setAge(18);
    userMapper.addUser(user);
    sqlSession.commit();
    System.out.println(userMapper.findUserById(1));
}
```

```shell
Created connection 871160466.
==>  Preparing: select * from USER where ID = ?
==> Parameters: 1(Integer)
<==    Columns: id, name, age
<==        Row: 1, llllll444, 20
<==      Total: 1
User(id=1, name=llllll444, age=20)
==>  Preparing: insert into USER (id,NAME, AGE) VALUES (?,?,?)
==> Parameters: 4(Integer), wangwu(String), 18(Integer)
<==    Updates: 1
==>  Preparing: select * from USER where ID = ?
==> Parameters: 1(Integer)
<==    Columns: id, name, age
<==        Row: 1, llllll444, 20
<==      Total: 1
User(id=1, name=llllll444, age=20)
Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@33ecda92]
Returned connection 871160466 to pool.
```

修改数据库修改后重新查询数据。再次执行查询语句。

### 1.3

```java
@Test
public void test3() throws IOException {
   SqlSession sqlSession1 = sqlSessionFactory.openSession(true);

   SqlSession sqlSession2 = sqlSessionFactory.openSession(true);

    UserMapper userMapper1 = sqlSession1.getMapper(UserMapper.class);
    UserMapper userMapper2 = sqlSession2.getMapper(UserMapper.class);
    System.out.println("会话1读取数据" + userMapper1.findUserById(1));
    System.out.println("会话1读取数据" + userMapper1.findUserById(1));
    System.out.println("会话2读取数据" + userMapper1.findUserById(1));
    User user1 = new User(1, "llllll666", 20);
    userMapper2.updateUserById(user1);
    System.out.println("会话2更新了数据，"+ user1.getId()+user1.getName());
    System.out.println("会话1读取数据" + userMapper1.findUserById(1));
    System.out.println("会话2读取数据" + userMapper2.findUserById(1));
```

```shell
==>  Preparing: select * from USER where ID = ?
==> Parameters: 1(Integer)
<==    Columns: id, name, age
<==        Row: 1, 张三, 20
<==      Total: 1
会话1读取数据User(id=1, name=张三, age=20)
会话1读取数据User(id=1, name=张三, age=20)
Opening JDBC Connection
Created connection 550302731.
==>  Preparing: select * from USER where ID = ?
==> Parameters: 1(Integer)
<==    Columns: id, name, age
<==        Row: 1, 张三, 20
<==      Total: 1
会话2读取数据User(id=1, name=张三, age=20)
==>  Preparing: update db_mybatis.user set id = ?, name=?, age= ? where id = ?
==> Parameters: 1(Integer), 李四(String), 20(Integer), 1(Integer)
<==    Updates: 1
会话2更新了数据，1李四
会话1读取数据User(id=1, name=张三, age=20)
==>  Preparing: select * from USER where ID = ?
==> Parameters: 1(Integer)
<==    Columns: id, name, age
<==        Row: 1, 李四, 20
<==      Total: 1
会话2读取数据User(id=1, name=李四, age=20)
```

sqlSession2更新了id为1的user的姓名，从张三改为了李四，但session1之后的查询中，id为1的学生的名字还是张三，出现了脏数据，也证明了我们之前就得到的结论，一级缓存只存在于只在数据库会话内部共享。





## 启用STATEMENT

添加

```sql
<setting name="localCacheScope" value="STATEMENT"/>
```



```xml
Setting autocommit to false on JDBC Connection [org.hsqldb.jdbc.JDBCConnection@5dd6264]
==>  Preparing: select * from USER where ID = ?
==> Parameters: 1(Integer)
<==    Columns: ID, NAME, AGE
<==        Row: 1, zhangsan, 18
<==      Total: 1
User(id=1, name=zhangsan, age=18)
==>  Preparing: select * from USER where ID = ?
==> Parameters: 1(Integer)
<==    Columns: ID, NAME, AGE
<==        Row: 1, zhangsan, 18
<==      Total: 1
User(id=1, name=zhangsan, age=18)
==>  Preparing: select * from USER where ID = ?
==> Parameters: 1(Integer)
<==    Columns: ID, NAME, AGE
<==        Row: 1, zhangsan, 18
<==      Total: 1
User(id=1, name=zhangsan, age=18)
```

每次查询，就执行一次数据库查询操作。

## 二级缓存

1. 在mybatis 主配置文件中指定cacheEnable 属性值为true.

```java
<setting name="cacheEnable" value="true"/>
```

​	查看package org.apache.ibatis.session包下Configrution 类发现

```java
protected boolean cacheEnabled = true;//默认为true ，可以不设置
```

2. Mapper 文件中配置缓存策略。

	官网文档 https://mybatis.org/mybatis-3/zh/sqlmap-xml.html#cache.

	参考博客：[(80条消息) MyBatis源码的学习（6）---二级缓存默认是关闭的？_itw_zhangzx02的博客-CSDN博客_mybatis二级缓存默认开启还是关闭](https://blog.csdn.net/itw_zhangzx02/article/details/103554634)

	在Sql 映射文件中，配置。

```
<cache
  eviction="FIFO"
  flushInterval="60000"
  size="512"
  readOnly="true"/>
```

- `LRU` – 最近最少使用：移除最长时间不被使用的对象。
- `FIFO` – 先进先出：按对象进入缓存的顺序来移除它们。
- `SOFT` – 软引用：基于垃圾回收器状态和软引用规则移除对象。
- `WEAK` – 弱引用：更积极地基于垃圾收集器状态和弱引用规则移除对象。

3. 也可以针对Sql映射文件中的 配置是否启用缓存useCache, flushCahce指定是否刷新缓存。

```java
  <select id="findUserById" resultType="com.zdy.entity.User" useCache="true" flushCache="true">
        select * from USER where ID = #{id}
    </select>
```

