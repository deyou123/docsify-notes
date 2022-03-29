## mysql 主从复制

### 使用docker 

创建msyql1

> `docker run --name mysql1 -p 33061:3306 -e MYSQL_ROOT_PASSWORD=123 -d mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci`

创建mysql2

> 
> `docker run --name mysql2  -p 33062:3306 -e MYSQL_ROOT_PASSWORD=123 -d mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci`
> 


也可以使用msyql 8,配置可能复杂点。（自行百度解决）

>`docker run --name mysql8  -p 3306:3306 -e MYSQL_ROOT_PASSWORD=666666 -d mysql:latest --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci`
>


本人使用阿里云服务器。记得安全组开启端口，

![image-20220328192006494](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220328192006494.png)



使用数据库链接工具测试数据库是否创建成功

### Mysql1 授权用户

> `grant replication slave on *.* to 'rep1'@'%' identified by '123';`



![image-20220328203833828](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220328203833828.png)

### 配置mysql1主机

> [root@root ~]# `docker exec -it mysql1 /bin/bash`
> root@b09b62814793:/# `cat  /etc/mysql/mysql.conf.d/mysqld.cnf`

修改配置文件

![image-20220329144607007](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220329144607007.png)



```bash

# Copyright (c) 2014, 2021, Oracle and/or its affiliates.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License, version 2.0,
# as published by the Free Software Foundation.
#
# This program is also distributed with certain software (including
# but not limited to OpenSSL) that is licensed under separate terms,
# as designated in a particular file or component or in included license
# documentation.  The authors of MySQL hereby grant you an additional
# permission to link the program and your derivative works with the
# separately licensed software that they have included with MySQL.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License, version 2.0, for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA

#
# The MySQL  Server configuration file.
#
# For explanations see
# http://dev.mysql.com/doc/mysql/en/server-system-variables.html

[mysqld]
log-bin=/var/lib/mysql/log
# 服务id
server-id=1
#需要同步的数据库
binlog-do-db=javaboydb
pid-file	= /var/run/mysqld/mysqld.pid
socket		= /var/run/mysqld/mysqld.sock
datadir		= /var/lib/mysql
#log-error	= /var/log/mysql/error.log
# By default we only accept connections from localhost
#bind-address	= 127.0.0.1
# Disabling symbolic-links is recommended to prevent assorted security risks
symbolic-links=0

```

在xshell 中

> root@b09b62814793:/# `exit`
> exit
> [root@root ~]# `vim mysqld.cnf`
>
> `## 进入编辑器，修改为上面内容`
>
> [root@root ~]# `docker cp ./mysqld.cnf mysql1:/etc/mysql/mysql.conf.d/`
>
> [root@root ~]# `docker restart mysql1`

使用 数据库连接工具查看

在mysql1 


> `show master status;`

![image-20220329124132168](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220329124132168.png)

### 配置mysql从机

> [root@root ~]# `docker exec -it mysql2 /bin/bash`
>
> root@889c5720a149:/#  `cat  /etc/mysql/mysql.conf.d/mysqld.cnf`
>
> `复制文件内容`
>
> root@889c5720a149:/# `exit`
> exit
>
> [root@root ~]# `rm  mysqld.cnf`
> rm: remove regular file 'mysqld.cnf'? Y
> [root@root ~]# `vim mysqld.cnf`

`编辑mysqld.cnf 如下` 

![image-20220329144725119](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220329144725119.png)

```bash

# Copyright (c) 2014, 2021, Oracle and/or its affiliates.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License, version 2.0,
# as published by the Free Software Foundation.
#
# This program is also distributed with certain software (including
# but not limited to OpenSSL) that is licensed under separate terms,
# as designated in a particular file or component or in included license
# documentation.  The authors of MySQL hereby grant you an additional
# permission to link the program and your derivative works with the
# separately licensed software that they have included with MySQL.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License, version 2.0, for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA

#
# The MySQL  Server configuration file.
#
# For explanations see
# http://dev.mysql.com/doc/mysql/en/server-system-variables.html

[mysqld]
server-id=2
pid-file	= /var/run/mysqld/mysqld.pid
socket		= /var/run/mysqld/mysqld.sock
datadir		= /var/lib/mysql
#log-error	= /var/log/mysql/error.log
# By default we only accept connections from localhost
#bind-address	= 127.0.0.1
# Disabling symbolic-links is recommended to prevent assorted security risks
symbolic-links=0
```

保存退出

> [root@root ~]# `docker cp ./mysqld.cnf mysql2:/etc/mysql/mysql.conf.d/`
>
> [root@root ~]# `docker restart mysql2`

配置msyql2 数据库

> [root@root ~]# `docker exec -it mysql2 /bin/bash`
> root@889c5720a149:/# `mysql -u root -p`
> Enter password: 
> Welcome to the MySQL monitor.  Commands end with ; or \g.
> Your MySQL connection id is 12
> Server version: 5.7.35 MySQL Community Server (GPL)
>
> Copyright (c) 2000, 2021, Oracle and/or its affiliates.
>
> Oracle is a registered trademark of Oracle Corporation and/or its
> affiliates. Other names may be trademarks of their respective
> owners.
>
> Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.
>
> mysql> `change master to master_host='39.103.170.254', master_port=33061,master_user='rep1',master_password='123',master_log_file='log.000001',master_log_pos=154;`
>
> mysql> `start slave;` //配置错误使用stop slave; 然后重新配置。
>
> 


查看配置是否成功 `Slave_IO_Running` 和 `Slave_SQL_Running` 均为yes 配置成功。
> mysql> `show slave status\G;`
>
>
> *************************** 1. row ***************************
>                Slave_IO_State: Waiting for master to send event
>                   Master_Host: 39.103.170.254
>                   Master_User: rep1
>                   Master_Port: 33061
>                 Connect_Retry: 60
>               Master_Log_File: log.000002
>           Read_Master_Log_Pos: 154
>                Relay_Log_File: 889c5720a149-relay-bin.000003
>                 Relay_Log_Pos: 355
>         Relay_Master_Log_File: log.000002
>              Slave_IO_Running: `Yes`
>             Slave_SQL_Running: `Yes`
>               Replicate_Do_DB: 
>           Replicate_Ignore_DB: 
>            Replicate_Do_Table: 
>        Replicate_Ignore_Table: 
>       Replicate_Wild_Do_Table: 
>   Replicate_Wild_Ignore_Table: 
>                    Last_Errno: 0
>                    Last_Error: 
>                  Skip_Counter: 0
>           Exec_Master_Log_Pos: 154
>               Relay_Log_Space: 1492
>               Until_Condition: None
>                Until_Log_File: 
>                 Until_Log_Pos: 0
>            Master_SSL_Allowed: No
>            Master_SSL_CA_File: 
>            Master_SSL_CA_Path: 
>               Master_SSL_Cert: 
>             Master_SSL_Cipher: 
>                Master_SSL_Key: 
>         Seconds_Behind_Master: 0
> Master_SSL_Verify_Server_Cert: No
>                 Last_IO_Errno: 0
>                 Last_IO_Error: 
>                Last_SQL_Errno: 0
>                Last_SQL_Error: 
>   Replicate_Ignore_Server_Ids: 
>              Master_Server_Id: 1
>                   Master_UUID: a925c339-af14-11ec-81be-0242ac110002
>              Master_Info_File: /var/lib/mysql/master.info
>                     SQL_Delay: 0
>           SQL_Remaining_Delay: NULL
>       Slave_SQL_Running_State: Slave has read all relay log; waiting for more updates
>            Master_Retry_Count: 86400
>                   Master_Bind: 
>       Last_IO_Error_Timestamp: 
>      Last_SQL_Error_Timestamp: 
>                Master_SSL_Crl: 
>            Master_SSL_Crlpath: 
>            Retrieved_Gtid_Set: 
>             Executed_Gtid_Set: 
>                 Auto_Position: 0
>          Replicate_Rewrite_DB: 
>                  Channel_Name: 
>            Master_TLS_Version: 
> 1 row in set (0.00 sec)
>
> ERROR: 
> No query specified

检测

在mysql1 中javaboydb 数据库中新建user 表。在msyql2 中同样可以发现。



# springboot Mysql 动态切换数据源

主要功能： 实现了主表（mysql1）写数据，从表(mysql2)读数据。



使用工具 https://github.com/baomidou/dynamic-datasource-spring-boot-starter

本人：阿里云环境主从数据库已经部署好，看博客的小伙伴可以直接使用下面的yml 配置文件。

想自己部署的参考这篇博客本人 https://zhaideyou.blog.csdn.net/article/details/123821990

创建项目: 引入依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.1.4</version>
    </dependency>
    
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
        <version>3.3.1</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
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
```yml
spring:
  datasource:
    dynamic:
      primary: master #设置默认的数据源或者数据源组,默认值即为master
      strict: false #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源.
      datasource:
        master:
          url: jdbc:mysql://39.103.170.254:33061/javaboydb
          username: root
          password: 123
        slave:
          url: jdbc:mysql://39.103.170.254:33062/javaboydb
          username: root
          password: 123
```


```java
@Data
public class User {
    private int id;
    private String username;
}
```



```java
@Mapper
public interface UserMapper {
    @Insert( "insert into javaboydb.user(username) values (#{username})" )
    long addUser(String username);
    @Select( "select * from javaboydb.user" )
    List<User> getAllUser();
}
```



```java
public interface UserService {

    long addUser(String username);

    List<User> getAllUser();
}
```



```java
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    @DS( "master" )
    public long addUser(String username) {
        return userMapper.addUser( username );
    }

    @Override
    @DS( "slave" )
    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }
}
```



测试：

```java
@Autowired
private UserService userService;
@Test
void testAddUser(){
    System.out.println( userService.addUser( "赵六" ) );
}

@Test
void getAllUser(){
    List<User> userList = userService.getAllUser();
    userList.forEach( System.out::println );
}
```



使用mybatis-plus  测试

添加依赖

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.0</version>
</dependency>
```



```java
@Mapper
public interface UserMapper2 extends BaseMapper<User> {
}
```

```java
public interface UserService2  extends IService<User> {


}
```

```java
@Service
public class UserServiceImpl2 extends ServiceImpl<UserMapper2, User> implements UserService2 {

    @Override
    @DS( "slave" )
    public List<User> list() {
        System.out.println("使用读数据库");
        return super.list();
    }

    @Override
    @DS( "master" )
    public boolean save(User entity) {
        return super.save( entity );
    }
}
```

测试

```java
/**
 * Mybatis-plus 测试
 *
 */
@Test
@DisplayName( "使用mybatis-plus 测试" )
void testAddUser2(){

    userService2.save(new User( (int) Math.random(),"j江南一点雨是大佬"));
}

/**
 * Mybatis-plus 测试
 *
 */
@Test
@DisplayName( "使用mybatis-plus 测试" )
void saveUserTest(){
    userService2.list().forEach( System.out::println );
}
```

自己用数据库连接工具查看数据库。
