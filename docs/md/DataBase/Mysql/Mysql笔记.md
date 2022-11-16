### Mysql 个人笔记补充

## 第01章

DBMS 排名

[historical trend of the popularity ranking of database management systems (db-engines.com)](https://db-engines.com/en/ranking_trend)

![image-20220425094056938](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220425094056938.png)

软件下载地址

https://dev.mysql.com/downloads/installer/



## 第2章

可以下载5.7版本和8.0版本

5.7 数据库也不支持中文，字符集默认是latin1。

### 5.7 数据库也不支持中文解决办法

查看编码命令

> show variables like 'character_%';
> show variables like 'collation_%';

修改mysql的数据目录下C:\ProgramData\MySQL\MySQL Server 5.7的my.ini配置文件

```ini
[mysqld] # 大概在76行左右，在其下添加
...
character-set-server=utf8
collation-server=utf8_general_c
```

### 修改msyql8的加密规则

mysql8 加密规则`caching_sha2_password`

mysql5 加密规则 `mysql_native_password`

Mysql 8 修改加密规则

```shell
#使用mysql数据库
USE mysql;
#修改'root'@'localhost'用户的密码规则和密码
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'abc123';
#刷新权限
FLUSH PRIVILEGES;
```

重启服务

### root用户密码忘记

问题1：root用户密码忘记，重置的操作

* 通过任务管理器或者服务管理，关掉mysqld(服务进程)

* 通过命令行+特殊参数开启

```shell
mysqld mysqld --defaults-file="D:\ProgramFiles\mysql\MySQLServer5.7Data\my.ini" --skip-grant-tables
```

* 此时，mysqld服务进程已经打开。并且不需要权限检查 

* mysql -uroot 无密码登陆服务器。另启动一
  个客户端进行 

* 修改权限表 

```shell
 use mysql; 
 update user set authentication_string=password('新密码') where user='root' and Host='localhost'; 
 flush privileges; 
```

* 通过任务管理器，关掉mysqld服务进程。 

* 再次通过服务管理，打开mysql服务。 

* 即可用修改后的新密码登陆。



## 第3章 SQL 

SQL 分类

DDL（Data Definition Languages） 数据定义语言

	- create 
	- drop
	- alter

DML(Data Manipulation Language) 数据管理语言

* insert
* delete
* update
* select

DCL(Data Control Language) 数据控制语言

​	GRANT 、 REVOKE 、 COMMIT 、 ROLLBACK 、 SAVEPOINT

SQL语言在功能上主要分为如下3大类：
DDL（、数据定义语言），这些语句定义了不同的数据库、表、视图、索
引等数据库对象，还可以用来创建、删除、修改数据库和数据表的结构。
主要的语句关键字包括 CREATE 、 DROP 、 ALTER 等。
DML（Data Manipulation Language、数据操作语言），用于添加、删除、更新和查询数据库记
录，并检查数据完整性。
主要的语句关键字包括 INSERT 、 DELETE 、 UPDATE 、 SELECT 等。
SELECT是SQL语言的基础，最为重要。
DCL（Data Control Language、数据控制语言），用于定义数据库、表、字段、用户的访问权限和
安全级别。
主要的语句关键字包括 GRANT 、 REVOKE 、 COMMIT 、 ROLLBACK 、 SAVEPOINT 等。
因为查询语句使用的非常的频繁，所以很多人把查询语句单拎出来一类：DQL（数据查询语言）。
还有单独将 COMMIT 、 ROLLBACK 取出来称为TCL （Transaction Control Language，事务控制语
言）。

### SQL大小写规范

**MySQL 在 Windows 环境下是大小写不敏感的**
**MySQL 在 Linux 环境下是大小写敏感的**
数据库名、表名、表的别名、变量名是严格区分大小写的
关键字、函数名、列名(或字段名)、列的别名(字段的别名) 是忽略大小写的。
推荐采用统一的书写规范：
数据库名、表名、表别名、字段名、字段别名等都小写
SQL 关键字、函数名、绑定变量等都大写

注释

### mysql 登录

```shell
mysql -h localhost -P 3306 -u root -p
```

### 数据库操作
```sql
-- 创建数据库
create database test;
drop database test
```

### 表操作

mysql 8.0.17 后不需要再int 类型后指定字符长度。

#### 创建表

```sql
ues test;
create table user(
	id int,
	name varchar(255)
);
```

#### 删除表

```sql
drop table user;
```

#### 表结构

```sql
DESCRIBE employees;
或
DESC employees;
```

![image-20220425195630426](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220425195630426.png)

其中，各个字段的含义分别解释如下：
Field：表示字段名称。
Type：表示字段类型，这里 barcode、goodsname 是文本型的，price 是整数类型的。
Null：表示该列是否可以存储NULL值。
Key：表示该列是否已编制索引。PRI表示该列是表主键的一部分；UNI表示该列是UNIQUE索引的一
部分；MUL表示在列中某个给定值允许出现多次。
Default：表示该列是否有默认值，如果有，那么值是多少。
Extra：表示可以获取的与给定列有关的附加信息，例如AUTO_INCREMENT等。

### 表的查询操作

#### 伪表查询

```shell
SELECT 1+1,3*2 FROM DUAL;
```

#### 列的别名

在列名和别名之间加入关键字**AS**，别名使用双引号,也可**省略AS**关键字

```sql
SELECT last_name AS name, commission_pct comm 
FROM employees;
```

**别名使用  **""

```sql
SELECT last_name "Name", salary*12 "Annual Salary"
FROM employees;
```

去重DISTINCT

```sql
SELECT DISTINCT department_id
FROM employees;
```

### 空值参与运算

```sql
SELECT employee_id,salary,commission_pct,
12 * salary * (1 + commission_pct) "annual_sal"
FROM employees;
```

![image-20220425193922761](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220425193922761-16508867747231.png)

当绩效点为null 导致年输入也变为null ，显然是不对的

![image-20220425194405438](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220425194405438.png)

着重号

为了避免 数据库表明和查询名称冲突

错误查询  order 冲突

```sql
mysql> SELECT * FROM ORDER;
ERROR 1064 (42000): You have an error in your SQL syntax; check the manual that
corresponds to your MySQL server version for the right syntax to use near 'ORDER' at
line 1
```



解决办法使用`order`

```sql
mysql> SELECT * FROM `ORDER`;
+----------+------------+
| order_id | order_name |
+----------+------------+
| 1 | shkstart |
| 2 | tomcat |
| 3 | dubbo |
+----------+------------+
3 rows in set (0.00 sec)
```

#### 增加固定常数列

```sql
SELECT '尚硅谷' as corporation, last_name FROM employees;
```

![image-20220425195237307](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220425195237307.png)

# 第4章

## 算术运算

在SQL中 + 没有连接的作用，就表示加法运算，此时字符串转换为数值（隐式转换）结果为101,

```sql
SELECT 100 + '1'
from DUAL
```

取模运算 % mod

查询员工id 数为偶数的结果集

```sql
mysql> SELECT 12 % 3, 12 MOD 5 FROM dual;
+--------+----------+
| 12 % 3 | 12 MOD 5 |
+--------+----------+
| 0 | 2 |
+--------+----------+
1 row in set (0.00 sec)
```

REGEXP 使用正则表达式e$ ,like 使用 %e

```sql
SELECT first_name,last_name
FROM employees
WHERE first_name like '%e';

SELECT first_name,last_name
FROM employees
WHERE first_name REGEXP 'e$';
```



# 第6章

​	多表查询
92sql 99sql 

内连接、外连接、满连接



5.7.1 代码实现
#中图：内连接 A∩B

```sql
SELECT employee_id,last_name,department_name
FROM employees e JOIN departments d
ON e.`department_id` = d.`department_id`;
```

#左上图：左外连接

```sql
SELECT employee_id,last_name,department_name
FROM employees e LEFT JOIN departments d
ON e.`department_id` = d.`department_id`;
```

#右上图：右外连接

```sql
SELECT employee_id,last_name,department_name
FROM employees e RIGHT JOIN departments d
ON e.`department_id` = d.`department_id`;
```

#左中图：A - A∩B

```sql
SELECT employee_id,last_name,department_name
FROM employees e LEFT JOIN departments d
ON e.`department_id` = d.`department_id`
WHERE d.`department_id` IS NULL
```

#右中图：B-A∩B

```sql
SELECT employee_id,last_name,department_name
FROM employees e RIGHT JOIN departments d
ON e.`department_id` = d.`department_id`
WHERE e.`department_id` IS NULL
```

```sql
SELECT employee_id,last_name,department_name
FROM employees e LEFT JOIN departments d
ON e.`department_id` = d.`department_id`
WHERE d.`department_id` IS NULL
UNION ALL #没有去重操作，效率高
SELECT employee_id,last_name,department_name
FROM employees e RIGHT JOIN departments d
ON e.`department_id` = d.`department_id`;
```



```sql
SELECT employee_id,last_name,department_name
FROM employees e LEFT JOIN departments d
ON e.`department_id` = d.`department_id`
WHERE d.`department_id` IS NULL
UNION ALL #没有去重操作，效率高
SELECT employee_id,last_name,department_name
FROM employees e RIGHT JOIN departments d
ON e.`department_id` = d.`department_id`;
```

# 第7章

### 加密函数

# 第11章

## 数据类型

这个`int(M)`我们可以简单的理解为：
这个长度是为了告诉MYSQL数据库，我们这个字段的存储的数据的宽度为M位数, 当然如果你不是M位数（只要在该类型的存储范围之内）MYSQL也能正常存储。。

我们把这个字段的"属性"修改为UNSIGNED ZEROFILL看一下效果

`uid` int(3) unsigned zerofill NOT NULL,

现在我的uid字段：长度(M)=3, 属性=UNSIGNED ZEROFILL（无符号,用0来填充位数）。
设置这个属性后往表时插入数据，系统会自动把uid字段M不够3位的在左侧用0来填充。
效果如下：

insert into `test` (`uid`) VALUES(11);



现在我们应该清楚的知道：长度M与你存放的数值型的数的大小无关.

# 第15章 存储过程与函数

## 存储过程概述

**含义**：存储过程的英文是 Stored Procedure 。它的思想很简单，就是一组经过 预先编译 的 SQL 语句

的封装。

执行过程：存储过程预先存储在 MySQL 服务器上，需要执行的时候，客户端只需要向服务器端发出调用

存储过程的命令，服务器端就可以把预先存储好的这一系列 SQL 语句全部执行。

**好处**： 

1、简化操作，提高了sql语句的重用性，减少了开发程序员的压力 

2、减少操作过程中的失误，提高效率

3、减少网络传输量（客户端不需要把所有的 SQL 语句通过网络发给服务器） 

4、减少了 SQL 语句暴露在网上的风险，也提高了数据查询的安全性



代码举例

```sql
DELIMITER $ 

CREATE PROCEDURE select_all_data() 
BEGIN 
	SELECT * FROM employees;
END $

DELIMITER ;
```

DELIMITER \$  把结束符号换为\$ 符号，

DELIMITER ;   把结束符号换为 **;**



原生Java 调用存储过程命令

> call select_all_data();



## 存储函数

举例 创建存储函数，名称为email_by_id()，参数传入emp_id，该函数查询emp_id的email，并返回，数据类型

为字符串型。

```sql
DELIMITER // 
CREATE FUNCTION email_by_id(emp_id INT) 
RETURNS VARCHAR(25) 
DETERMINISTIC 
CONTAINS SQL 
BEGIN
	RETURN (SELECT email FROM employees WHERE employee_id = emp_id); 
END // 
DELIMITER ;
```

调用

> SET @emp_id = 102; 
>
> SELECT email_by_id(102);

![image-20220826162714856](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220826162714856.png)

## Java 原生代码

### 创建存储函数

```sql
SET GLOBAL log_bin_trust_function_creators = 1;

 delimiter $
 create function func_sum(num1 int,num2 int) returns int 
 begin 
 declare res int;
 set res=num1+num2;
 return res; 
 end$
 delimiter ;
```



```java
public static void main(String[] args) throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.cj.jdbc.Driver"); //用反射的方式将驱动包加载到内存中
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db15?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC","root","666666");
    String sql = "select func_sum(?,?) as res";
    PreparedStatement pstat = conn.prepareStatement( sql );
    pstat.setInt(1,20);
    pstat.setInt(2,230);
    ResultSet rs = pstat.executeQuery();
    while (rs.next()){
        System.out.println(rs.getInt("res"));
    }

}
```



### 创建存储过程

```sql
delimiter $
create procedure proc_sum(in num1 integer,in num2 integer,out he integer)
begin
set he=num1+num2;
end$
delimiter ;
```



```java
public static void main(String[] args) throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.cj.jdbc.Driver"); //用反射的方式将驱动包加载到内存中
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db15?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC","root","666666");
    String sql = "{call proc_sum(?,?,?)}";
    CallableStatement cs = conn.prepareCall(sql);
    cs.setInt(1,20);
    cs.setInt(2,30);
    cs.registerOutParameter(3, Types.INTEGER);
    cs.execute();
    System.out.println(cs.getInt(3));
    conn.close();
}
```

### Mybatis 项目使用存储过程

```sql
CREATE TABLE `t_user`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `age` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'tom', '1', 20);
```



存储过程

```sql
CREATE  PROCEDURE `getUserById`(IN `u_id` int)
BEGIN
	
	SELECT id,name,sex,age FROM t_user WHERE id=u_id;
END
```



```java
@Data
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String sex;

    private Integer age;

}
```

```java
public interface UserMapper extends BaseMapper<User> {
    //根据id查询用户
    User getUserById(Integer id);
}
```

```java
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mybatispro2.mapper.UserMapper">
    <!-- 根据id查询用户 -->
    <select id="getUserById" parameterType="Integer" resultType="com.example.mybatispro2.entity.User" statementType="CALLABLE">
        {call getUserById(#{id,mode=IN})}
    </select>
</mapper>
```

```java
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/getUsers")
    public String getUsers() {

        User user = userMapper.getUserById( 1 );
        System.out.println("添加成功，增加的id="+user.toString());
        return "查询用户成功！";
    }
}
```

```java
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/getUsers")
    public User getUsers() {

        return userMapper.getUserById( 1 );
    }
}
```

http://localhost:8080/user/getUsers

输出结果

```json
{"id":1,"name":"tom","sex":"1","age":20}
```

